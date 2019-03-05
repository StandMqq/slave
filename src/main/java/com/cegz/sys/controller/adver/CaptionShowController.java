package com.cegz.sys.controller.adver;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.CaptionShow;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.CaptionShowView;
import com.cegz.sys.service.adver.CaptionShowService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 字幕后台控制类
 * @author  maqianqian
 * @date 2019年02月21日
 */

@Slf4j
@RestController
@RequestMapping("/caption")
public class CaptionShowController {

    @Autowired
    private ServerAck serverAck;

    @Autowired
    private CaptionShowService captionShowService;

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
     * @param request
     * @return
     * @author  maqianqian
     * @date 2019年02月21日
     */
    private ResultData checkToken(HttpServletRequest request){
        String tokens = request.getHeader("token");
        if(StringUtil.isEmpty(tokens)){
            return serverAck.getLoginTimeOutError();
        }
        Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
        Users user = (Users) session.getAttribute("user");
        if(user == null){
            return serverAck.getParamError().setMessage("token无效");
        }
        return null;
    }


    /**
     * 获取有效的字幕内容
     * @param request
     * @param version
     * @param isDeleted
     * @return
     * @author  maqianqian
     * @date 2019年02月21日
     */
    @RequiresPermissions(value = "caption/getCaptionShowContentByIsDeleted")
    @PostMapping("getCaptionShowContentByIsDeleted")
    public ResultData getCaptionShowContentByIsDeleted(HttpServletRequest request, String version, Byte isDeleted){
        if(checkVersion(version) != null){
            return checkVersion(version);
        }
        if(checkToken(request) != null){
            return checkToken(request);
        }
        if(isDeleted == null){
            return serverAck.getParamError().setMessage("状态不能为空");
        }


        try {
            //筛选出状态为0的字幕数据    状态， 0 有效， 1 无效
            CaptionShow captionShow = captionShowService.getCaptionShowContentByIsDeleted(isDeleted);
            if(captionShow != null && !("").equals(captionShow)){
                String content = captionShow.getContent();
                CaptionShowView view = new CaptionShowView();
                view.setContent(content);
                view.setId(captionShow.getId());
                view.setIsDeleted(captionShow.getIsDeleted());
                Map<String, Object> map = new HashMap<>();
                //有效的字幕在数据库条数只有一条
                map.put("total", 1);
                map.put("view", view);
                return serverAck.getSuccess().setData(map);
            }else {
                return serverAck.getEmptyData().setMessage("无相关数据");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return serverAck.getServerError();
        }
    }

    /**
     * 根据id修改字幕内容
     * @param request
     * @param version
     * @param id
     * @param updateContent
     * @return
     */
    @RequiresPermissions(value = "caption/updateCaptionShowContentById")
    @PostMapping("updateCaptionShowContentById")
    @Transactional
    public ResultData updateCaptionShowContentById(HttpServletRequest request, String version, Long id, String updateContent){
        if(checkVersion(version) != null){
            return checkVersion(version);
        }
        if(checkToken(request) != null){
            return checkToken(request);
        }
        if(updateContent == null){
            updateContent = "";
        }
        //字幕个数限制在6个以内，超过6个就不能更改
        if(updateContent.length() > 6){
            return serverAck.getFailure();
        }
        String tokens = request.getHeader("token");
        if (StringUtil.isEmpty(tokens)) {
            return serverAck.getLoginTimeOutError();
        }
        Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return serverAck.getParamError().setMessage("token无效");
        }

        try {
            if(id == null){
                Byte isDeleted = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = sdf.format(new Date());
                Long createUserId = user.getId();
                //当id为空时，新增字幕
                captionShowService.addCaptionShowContent(createUserId, isDeleted, createTime, updateContent);
            }else {
                //id不为空时，根据id修改字幕内容
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String updateTime = sdf.format(new Date());
                captionShowService.updateCaptionShowContentById(id, updateContent, updateTime);
            }
            return serverAck.getSuccess().setMessage("修改成功，请及时查看小程序");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return serverAck.getParamError();
        }
    }
}
