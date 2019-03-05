package com.cegz.sys.controller.adver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.*;
import com.cegz.sys.model.view.adver.SubscribeDesignView;
import com.cegz.sys.service.adver.*;
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

import com.alibaba.fastjson.JSONObject;
import com.cegz.sys.model.shop.RedPacket;
import com.cegz.sys.model.view.adver.AdvertisementView;
import com.cegz.sys.model.view.adver.PriceView;
import com.cegz.sys.redis.RedisUtil;
import com.cegz.sys.service.shop.RedPacketService;
import com.cegz.sys.util.Constant;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.TokenUtil;
import com.cegz.sys.util.URLConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 广告后台控制类
 * 
 * @author tenglong
 * @date 2018年8月6日
 */
@Slf4j
@RestController
@RequestMapping("/advertisement")
public class AdvertisementController {

	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeviceService deviceService;

	// 黑名单
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private RedPacketService redPacketService;

	@Autowired
	private SubscribeDesignService subscribeDesignService;

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
	 * 获取广告信息审核数据列表
	 *
	 * @param pageSize        分页参数
	 * @param name            广告名称
	 * @param advertiserName  广告主名称
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月13日
	 */
	@RequiresPermissions(value = { "advertisement/getExamineList" })
	@PostMapping("getExamineList")
	public ResultData getExamineList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Integer status, String name, String advertiserName, String advertiserPhone, String typeNo) {
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
		if (status == null) {
			return serverAck.getParamError().setMessage("状态不能为空");
		}
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(advertiserName)) {
			advertiserName = null;
		}
		if (StringUtils.isEmpty(advertiserPhone)) {
			advertiserPhone = null;
		}
		try {
			List<AdvertisementView> result = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = advertisementService.queryAdvertisementCount(status, name, advertiserName,
					advertiserPhone, typeNo);

			// 广告订单审核数据列表
			List<Advertisement> list = advertisementService.getAdvertisementList(startPage, pageSize, status, name,
					advertiserName, advertiserPhone, typeNo);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Advertisement a : list) {
				AdvertisementView view = new AdvertisementView();
				view.setId(a.getId());
				view.setTitle(a.getTitle());
				if (a.getAdvertiser() != null) {
					view.setAdvertiserName(a.getAdvertiser().getName());
					view.setAdvertiserPhone(a.getAdvertiser().getPhone());
				}
				view.setAdvertisementType(a.getAdvertisementType().getTypeNo());
				view.setAdvertisementTypeName(a.getAdvertisementType().getName());
				view.setTitleImgUrl(a.getTitlePicUrl());
				view.setContentImages(a.getContentPicUrl());
				view.setContent(a.getContent());
				if(a.getStatus() == 2) {
					if (a.getUpdateTime() != null) {
						view.setCreateTime(sdf.format(a.getUpdateTime()));
					}
				}else {
					view.setCreateTime(sdf.format(a.getCreateTime()));
				}
				if (a.getCreateUserId() != null) {
					view.setCreatePhone(a.getCreateUserId().getPhone());
				}
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
	 * 通过id获取单条广告数据
	 * 
	 * @param id
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "advertisement/getAdvertisementById" })
	@PostMapping("getAdvertisementById")
	public ResultData getAdvertisementById(Long id, HttpServletRequest request, String version) {
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
			// 广告信息审核数据
//			Optional<Advertisement> advertisementOpt = advertisementService.getAdvertisementById(id);
//			if (!advertisementOpt.isPresent()) {
//				return serverAck.getParamError().setMessage("广告ID有误");
//			}
			Advertisement advertisement = advertisementService.getAdvertisementByIdV2(id);
			if (advertisement == null) {
				return serverAck.getParamError().setMessage("广告ID有误");
			}
//			Advertisement advertisement = advertisementOpt.get();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			AdvertisementView view = new AdvertisementView();
			view.setId(advertisement.getId());
			view.setTitle(advertisement.getTitle());
			if (advertisement.getAdvertiser() != null) {
				view.setAdvertiserName(advertisement.getAdvertiser().getName());
				view.setAdvertiserPhone(advertisement.getAdvertiser().getPhone());
			}
			if (advertisement.getAdvertisementType() != null) {
				view.setAdvertisementType(advertisement.getAdvertisementType().getTypeNo());
				String typeNo = advertisement.getAdvertisementType().getTypeNo();
				System.out.println(typeNo);
				view.setAdvertisementTypeName(advertisement.getAdvertisementType().getName());
			}
			// 获取标题地址
			view.setTitleImgUrl(advertisement.getTitlePicUrl());
			// 获取内容图片地址
			view.setContentImages(advertisement.getContentPicUrl());
			// 获取logo图片地址
			view.setIconImgUrl(advertisement.getIconUrl());
			// 获取创建时间
			view.setCreateTime(sdf.format(advertisement.getCreateTime()));
			view.setStatus(advertisement.getStatus());
			if (advertisement.getPrice() != null) {
				view.setPriceId(advertisement.getPrice().getId());
				view.setPriceName(advertisement.getPrice().getName());
				view.setPrice(advertisement.getPrice().getPrice());
			}
			// 获取类型为5的红包广告经度纬度

			if (advertisement.getAdvertisementType() != null && advertisement.getAdvertisementType().getId() == 7) {
				RedPacket redpacket = redPacketService.getRedPacketByAdvertisementId(advertisement.getId(), 5);
				// 获取经度
				view.setLng(redpacket.getLng());
				// 获取纬度
				view.setLat(redpacket.getLat());
			}

			view.setContent(advertisement.getContent());

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
	 * 广告订单信息状态审核（审核通过后，发布广告）
	 * 

	 * @param reason
	 * @return
	 * @author yucheng
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "advertisement/advertisementStatusExamine" })
	@PostMapping("advertisementStatusExamine")
	public ResultData advertisementStatusExamine(Long id, Integer status, String reason, HttpServletRequest request,
			String version) {
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
		if (status == null || status == 0) {// 审核状态 0 审核中，1 完成 2失败
			return serverAck.getParamError().setMessage("审核状态必填且不为0");
		}
		if (status != null && status != 0 && status == 2 && StringUtil.isEmpty(reason)) {// 审核状态为未通过原因必填
			return serverAck.getParamError().setMessage("审核未通过时原因不能为空");
		}
		try {
			// 处理
			// 获取广告信息
			Advertisement advertisement = advertisementService.getAdvertisementByIdV2(id);
			if (advertisement == null || advertisement.getId() == null) {
				return serverAck.getParamError().setMessage("广告ID有误");
			}
			advertisement.setStatus(status.byteValue());
			advertisement.setReason(status.byteValue() == 1 ? null : reason);// 如果状态为1，原因置为null
			advertisement.setUpdateTime(new Date());

			int ret = advertisementService.advertisementStatusExamine(advertisement.getId(), advertisement.getStatus(),
					advertisement.getReason(), advertisement.getUpdateTime());
			if(status == 1) {
				Map<String, Object> paramMap1 = new HashMap<String, Object>();
				paramMap1.put("advertisementId", id);
				paramMap1.put("version", version);
				try {
					// 修改token为用户token，而不是直接用登录人token
					Advertiser advertiser = advertisement.getAdvertiser();
					if (advertiser != null) {
						// 获取token
						String advertiserToken = TokenUtil.getToken(Constant.DES_KEY,
								advertiser.getCreateUserId().getUserName());
						paramMap1.put("token", URLEncoder.encode(advertiserToken, "utf8"));
					} else {
						return serverAck.getFailure().setMessage("广告主信息不存在");
					}
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
					return serverAck.getFailure().setMessage("token编码失败");
				}
				
				Order order = orderService.getOrderByAdvertisementId(id);
				if(order != null) {
					if(order.getRegionId() != null) {
						try {
							String doPost = null; 
							doPost = URLConnectionUtil.doPost(serverApiUrl + "advertisement-v2/publishRegionAdvertisement", paramMap1);
							if (StringUtil.isEmpty(doPost)) {
								return serverAck.getEmptyData();
							}
							JSONObject json = JSONObject.parseObject(doPost);
							if (!"200".equals(json.getString("code"))) {
								// {"code":400,"message":"版本号不能为空","success":false,"data":null,"hasNext":false}
								ResultData resultData = JSONObject.parseObject(doPost.toString(), ResultData.class);
								return resultData;
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							return serverAck.getServerError();
						}
					}else {
						try {
							String doPost = null; 
							doPost = URLConnectionUtil.doPost(serverApiUrl + "advertisement-v2/publishAdvertisement", paramMap1);
							if (StringUtil.isEmpty(doPost)) {
								return serverAck.getEmptyData();
							}
							JSONObject json = JSONObject.parseObject(doPost);
							if (!"200".equals(json.getString("code"))) {
								// {"code":400,"message":"版本号不能为空","success":false,"data":null,"hasNext":false}
								ResultData resultData = JSONObject.parseObject(doPost.toString(), ResultData.class);
								return resultData;
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							return serverAck.getServerError();
						}
					}
					
				}
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (ret == 0) {
				// 记录失败日志
				log.info("广告审核失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ advertisement.getStatus() + ";" + "审核内容-->" + advertisement.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}

			if (status.byteValue() == 1 && advertisement.getAdvertisementType().getTypeNo().equals("002")) { // 需求修改为文字广告审核通过直接发布，且不发送短信
				// 发布文字广告
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("version", version);

				// 获取广告创建人token
				String token = TokenUtil.getToken(Constant.DES_KEY, advertisement.getCreateUserId().getUserName());
				paramMap.put("token", URLEncoder.encode(token, "utf8"));
				paramMap.put("adverId", advertisement.getId());
				try {
					String doPost = URLConnectionUtil.doPost(serverApiUrl + "advertisement/publishAdvertisementWord",
							paramMap);
					log.info("调用发布文字广告接口返回值--->doPost：", doPost);
					System.out.println("调用发布文字广告接口返回值--->doPost：" + doPost);
					if (StringUtil.isEmpty(doPost)) {
						return serverAck.getEmptyData();
					}
					JSONObject json = JSONObject.parseObject(doPost);
					if (!"200".equals(json.getString("code"))) {
						// {"code":400,"message":"版本号不能为空","success":false,"data":null,"hasNext":false}
						ResultData resultData = JSONObject.parseObject(doPost.toString(), ResultData.class);
						return resultData;
					}

					if (status.byteValue() == 1 && advertisement.getAdvertisementType().getTypeNo().equals("002")) { // 需求修改为文字广告审核通过直接发布，且不发送短信
						advertisement.setStatus(3);
						advertisementService.advertisementStatusExamine(advertisement.getId(),
								advertisement.getStatus(), advertisement.getReason(), advertisement.getUpdateTime());
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					return serverAck.getServerError();
				}
			}
			// 地图红包广告审核
			if (status.byteValue() == 1 && advertisement.getAdvertisementType().getTypeNo().equals("007")) { // 地图红包广告审核通过直接发布，且不发送短信
				// 发布地图红包广告
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("version", version);

				// 获取广告创建人token
				String token = TokenUtil.getToken(Constant.DES_KEY, advertisement.getCreateUserId().getUserName());
				paramMap.put("token", URLEncoder.encode(token, "utf8"));
				paramMap.put("packetId", advertisement.getId());
				try {
					String doPost = URLConnectionUtil.doPost(serverApiUrl + "redpacket/publishMapRedPacket", paramMap);
					log.info("调用发布地图红包广告接口返回值--->doPost：", doPost);
					System.out.println("调用发布文字广告接口返回值--->doPost：" + doPost);
					if (StringUtil.isEmpty(doPost)) {
						return serverAck.getEmptyData();
					}
					JSONObject json = JSONObject.parseObject(doPost);
					if (!"200".equals(json.getString("code"))) {
						// {"code":400,"message":"版本号不能为空","success":false,"data":null,"hasNext":false}
						ResultData resultData = JSONObject.parseObject(doPost.toString(), ResultData.class);
						return resultData;
					}

					if (status.byteValue() == 1 && advertisement.getAdvertisementType().getTypeNo().equals("007")) { // 地图红包广告审核通过直接发布，且不发送短信
						advertisement.setStatus(3);
						advertisementService.advertisementStatusExamine(advertisement.getId(),
								advertisement.getStatus(), advertisement.getReason(), advertisement.getUpdateTime());
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					return serverAck.getServerError();
				}
			}
			// 发送短信内容组装
			Map<String, Object> paramMap = new HashMap<>(16);
			if (advertisement.getAdvertiser() != null) {
				if (!StringUtils.isEmpty(advertisement.getAdvertiser().getPhone())) {
					//paramMap.put("mobile", advertisement.getAdvertiser().getPhone());
				} else if (advertisement.getAdvertiser().getCreateUserId() != null
						&& !StringUtils.isEmpty(advertisement.getAdvertiser().getCreateUserId().getPhone())) {
					//paramMap.put("mobile", advertisement.getAdvertiser().getCreateUserId().getPhone());
				}
			} else {
				// 记录失败日志
				log.info("广告审核发送短信失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ advertisement.getStatus() + ";" + "审核内容-->" + advertisement.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->未找到发送短信号码;");
				return serverAck.getFailure();
			}
			paramMap.put("account", account);
			paramMap.put("pswd", pwd);
			paramMap.put("needstatus", needStatus);

			// 审核状态为1通过时，发送短信通知被审核人，话术：您的广告主资质审核已通过，请前往“车而告之”微信小程序查看
			if (1 == status.byteValue() && advertisement.getAdvertisementType() != null
					&& !StringUtil.isEmpty(advertisement.getAdvertisementType().getTypeNo())
					&& (advertisement.getAdvertisementType().getTypeNo().equals("001")
							|| advertisement.getAdvertisementType().getTypeNo().equals("007"))) { // 需求修改为文字广告审核通过直接发布，且不发送短信

				if (!StringUtils.isEmpty(advertisement.getTitle()) && !advertisement.getTitle().equals("null")) {
					paramMap.put("msg", "您的广告《" + advertisement.getTitle() + "》审核已通过，请前往“车而告之”微信小程序查看");
				} else {
					paramMap.put("msg", "您的广告《字幕广告》审核已通过，请前往“车而告之”微信小程序查看");
				}
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (advertisement.getAdvertiser() != null) {
					if (!StringUtils.isEmpty(advertisement.getAdvertiser().getPhone())) {
						log.info("{}发送短信{},結果{}", advertisement.getAdvertiser().getPhone(), "广告审核通过", retSms);
					} else if (advertisement.getAdvertiser().getCreateUserId() != null
							&& !StringUtils.isEmpty(advertisement.getAdvertiser().getCreateUserId().getPhone())) {
						log.info("{}发送短信{},結果{}", advertisement.getAdvertiser().getCreateUserId().getPhone(), "广告审核通过",
								retSms);
					}
				}
			}

			// 审核状态为2失败时，发送短信通知被审核人，记录次数，次数达到上限存入黑名单
			if (2 == status.byteValue()) {

				if (!StringUtils.isEmpty(advertisement.getTitle()) && !advertisement.getTitle().equals("null")) {
					paramMap.put("msg", "您的广告《" + advertisement.getTitle() + "》审核失败，请前往“车而告之”微信小程序查看");
				} else {
					paramMap.put("msg", "您的广告《字幕广告》审核失败，请前往“车而告之”微信小程序查看");
				}
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (advertisement.getAdvertiser() != null) {
					if (!StringUtils.isEmpty(advertisement.getAdvertiser().getPhone())) {
						log.info("{}发送短信{},結果{}", advertisement.getAdvertiser().getPhone(), "广告审核失败", retSms);
					} else if (advertisement.getAdvertiser().getCreateUserId() != null
							&& !StringUtils.isEmpty(advertisement.getAdvertiser().getCreateUserId().getPhone())) {
						log.info("{}发送短信{},結果{}", advertisement.getAdvertiser().getCreateUserId().getPhone(), "广告审核失败",
								retSms);
					}
				}

				// 查询redis中是否存在数据
				Object hget = redisUtil.hget("blackList", advertisement.getCreateUserId().getId().toString());
				if (hget != null) {
					// 存在：次数加一,不添加时间
					int count = Integer.parseInt(hget.toString());
					redisUtil.hset("blackList", advertisement.getCreateUserId().getId().toString(), count + 1);
					if (5 <= count) {
						Authority authority = new Authority();
						authority.setName(advertisement.getCreateUserId().getUserName());
						authority.setGrade(0);
						authority.setIsDeleted((byte) 0);
						authority.setCreateTime(new Date());
						authority.setCreateUserId(user);
						authorityService.save(authority);
					}
				} else {
					// 不存在：新增缓存，添加时间，时间暂定一周
					redisUtil.hset("blackList", advertisement.getCreateUserId().getId().toString(), 1,
							7 * 24 * 60 * 60);
				}
			}

			// 记录成功日志
			log.info("广告审核成功：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
					+ advertisement.getStatus() + ";" + "审核内容-->" + advertisement.getReason() + ";" + "审核时间-->"
					+ df.format(new Date()));
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 套餐列表
	 *
	 * @param version
	 * @return
	 * @author lijiaxin
	 * @date 2018年8月20日
	 */

	@RequiresPermissions(value = { "advertisement/listPrice" })
	@PostMapping("listPrice")
	public ResultData listPrice(HttpServletRequest request, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		try {
			List<Price> priceList = advertisementService.listPrice();
			if (priceList == null || priceList.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<PriceView> list = new ArrayList<>();
			for (int i = 0; i < priceList.size(); i++) {
				PriceView view = new PriceView();
				Price price = priceList.get(i);
				view.setId(price.getId());
				view.setPriceName(price.getName());
				view.setPriceNo(price.getPriceNo());
				list.add(view);
			}
			return serverAck.getSuccess().setData(list);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 发布广告
	 * 
	 * @param adverId
	 * @param carNumber
	 * @param days
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月22日
	 */

	@RequiresPermissions(value = { "advertisement/publishAdvertisement" })
	@PostMapping("publishAdvertisement")
	public ResultData publishAdvertisement(Long adverId, Integer carNumber, Integer days, HttpServletRequest request,
			String version, Integer couponNum, String clickNums, String redMoneys, String sponsorIds) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (adverId == null) {
			return serverAck.getParamError().setMessage("广告id不能为空");
		}
		if (carNumber == null) {
			return serverAck.getParamError().setMessage("车辆数不能为空");
		}
		if (days == null) {
			return serverAck.getParamError().setMessage("发布天数不能为空");
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("adverId", adverId);
			paramMap.put("carNumber", carNumber);
			paramMap.put("days", days);
			try {
				// 修改token为用户token，而不是直接用登录人token
				Optional<Advertisement> advertisementOpt = advertisementService.getAdvertisementById(adverId);
				Advertisement advertisement = advertisementOpt.get();
				Advertiser advertiser = advertisement.getAdvertiser();
				if (advertiser != null) {
					// 获取token
					String advertiserToken = TokenUtil.getToken(Constant.DES_KEY,
							advertiser.getCreateUserId().getUserName());
					paramMap.put("token", URLEncoder.encode(advertiserToken, "utf8"));
				} else {
					return serverAck.getFailure().setMessage("广告主信息不存在");
				}
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
				return serverAck.getFailure().setMessage("token编码失败");
			}
			paramMap.put("version", version);
//			paramMap.put("ticketNumber", couponNum);
			paramMap.put("clickNums", clickNums);
			paramMap.put("redMoneys", redMoneys);
			try {
				String doPost = null;
				if(StringUtils.isEmpty(sponsorIds)) {
					doPost = URLConnectionUtil.doPost(serverApiUrl + "advertisement/publishAdvertisementV4", paramMap);
				}else {
//					String[] split = sponsorIds.split(",");
//					if(split != null && split.length > 0) {
//						for (String sponsorId : split) {
//							paramMap.put("sponsorId", sponsorId);
//							doPost = URLConnectionUtil.doPost(serverApiUrl + "advertisement/publishSponsorAdvertisement", paramMap);
//						}
//					}
					paramMap.put("sponsorId", sponsorIds);
					doPost = URLConnectionUtil.doPost(serverApiUrl + "advertisement/publishSponsorAdvertisement", paramMap);
				}
				if (StringUtil.isEmpty(doPost)) {
					return serverAck.getEmptyData();
				}
				JSONObject json = JSONObject.parseObject(doPost);
				if (!"200".equals(json.getString("code"))) {
					// {"code":400,"message":"版本号不能为空","success":false,"data":null,"hasNext":false}
					ResultData resultData = JSONObject.parseObject(doPost.toString(), ResultData.class);
					return resultData;
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return serverAck.getServerError();
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 下架广告数据
	 *
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "advertisement/advertisementDown" })
	@PostMapping("advertisementDown")
	@Transactional
	public ResultData advertisementDown(HttpServletRequest request, String version, Long id) {
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
		try {
			Optional<Advertisement> opt = advertisementService.getAdvertisementById(id);
			// 处理：
			// 1：广告表状态修改为投放完成（为什么不修改成下架？因为后续需求可能重新上架当前广告，复用广告，所以下架状态放在了订单上面）
			// 2：订单表状态修改为下架
			// 3：发布表状态修改为删除，不然定时器扫到；修改结束时间为当前时间（转到api那边修改，这边不做处理）
			// 4：设备表，文字广告下架修改script_total；图片广告修改total
			// 1：广告表状态修改
			Advertisement advertisement = opt.get();
			advertisement.setStatus(4);
			advertisement.setUpdateTime(new Date());
			advertisement.setUpdateUserId(user);
			int ret = advertisementService.save(advertisement);
			if (ret != 0) {
				// 2：订单表状态修改
				Order order = orderService.getOrderByAdvertisementId(id);
				order.setStatus((byte) 5);// 5:下架状态
				order.setUpdateTime(new Date());
				order.setUpdateUserId(user);
				orderService.save(order);
				// 3：发布表状态修改，修改结束时间为当前时间（转到api那边修改，这边不做处理）
				// advertisementService.editPublishRecord(id, order.getId(), user.getId());
				// 4：设备表，文字广告下架修改script_total；图片广告修改total
				if (advertisement.getAdvertisementType() != null
						&& !StringUtil.isEmpty(advertisement.getAdvertisementType().getTypeNo())) {
					deviceService.editTotalOrScriptTotal(id, advertisement.getAdvertisementType().getTypeNo());
				} else {
					return serverAck.getFailure().setMessage("修改设备发布数失败");
				}

				// 调用api接口实时下架广告
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String token = TokenUtil.getToken(Constant.DES_KEY, user.getUserName());
				paramMap.put("token", URLEncoder.encode(token, "utf8"));
				paramMap.put("version", version);
				paramMap.put("orderId", order.getId());
				try {
					String doPost = URLConnectionUtil.doPost(serverApiUrl + "control/delAdver", paramMap);
					log.info("调用下架广告返回信息--->doPost：", doPost);
					if (StringUtil.isEmpty(doPost)) {
						return serverAck.getEmptyData();
					}
					JSONObject json = JSONObject.parseObject(doPost);
					if (!"200".equals(json.getString("code"))) {
						// {"code":400,"message":"版本号不能为空","success":false,"data":null,"hasNext":false}
						ResultData resultData = JSONObject.parseObject(doPost.toString(), ResultData.class);
						return resultData;
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					return serverAck.getServerError();
				}
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 广告预约申请记录列表
	 * @param request
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param status
	 * @return
	 * @author maqianqian
	 * @date 2019年02月15日
	 */
	@RequiresPermissions(value = { "advertisement/getSubscribeDesignlist" })
	@PostMapping("getSubscribeDesignlist")
	public ResultData getSubscribeDesignlist(HttpServletRequest request, String version, Integer curPage,
											 Integer pageSize, Byte status){

		if(checkVersion(version) != null){
			return checkVersion(version);
		}
		if(checkToken(request) != null){
			return checkToken(request);
		}
		if(curPage == null){
			curPage = 1;
		}
		if(pageSize == null){
			pageSize = 10;
		}
		if(status == null){
			return serverAck.getEmptyData().setMessage("状态为空");
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

		try {
				//	查询不同类型广告的数量
				Long count = subscribeDesignService.countSubscribeDesignByStatus(status);
				if(count == null || count < 0){
					return serverAck.getEmptyData();
				}
				//分页查询广告预约列表
				int startPage = (curPage - 1) * pageSize;

				List<SubscribeDesign> listData = subscribeDesignService.listSubscribeDesignByStatus(status, startPage, pageSize);
				if(listData == null || listData.isEmpty()){
					return serverAck.getEmptyData();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				List<SubscribeDesignView>listView = new ArrayList<>();
				for (int i = 0; i < listData.size(); i++) {
					SubscribeDesign design = listData.get(i);
					SubscribeDesignView view = new SubscribeDesignView();
					view.setId(design.getId());
					view.setCreateTime(sdf.format(design.getCreateTime()));
					view.setName(design.getName());
					//申请者电话
					view.setCreateUserPhone(design.getPhone());
					//审核者电话
						if(design.getStatus() == 0){
							view.setUpdateUserPhone("");
						}else {
							Users updateUser = design.getUpdateUserId();
							Long updateUserId = updateUser.getId();
							//获取审核者电话号码
							String updateUserPhone = subscribeDesignService.getUpDateUserPhone(updateUserId);
							view.setUpdateUserPhone(updateUserPhone);
						}

					if(design.getStatus() == 0){
							view.setUpdateTime("");
						}else {
						if (design.getUpdateTime() != null) {
							view.setUpdateTime(sdf.format(design.getUpdateTime()));
						}
					}

					if(design.getType() == 1) {
						view.setType("小平面广告");
					}
					if(design.getType() == 2){
						view.setType("大平面广告");
					}
					if(design.getType()== 3) {
						view.setType("地图广告");
					}

					view.setStatus(design.getStatus());
					listView.add(view);
				}
				Map<String, Object> map = new HashMap<>();
				map.put("totalCount",count);
				map.put("list",listView);
				return serverAck.getSuccess().setData(map);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 修改广告审核状态
	 * @param request
	 * @param version
	 * @param status
	 * @param id
	 * @return
	 */
	@RequiresPermissions(value = { "advertisement/updateSubscribeDesignStatusById" })
	@PostMapping("updateSubscribeDesignStatusById")
	@Transactional
	public ResultData updateSubscribeDesignStatusById(HttpServletRequest request, String version, Byte status, Long id){

		if(checkVersion(version) != null){
			return checkVersion(version);
		}
		if(checkToken(request) != null){
			return checkToken(request);
		}
		if(status == null){
			return serverAck.getEmptyData().setMessage("状态为空");
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("主键不能为空");
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
			//获取当前登录用户信息id
			Long updateUserId = user.getId();
			//获取修改审核状态时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updateTime = sdf.format(new Date());
			//修改广告审核状态
			subscribeDesignService.updateSubscribeDesignStatusById(id, status, updateUserId, updateTime);
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
