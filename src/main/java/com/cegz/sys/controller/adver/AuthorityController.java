package com.cegz.sys.controller.adver;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.Authority;
import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.AuthorityView;
import com.cegz.sys.model.view.adver.ContactView;
import com.cegz.sys.service.adver.AuthorityService;
import com.cegz.sys.service.adver.ContactsService;
import com.cegz.sys.service.adver.DrivingRegistrationService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 广告后台控制类
 *
 * @author maqianqian
 * @date 2019年02月22日
 */
@Slf4j
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    @Autowired
    private ServerAck serverAck;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private DrivingRegistrationService drivingRegistrationService;


    /**
     * 版本号
     */
    @Value("${server.version}")
    private String serverVersion;

    /**
     * 服务url
     */
    @Value("${server.api_url}")
    private String serverApiUrl;

    @Value("${message.url}")
    private String messageUrl;

    @Value("${message.account}")
    private String account;

    @Value("${message.pwd}")
    private String pwd;

    @Value("${message.status}")
    private Boolean needStatus;

    /**
     * 校验version
     * @param version
     * @return
     */
    private ResultData checkVersion(String version){
        if(StringUtil.isEmpty(version)){
            return serverAck.getParamError().setMessage("版本号不能为空");
        }
        if(serverVersion != null && !serverVersion.equals(version)){
            return serverAck.getParamError().setMessage("版本错误");
        }
        return null;
    }
    /**
     * 校验token
     *
     * @param request
     * @return
     */
    private ResultData checkToken(HttpServletRequest request) {
        String tokens = request.getHeader("token");
        if (StringUtil.isEmpty(tokens)) {
            return serverAck.getLoginTimeOutError();
        }
        Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return serverAck.getParamError().setMessage("token无效");
        }
        return null;
    }

    /**
     * 获取名单等级为4的有效黑名单数据列表
     * @param request
     * @param version
     * @param curPage
     * @param pageSize
     * @param name
     * @param grade
     * @return
     * @author maqianqian
     * @data 2019年02月26日
     */
    @RequiresPermissions(value = {"authority/getAuthorityList"})
    @PostMapping("getAuthorityList")
    public ResultData getAuthorityList(HttpServletRequest request, String version, Integer curPage, Integer pageSize, String name, Byte grade){
        if(checkVersion(version) != null){
            return checkVersion(version);
        }
        if(checkToken(request) != null){
            return checkToken(request);
        }
        if(curPage == null){
            return serverAck.getParamError().setMessage("分页参数不能为空");
        }
        if(pageSize == null){
            return serverAck.getParamError().setMessage("分页参数不能为空");
        }
        if(StringUtils.isEmpty(name)){
            name = null;
        }
        if(StringUtils.isEmpty(grade)){
            grade = null;
        }

        try {

            //查询等级为4的名单数据数量
            Long count = authorityService.getAuthorityCountByGrade(grade);
            if(count == null){
                return serverAck.getEmptyData();
            }
            Integer startPage = (curPage - 1) * pageSize;// 计算起始页
            //获取名单等级为4的数据列表
            List<Authority>list = authorityService.getAuthorityListByGrade(startPage, pageSize, grade);
           // List<Authority> authorityList = authorityService.getAuthoryListByConditions(startPage, pageSize, name, phone, plateNumber, grade);
            if(list == null || list.size() <= 0){
                return serverAck.getEmptyData();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<AuthorityView> result = new ArrayList<>();
            for(Authority authority : list){
                AuthorityView view = new AuthorityView();
                view.setId(authority.getId());
                view.setGrade(authority.getGrade());
                view.setName(authority.getName());
                view.setIsDeleted(authority.getIsDeleted());

                if(authority.getCreateTime() != null){
                    //加入黑名单时间
                    view.setCreateTime(sdf.format(authority.getCreateTime()));
                }else {
                    view.setCreateTime("");
                }
                //修改时间
                if(authority.getUpdateTime() != null){
                    view.setUpdateTime(sdf.format(authority.getUpdateTime()));
                }else {
                    view.setUpdateTime("");
                }
                //黑名单车主电话
                if(authority.getName() != null){
                    //根据车主名字获取电话号码
                    List<String> phoneList = authorityService.getPhoneByName(authority.getName());
                    if(phoneList == null || phoneList.size() == 0){
                        view.setBeiOperatorPhone("");
                        //设置电话号码为空
                        String phone = "";
                        //一个车主名下可能有多个车牌号
                        String paltNumber = "";
                        //根据车主名字查询车牌号
                        List<String> plateList = authorityService.getPlateNumberByNameAndPhone(authority.getName(), phone);
                        if(plateList == null || plateList.size() == 0){
                              view.setPlateNumber("");
                        }else {
                            for(String plateNumber : plateList){
                                //拼接车牌号
                                paltNumber += plateNumber+" ";
                            }
                            //设置车牌号
                            view.setPlateNumber(paltNumber);
                        }
                    }else {//当查询到车主电话
                        for(String phone : phoneList){
                            //设置车主电话号码
                            view.setBeiOperatorPhone(phone+" ");
                            //根据车主名字和电话号码查询车牌号
                            List<String> plateList = authorityService.getPlateNumberByNameAndPhone(authority.getName(), phone);
                            String paltNumber = "";
                            if(plateList == null || plateList.size() == 0){
                                view.setPlateNumber("");
                            }else {
                                for(String plateNumber : plateList){
                                    //拼接车牌号
                                    paltNumber += plateNumber+" ";
                                }
                                //设置车牌号
                                view.setPlateNumber(paltNumber);
                            }
                        }
                    }
                }
                //审核人电话
                if(authority.getUpdateUserId() != null){
                    view.setOperatorPhone(authority.getUpdateUserId().getPhone());
                }else {
                    view.setOperatorPhone("");
                }
                if(authority.getRemark() != null){
                    view.setRemark(authority.getRemark());
                }else {
                    view.setRemark("");
                }
                result.add(view);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("totalCount", count);
            map.put("result", result);
            return serverAck.getSuccess().setData(map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return serverAck.getServerError();
        }
    }


    /**
     * 添加黑名单中根据输入条件模糊查询车主信息
     * @param request
     * @param curPage
     * @param pageSize
     * @param name 车主姓名
     * @param plateNumber 车牌号码
     * @param phone 车主电话
     * @return
     * @author maqianqian
     * @date 2019年02月26日
     */
    @RequiresPermissions(value = {"authority/queryAuthorityByConditions"})
    @PostMapping("queryAuthorityByConditions")
    public ResultData queryAuthorityByConditions(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
                                               String name, String plateNumber, String phone) {
        if (checkVersion(version) != null) {
            return checkVersion(version);
        }
        if (checkToken(request) != null) {
            return checkToken(request);
        }
        if (curPage == null) {
            return serverAck.getParamError().setMessage("分页参数不能为空");
        }
        if (pageSize == null) {
            return serverAck.getParamError().setMessage("分页参数不能为空");
        }
        if (StringUtils.isEmpty(name)) {
            name = null;
        }
        if (StringUtils.isEmpty(plateNumber)) {
            plateNumber = null;
        }
        if (StringUtils.isEmpty(phone)) {
            phone = null;
        }
        //获取当前登录用户信息
        String tokens = request.getHeader("token");
        if (StringUtil.isEmpty(tokens)) {
            return serverAck.getLoginTimeOutError();
        }
        Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return serverAck.getParamError().setMessage("token无效");
        }

        //查询数据列表分为两种情况。
        try { //1.当输入名字、电话号码时且不输入车牌号时,查询数据不唯一
            if (plateNumber == null) {
                Long count = contactsService.queryAuthorityCountByConditions(name, phone);
                if(name == null){
                    name = "";
                }
                if(phone == null){
                    phone = "";
                }
                //分页查询列表
                Integer startPage = (curPage - 1) * pageSize;
                List<Contacts> contactsList = contactsService.getContactsListByNameOrPhone(startPage, pageSize, name, phone);
                if (contactsList == null || contactsList.size() == 0) {
                    return serverAck.getEmptyData();
                }
                List<ContactView> result = new ArrayList<>();
                for(Contacts contacts : contactsList){
                    //多个车牌号拼接
                    String platNumber = "";
                    //根据contactsId查询车牌
                    Long contactsId = contacts.getId();
                    //获取到每一个车主名下车牌号集合
                    List<String> plateNumberList = drivingRegistrationService.queryPlateNumberByContactsId(contactsId);
                    ContactView view = new ContactView();
                    //车主id：contacts表id
                    view.setId(contactsId);
                    if(plateNumberList == null || plateNumberList.size() == 0){
                        return serverAck.getEmptyData();
                    }

                  for(String plateNum : plateNumberList){
                        platNumber += plateNum+" ";
                  }
                    view.setPlateNumber(platNumber);
                  if(contacts.getName() == null){
                      view.setName("");
                  }else {
                      view.setName(contacts.getName());
                  }
                    if(contacts.getPhone() == null){
                        view.setPhone("");
                    }else {
                        view.setPhone(contacts.getPhone());
                    }

                    result.add(view);
                }
                Map<String, Object>map = new HashMap<>();
                map.put("totalCount", count);
                map.put("result", result);
                return serverAck.getSuccess().setData(map);
            }else {//2.当输入车牌号时
                //DrivingRegistration dr = drivingRegistrationService.queryDrivingRegistrationByPlateNumber(plateNumber);
                //根据车牌，查询记录总数
                Long count = drivingRegistrationService.queryCountByPlateNumber(plateNumber);
                //分页查询列表
                Integer startPage = (curPage - 1) * pageSize;
                //根据车牌号模糊查询
                List<DrivingRegistration> drList = drivingRegistrationService.queryDrivingRegistrationListByPlateNumber(startPage, pageSize, plateNumber);
                if(drList == null || drList.size() == 0){
                    return serverAck.getEmptyData();
                }
                List<ContactView> result = new ArrayList<>();
                for(DrivingRegistration dr : drList){
                    ContactView view = new ContactView();
                    String number = dr.getPlateNumber();
                    view.setPlateNumber(number);
                    Long id = dr.getId();
                    Long contactId = contactsService.getContactsIdByid(id);
                    Contacts contacts = contactsService.getOneByContactId(contactId);
                    String name1 = contacts.getName();
                    String phone1 = contacts.getPhone();
                    if(name1 != null){
                        view.setName(name1);
                    }else {
                        view.setName("");
                    }
                    if(phone1 != null){
                        view.setPhone(phone1);
                    }else {
                        view.setPhone("");
                    }
                    //车主id：contacts表id
                    view.setId(contactId);
                    result.add(view);
                }
                Map<String, Object>map = new HashMap<>();
                map.put("totalCount", count);
                map.put("result", result);
                return serverAck.getSuccess().setData(map);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return serverAck.getServerError();
        }
    }


    /**
     * 新加车主到黑名单中
     * @param request
     * @param version
     * @param remark 加入黑名单原因
     * @return
     * @author maqianqian
     * @date 2019年02月27日
     */
    @RequiresPermissions(value = { "authority/addContactsToAuthority" })
    @PostMapping("addContactsToAuthority")
    @Transactional
    public ResultData addContactsToAuthority(HttpServletRequest request, String version, String remark, Long contacstId){

        if(checkVersion(version) != null){
            return checkVersion(version);
        }
        if(checkToken(request) != null){
            return checkToken(request);
        }
        if(StringUtils.isEmpty(remark)){
            remark = null;
        }

        //获取当前登录用户信息
        String tokens = request.getHeader("token");
        if (StringUtil.isEmpty(tokens)) {
            return serverAck.getLoginTimeOutError();
        }
        Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return serverAck.getParamError().setMessage("token无效");
        }

        if("1".equals(remark)){
            remark = "无";
        }
        if("2".equals(remark)){
            remark = "设备未升级";
        }
        if("3".equals(remark)){
            remark = "行为恶劣";
        }

        try {
            Contacts contacts = contactsService.getOneByContactId(contacstId);
            //获取车主姓名
            String contactsName = contacts.getName();
            Long flg = authorityService.getAuthorityCount(contactsName);
            //Authority authority = authorityService.getOneByContactsName(contactsName);
            if(flg > 0){//黑名单中已经存在该车主
                return serverAck.getParamError().setMessage("该用户已在黑名单中");
            }else {
                //黑名单等级
                Byte grade = 4;
                Byte isDeleted = 0;
                //加入黑名单时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date());
                //审核者电话
                //Users users = authorityService.getUpdateUserPhoneById(updateUserId);
                //tring updatePhone = users.getPhone();
                //updateUserId审核者id对应创建者外键
                Long createUserId = contacts.getCreateUserId().getId();
                Long updateUserId = user.getId();
                authorityService.addContactsToAuthority(contactsName, grade, date, createUserId, remark, isDeleted, updateUserId);
                return serverAck.getSuccess();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return serverAck.getServerError();
        }
    }

    /**
     * 根据id将车主移出黑名单
     * @param request
     * @param version
     * @param id
     * @author maqianqian
     * @date 2019年02月28日
     * @return
     */
    @RequiresPermissions(value = { "authority/relieveAuthorityById" })
    @PostMapping("relieveAuthorityById")
    @Transactional
    public ResultData relieveAuthorityById(HttpServletRequest request, String version, Long id){
        if(checkVersion(version) != null){
            return checkVersion(version);
        }
        if(checkToken(request) != null){
            return checkToken(request);
        }
        if(id == null){
            return serverAck.getEmptyData();
        }

        try {
            Optional<Authority> authority = authorityService.getAuthorityById(id);
            if(authority != null){
                //根据id将车主移出黑名单
                authorityService.relieveAuthorityById(id);
                return serverAck.getSuccess();
            }else {
                return serverAck.getServerError();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return serverAck.getServerError();
        }
    }

    /**
     * 在黑名单中根据条件模糊查询有效且等级为4的车主列表数据
     * @param request
     * @param version
     * @param curPage
     * @param pageSize
     * @param name
     * @param phone
     * @param plateNumber
     * @return
     */
    @RequiresPermissions(value = { "authority/getAuthorityByConditions" })
    @PostMapping("getAuthorityByConditions")
    public ResultData getAuthorityByConditions(HttpServletRequest request, String version, Integer curPage, Integer pageSize, String name, String phone, String plateNumber){
        if(checkVersion(version) != null){
            return checkVersion(version);
        }
        if(checkToken(request) != null){
            return checkToken(request);
        }
        if (curPage == null) {
            return serverAck.getParamError().setMessage("分页参数不能为空");
        }
        if (pageSize == null) {
            return serverAck.getParamError().setMessage("分页参数不能为空");
        }
        if(StringUtils.isEmpty(plateNumber)){
            plateNumber = null;
        }

        try {
            //按是否输出车牌号分类查询
            //1.当输入车牌号,根据车牌号查询
            if(plateNumber != null){
                Integer count = 0 ;
                //分页查询列表
                Integer startPage = (curPage - 1) * pageSize;
                //根据车牌号模糊查询
                List<DrivingRegistration> drList = drivingRegistrationService.queryDrivingRegistrationListByPlateNumber(startPage, pageSize, plateNumber);
                Integer ll = drList.size();
                if(drList == null || drList.size() == 0){
                    return serverAck.getEmptyData();
                }
                List<AuthorityView> result = new ArrayList<>();
                for(DrivingRegistration dr : drList){
                    AuthorityView view = new AuthorityView();
                    Long drId = dr.getId();
                    Long contactId = contactsService.getContactsIdByid(drId);
                    Contacts contacts = contactsService.getOneByContactId(contactId);
                    String name1 = contacts.getName();
                    //根据名字查询有效且等级为4的数据
                    Authority authority = authorityService.getAuthorityByName(name1);
                    //根据车主名字判断，该车主时候在黑名单中
                    if(authority == null){//不在该黑名单中，这跳出本次循环，执行下一次
                        continue;
                    }
                    //按条件查询获得的数据总条数
                    count = count + 1 ;
                    //当名字在黑名单中时才赋值
                    String phone1 = contacts.getPhone();
                    if(name1 == null){
                        view.setName("");
                    }else {
                        view.setName(name1);
                    }
                    if(phone1 == null){
                        view.setBeiOperatorPhone("");
                    }else {
                        view.setBeiOperatorPhone(phone1);
                    }
                    //根据名字和电话查询该车主名下车牌号集合
                    List<String> plateNumberList = authorityService.getPlateNumberByNameAndPhone(name1, phone1);
                    //车牌号集合字符串
                    String plateNumberStr = "";
                    for(String plateNumber1 : plateNumberList){
                        plateNumberStr += plateNumber1+" ";
                    }
                    view.setPlateNumber(plateNumberStr);
                    //根据名字或者电话查询有效且等级为4的数据
                    //Authority authority = authorityService.getAuthorityByName(name1);
                    //Authority authority = authorityService.getAuthorityByNameAndPhone(name1, phone1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if(authority.getCreateTime() == null ){
                        view.setCreateTime("");
                    }else {
                        view.setCreateTime(sdf.format(authority.getCreateTime()));
                    }
                    if(authority.getUpdateTime() == null ){
                        view.setUpdateTime("");
                    }else {
                        view.setUpdateTime(sdf.format(authority.getUpdateTime()));
                    }
                    if(authority.getRemark() == null || ("").equals(authority.getRemark())){
                        view.setRemark("");
                    }else {
                        view.setRemark(authority.getRemark());
                    }
                    if(authority.getUpdateUserId() == null){
                        view.setOperatorPhone("");
                    }else {
                        view.setOperatorPhone(authority.getUpdateUserId().getPhone());
                    }

                    result.add(view);
                }
                if(count == 0){
                    return serverAck.getEmptyData();
                }else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("totalCount", count);
                    map.put("result", result);
                    return serverAck.getSuccess().setData(map);
                }

            }else {//2.当没有输出车牌号，根据姓名或者电话号码
                Integer count = 0 ;
                Integer startPage = (curPage - 1) * pageSize;
                //根据名字或者电话号码模糊查询数据列表，分页展示
                //List<Contacts> contactsList = contactsService.getContactsListByNameOrPhone(startPage, pageSize, name, phone);
                List<Contacts> contactsList = contactsService.getContactsListByNameOrPhone(startPage, pageSize, name, phone);
                if(contactsList == null || contactsList.size() == 0){
                    return serverAck.getEmptyData();
                }
                List<AuthorityView> result = new ArrayList<>();
                for(Contacts contacts : contactsList){
                    AuthorityView view = new AuthorityView();
                    String name1 = contacts.getName();
                    //根据名字查询有效且等级为4的数据
                    Authority authority = authorityService.getAuthorityByName(name1);
                    //根据车主名字判断，该车主时候在黑名单中
                    if(authority == null){//不在该黑名单中，这跳出本次循环，执行下一次
                        continue;
                    }
                    //按条件查询获得的数据总条数
                    count = count + 1;
                    //当名字在黑名单中时才赋值
                    String phone1 = contacts.getPhone();
                    if(name1 == null || ("").equals(name1)){
                        view.setName("");
                    }else {
                        view.setName(name1);
                    }
                    if(phone1 == null || ("").equals(phone1)){
                        view.setBeiOperatorPhone("");
                    }else {
                        view.setBeiOperatorPhone(phone1);
                    }
                    //多个车牌号拼接
                    String platNumber = "";
                    Long contactsId = contacts.getId();
                    //获取到每一个车主名下车牌号集合
                    List<String> plateList = drivingRegistrationService.queryPlateNumberByContactsId(contactsId);
                    if(plateList == null || plateList.size() == 0){
                        return serverAck.getEmptyData();
                    }
                    for(String plateNumber1 : plateList){
                        //拼接车牌号
                        platNumber += plateNumber1+" ";
                    }
                    view.setPlateNumber(platNumber);
                    //根据名字查询有效且等级为4的数据
                    //Authority authority = authorityService.getAuthorityByName(name1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if(authority.getCreateTime() == null ){
                        view.setCreateTime("");
                    }else {
                        view.setCreateTime(sdf.format(authority.getCreateTime()));
                    }
                    if(authority.getUpdateTime() == null ){
                        view.setUpdateTime("");
                    }else {
                        view.setUpdateTime(sdf.format(authority.getUpdateTime()));
                    }
                    if(authority.getRemark() == null || ("").equals(authority.getRemark())){
                        view.setRemark("");
                    }else {
                        view.setRemark(authority.getRemark());
                    }
                    if(authority.getUpdateUserId() == null){
                        view.setOperatorPhone("");
                    }else {
                        view.setOperatorPhone(authority.getUpdateUserId().getPhone());
                    }
                    result.add(view);
                }
                if(count == 0){//当没有查询到车主信息
                    return serverAck.getEmptyData();
                }else {//当查询到车主信息
                    Map<String, Object> map = new HashMap<>();
                    map.put("totalCount", count);
                    map.put("result", result);
                    return serverAck.getSuccess().setData(map);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return serverAck.getServerError();
        }
    }


}


