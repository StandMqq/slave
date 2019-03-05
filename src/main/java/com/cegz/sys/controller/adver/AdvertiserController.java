package com.cegz.sys.controller.adver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.Advertiser;
import com.cegz.sys.model.adver.DataDictionary;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.AdvertisementView;
import com.cegz.sys.model.view.adver.AdvertiserView;
import com.cegz.sys.service.adver.AdvertisementService;
import com.cegz.sys.service.adver.AdvertiserService;
import com.cegz.sys.service.adver.DataDictionaryService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.URLConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 广告主后台控制类
 * 
 * @author tenglong
 * @date 2018年7月31日
 */
@Slf4j
@RestController
@RequestMapping("/advertiser")
public class AdvertiserController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AdvertiserService advertiserService;

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

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
	 * 
	 * @param version
	 * @return
	 */
	private ResultData checkVersion(String version) {
		if (StringUtil.isEmpty(version)) {
			return serverAck.getParamError().setMessage("版本号不能为空");
		}
		if (serverVersion != null && !serverVersion.equals(version)) {
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
	 * 获取 广告主待审核列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "advertiser/getExamineList" })
	@PostMapping("getExamineList")
	public ResultData getExamineList(String version, Integer curPage, Integer pageSize, Integer status, String name,
			String phone, String registerPhone, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (status == null) {
			return serverAck.getParamError().setMessage("状态不能为空");
		}
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		if (curPage == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		if (pageSize == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = advertiserService.queryAdvertiserExamineTotalCount(status, name, phone, registerPhone);
			// 广告主审核数据列表
			List<Advertiser> list = advertiserService.listAdvertiserExamine(startPage, pageSize, status, name, phone,
					registerPhone);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<AdvertiserView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Advertiser advertiser : list) {
				AdvertiserView view = new AdvertiserView();
				view.setId(advertiser.getId());
				view.setCity(advertiser.getCity());
				view.setCompany(advertiser.getCompany());
				view.setName(advertiser.getName());
				view.setPhone(advertiser.getPhone());
				view.setEmail(advertiser.getEmail());
				view.setAddress(advertiser.getAddress());
				view.setAddressDetail(advertiser.getAddressDetail());
				view.setStatus(advertiser.getStatus());
				view.setCreateTime(sdf.format(advertiser.getCreateTime()));
				result.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("result", result);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过id获取单条 广告主
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "advertiser/getAdvertiserById" })
	@PostMapping("getAdvertiserById")
	public ResultData getAdvertiserById(Long id, String version, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("主键不能为空");
		}
		try {
			// 广告主信息审核数据
			Optional<Advertiser> advertisersOpt = advertiserService.getAdvertiserById(id);
			if (!advertisersOpt.isPresent()) {
				return serverAck.getParamError().setMessage("广告主ID有误");
			}
			Advertiser advertiser = advertisersOpt.get();
			AdvertiserView view = new AdvertiserView();
			view.setId(advertiser.getId());
			view.setCity(advertiser.getCity());
			view.setCompany(advertiser.getCompany());
			view.setBusinessLicense(advertiser.getBusinessLicense());
			view.setPictureUrl(advertiser.getPictureUrl());
			view.setName(advertiser.getName());
			view.setPhone(advertiser.getPhone());
			view.setEmail(advertiser.getEmail());
			view.setAddress(advertiser.getAddress());
			view.setAddressDetail(advertiser.getAddressDetail());
			view.setStatus(advertiser.getStatus());

			// 查询数据字典中的审核理由
			List<DataDictionary> dataDictionaryList = dataDictionaryService.getDataDictionaryByCode("001");
			Map<String, Object> map = new HashMap<>();
			map.put("dataDictionaryList", dataDictionaryList);
			map.put("view", view);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 广告主信息状态审核
	 * 
	 * @param id
	 * @param token
	 * @param reason
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "advertiser/advertiserStatusExamine" })
	@PostMapping("advertiserStatusExamine")
	public ResultData advertiserStatusExamine(Long id, Integer status, String reason, String version,
			HttpServletRequest request, String city, String company, String businessNumber, String name, String phone,
			String email, String address, String addressDetail) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
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
		if (id == null) {
			return serverAck.getParamError().setMessage("主键不能为空");
		}
		if (status == null || status == 0) {// 审核状态 0 审核中，1 通过，2 未通过
			return serverAck.getParamError().setMessage("审核状态必填且不为0");
		}
		if (status != null && status != 0 && status == 2 && StringUtil.isEmpty(reason)) {// 审核状态为未通过原因必填
			return serverAck.getParamError().setMessage("审核未通过时原因不能为空");
		}
		if (status != null && 1 == status) {// 审核通过时，校验其他信息
			if (StringUtils.isEmpty(city)) {
				return serverAck.getParamError().setMessage("城市不能为空");
			}
			if (StringUtils.isEmpty(company)) {
				return serverAck.getParamError().setMessage("公司名称不能为空");
			}
			if (StringUtils.isEmpty(businessNumber)) {
				return serverAck.getParamError().setMessage("营业执照编号不能为空");
			}
			if (StringUtils.isEmpty(name)) {
				return serverAck.getParamError().setMessage("联系人不能为空");
			}
			if (StringUtils.isEmpty(phone)) {
				return serverAck.getParamError().setMessage("电话不能为空");
			}
		}
		try {
			// 处理
			// 获取 广告主信息
			Optional<Advertiser> advertisersOpt = advertiserService.getAdvertiserById(id);
			if (!advertisersOpt.isPresent()) {
				return serverAck.getParamError().setMessage("广告主ID有误");
			}
			Advertiser advertiser = advertisersOpt.get();

			advertiser.setStatus(status.byteValue());
			advertiser.setReason(status.byteValue() == 1 ? null : reason);// 如果状态为1，原因置为null
			advertiser.setUpdateTime(new Date());
			int ret = advertiserService.advertiserStatusExamine(advertiser.getId(), advertiser.getStatus(),
					advertiser.getReason(), advertiser.getUpdateTime(), city, company, businessNumber, name, phone,
					email, address, addressDetail);
//			int ret = advertiserService.save(advertiser);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (ret == 0) {
				// 记录失败日志
				log.info("广告主审核失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ advertiser.getStatus() + ";" + "审核内容-->" + advertiser.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}
			// 记录成功日志
			log.info("广告主审核成功：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
					+ advertiser.getStatus() + ";" + "审核内容-->" + advertiser.getReason() + ";" + "审核时间-->"
					+ df.format(new Date()));

			// 发送短信内容组装
			Map<String, Object> paramMap = new HashMap<>(16);
			if (!StringUtils.isEmpty(advertiser.getPhone())) {
				paramMap.put("mobile", advertiser.getPhone());
			} else if (advertiser.getCreateUserId() != null
					&& !StringUtils.isEmpty(advertiser.getCreateUserId().getPhone())) {
				paramMap.put("mobile", advertiser.getCreateUserId().getPhone());
			} else {
				// 记录失败日志
				log.info("广告主审核通过后发送短信失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ advertiser.getStatus() + ";" + "审核内容-->" + advertiser.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->未找到发送短信号码;");
				return serverAck.getFailure();
			}
			paramMap.put("account", account);
			paramMap.put("pswd", pwd);
			paramMap.put("needstatus", needStatus);

			// 审核状态为通过时，发送短信通知被审核人，话术：您的广告主资质审核已通过，请前往“车而告之”微信小程序查看
			if (1 == status.byteValue()) {
				paramMap.put("msg", "您的广告主资质审核已通过，请前往“车而告之”微信小程序查看");
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (!StringUtils.isEmpty(advertiser.getPhone())) {
					log.info("{}发送短信{},結果{}", advertiser.getPhone(), "广告主资质审核通过", retSms);
				} else {
					log.info("{}发送短信{},結果{}", advertiser.getCreateUserId().getPhone(), "广告主资质审核通过", retSms);
				}
			}
			// 审核状态为失败时，发送短信通知被审核人
			if (2 == status.byteValue()) {
				paramMap.put("msg", "您的广告主资质审核失败，请前往“车而告之”微信小程序查看");
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (!StringUtils.isEmpty(advertiser.getPhone())) {
					log.info("{}发送短信{},結果{}", advertiser.getPhone(), "广告主资质审核失败", retSms);
				} else {
					log.info("{}发送短信{},結果{}", advertiser.getCreateUserId().getPhone(), "广告主资质审核失败", retSms);
				}
			}

			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取广告主列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "advertiser/getAdvertiserList" })
	@PostMapping("getAdvertiserList")
	public ResultData getAdvertiserList(String version, Integer curPage, Integer pageSize, Integer status, String name,
			String phone, String registerPhone, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (status == null) {
			return serverAck.getParamError().setMessage("状态不能为空");
		}
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		if (curPage == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		if (pageSize == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = advertiserService.queryAdvertiserExamineTotalCount(status, name, phone, registerPhone);
			// 广告主审核数据列表
			List<Advertiser> list = advertiserService.listAdvertiserExamine(startPage, pageSize, status, name, phone,
					registerPhone);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<AdvertiserView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Advertiser advertiser : list) {
				AdvertiserView view = new AdvertiserView();
				view.setId(advertiser.getId());
				view.setCity(advertiser.getCity());
				List<AdvertisementView> listTemp = advertisementService
						.getAdvertisementsByAdvertiserId(advertiser.getId());
				view.setAdvertisementNum(Long.valueOf(listTemp == null ? 0 : listTemp.size()));
				view.setCompany(advertiser.getCompany());
				view.setName(advertiser.getName());
				view.setPhone(advertiser.getPhone());
				view.setEmail(advertiser.getEmail());
				view.setAddress(advertiser.getAddress());
				view.setAddressDetail(advertiser.getAddressDetail());
				view.setStatus(advertiser.getStatus());
				view.setBusinessLicense(advertiser.getBusinessLicense());
				view.setCreateTime(sdf.format(advertiser.getCreateTime()));
				result.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("result", result);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过广告主id获取广告主详情
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "advertiser/getAdvertiserDetailById" })
	@PostMapping("getAdvertiserDetailById")
	public ResultData getAdvertiserDetailById(Long id, String version, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		try {
			// 广告主数据详情
			AdvertiserView advertiserView = advertiserService.getAdvertiserDetailById(id);
			return serverAck.getSuccess().setData(advertiserView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过广告主id获取广告主下的所有广告
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月7日
	 */

	@RequiresPermissions(value = { "advertiser/getAdvertisementByAdvertiserId" })
	@PostMapping("getAdvertisementByAdvertiserId")
	public ResultData getAdvertisementByAdvertiserId(Long id, String version, Integer curPage, Integer pageSize,
			Integer status, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("广告主id不能为空");
		}
		if (status == null) {
			return serverAck.getParamError().setMessage("状态不能为空");
		}
		if (curPage == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		if (pageSize == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = advertisementService.queryAdvertisementCountByAdvertiserId(id, status);
			// 广告主下的广告列表
			List<AdvertisementView> result = advertisementService.getAdvertisementsByAdvertiserIdLimit(id, status,
					startPage, pageSize);
			if (result == null || result.size() <= 0) {
				return serverAck.getEmptyData();
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("result", result);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
