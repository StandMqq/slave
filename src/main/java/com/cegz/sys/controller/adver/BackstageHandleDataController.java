package com.cegz.sys.controller.adver;

import java.math.BigDecimal;
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
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.Advertisement;
import com.cegz.sys.model.adver.AdvertisementType;
import com.cegz.sys.model.adver.Advertiser;
import com.cegz.sys.model.adver.Agent;
import com.cegz.sys.model.adver.Authority;
import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.DrivingLicense;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.IdCard;
import com.cegz.sys.model.adver.Order;
import com.cegz.sys.model.adver.Price;
import com.cegz.sys.model.adver.Sponsor;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.shop.RedPacket;
import com.cegz.sys.model.shop.UserRedPacket;
import com.cegz.sys.model.shop.WeChat;
import com.cegz.sys.model.view.adver.AdvertisementView;
import com.cegz.sys.model.view.adver.AdvertiserView;
import com.cegz.sys.model.view.adver.AgentView;
import com.cegz.sys.model.view.adver.CarView;
import com.cegz.sys.model.view.adver.ContactView;
import com.cegz.sys.model.view.adver.DrivingRegistrationView;
import com.cegz.sys.model.view.adver.MapRedEnvelopesAdvertisementView;
import com.cegz.sys.model.view.adver.SponsorView;
import com.cegz.sys.model.view.adver.UsersView;
import com.cegz.sys.model.view.shop.UserRedPacketView;
import com.cegz.sys.service.adver.AccountService;
import com.cegz.sys.service.adver.AdvertisementService;
import com.cegz.sys.service.adver.AdvertiserService;
import com.cegz.sys.service.adver.AgentService;
import com.cegz.sys.service.adver.AuthorityService;
import com.cegz.sys.service.adver.ContactsService;
import com.cegz.sys.service.adver.DeviceService;
import com.cegz.sys.service.adver.DrivingRegistrationService;
import com.cegz.sys.service.adver.OrderService;
import com.cegz.sys.service.adver.SponsorService;
import com.cegz.sys.service.adver.UserRedPacketService;
import com.cegz.sys.service.finance.WalletService;
import com.cegz.sys.service.shop.RedPacketService;
import com.cegz.sys.service.shop.UsersService;
import com.cegz.sys.util.Md5Util;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 后台操作数据控制类
 * 
 * @author tenglong
 * @date 2018年8月15日
 */
@Slf4j
@RestController
@RequestMapping("/backstageHandleData")
public class BackstageHandleDataController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ContactsService contactsService;

	@Autowired
	private DrivingRegistrationService drivingRegistrationService;

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private AdvertiserService advertiserService;

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private RedPacketService redPacketService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserRedPacketService userRedPacketService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private DeviceService deviceService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

	/**
	 * 图片根地址
	 */
	@Value("${server.image-url}")
	private String baseImageUrl;

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
	 * 获取所有用户列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */

	@RequiresPermissions(value = { "backstageHandleData/getUsersList" })
	@PostMapping("getUsersList")
	public ResultData getUsersList(String version, Integer curPage, Integer pageSize, String name, String phone,
			HttpServletRequest request) {
		if (StringUtil.isEmpty(version)) {
			return serverAck.getParamError().setMessage("版本号不能为空");
		}
		if (serverVersion != null && !serverVersion.equals(version)) {
			return serverAck.getParamError().setMessage("版本错误");
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
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = accountService.getUsersCount(name, phone);
			// 代理商审核数据列表
			List<Users> list = accountService.getUsersList(startPage, pageSize, name, phone);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<UsersView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Users users : list) {
				UsersView view = new UsersView();
				view.setId(users.getId());
				view.setName(users.getName());
				view.setPhone(users.getPhone());
				view.setCreateTime(sdf.format(users.getCreateTime()));
				// 查询有哪些角色
				List<Authority> authoritys = authorityService.getAuthorityListByCreateId(users.getId());
				if (authoritys != null && authoritys.size() > 0) {
					for (Authority authority : authoritys) {
						if (authority != null && authority.getGrade() != null) {
							if (authority.getGrade() == 0) {
								view.setBlack(true);
							}
							if (authority.getGrade() == 3) {
								view.setAdmin(true);
							}
						}
					}
				}
				if (StringUtils.isEmpty(users.getPasswordSalt())) {
					view.setPasswordSalt(false);
				} else {
					view.setPasswordSalt(true);
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
	 * 通过用户id获取改用户的所有角色
	 * 
	 * @param token
	 * @param version
	 * @param userId  : 用户id
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */

	@RequiresPermissions(value = { "backstageHandleData/getUsersRoleByUserId" })
	@PostMapping("getUsersRoleByUserId")
	public ResultData getUsersRoleByUserId(HttpServletRequest request, String version, Long userId) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("userId不能为空");
		}
		try {
			// 获取用户信息
			Optional<Users> userTemp = accountService.getUserById(userId);
			Users users = userTemp.get();
			// 车主
			// 状态 0 未填写，1 审核中，2 审核成功，3 审核失败
			Contacts contact = users.getContact();
			int contactStatus = 0;
			int advertiserStatus = 0;
			int sponsorStatus = 0;
			int agentStatus = 0;
			if (contact != null) {
				contactStatus = contact.getStatus() + 1;
			}

			// 广告主
			Advertiser advertiser = users.getAdvertiser();
			if (advertiser != null) {
				advertiserStatus = advertiser.getStatus() + 1;
			}
			// 保荐方
			Sponsor sponsor = users.getSponsor();
			if (sponsor != null) {
				sponsorStatus = sponsor.getStatus() + 1;
			}
			// 代理商
			Agent agent = users.getAgent();
			if (agent != null) {
				agentStatus = agent.getStatus() + 1;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("contact", contactStatus);
			map.put("advertiser", advertiserStatus);
			map.put("sponsor", sponsorStatus);
			map.put("agent", agentStatus);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 账号注册
	 * 
	 * @param userName
	 * @param password
	 * @param phone
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/regist" })
	@PostMapping("regist")
	public ResultData regist(String userName, String password, String secondPsw, HttpServletRequest request,
			String version) {

		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (StringUtil.isEmpty(userName)) {
			return serverAck.getParamError().setMessage("账号不能为空");
		}
		if (!StringUtil.isMobile(userName)) {
			return serverAck.getParamError().setMessage("用户名必须为手机号");
		}
		if (StringUtil.isEmpty(password)) {
			return serverAck.getParamError().setMessage("密码不能为空");
		}
		if (password.length() < 6) {
			return serverAck.getParamError().setMessage("密码长度不能小于6位");
		}
		if (StringUtil.isEmpty(secondPsw)) {
			return serverAck.getParamError().setMessage("二次密码不能为空");
		}
		if (!password.trim().equals(secondPsw.trim())) {
			return serverAck.getParamError().setMessage("两次输入密码不一致");
		}
		try {
			// 验证账号是否存在
			userName = userName.trim();
			password = password.trim();
			Users userStatus = accountService.getUserByName(userName);
			if (userStatus != null) {
				return serverAck.getParamError().setMessage("账号已经存在");
			}
			// 数据处理
			Users user = new Users();
			user.setUserName(userName);
			String encodePassword = Md5Util.EncoderByMd5(password);
			user.setPassword(encodePassword);
			user.setPhone(userName);
			user.setCreateTime(new Date());
			// 生成uuid
			String uuid = TokenUtil.getUUID();
			user.setUuid(uuid);
			ByteSource salt = ByteSource.Util.bytes(userName);
			String passwordSalt = new SimpleHash("md5", password, salt, 2).toHex();
			user.setPasswordSalt(passwordSalt);
			// 数据存储
			int ret = accountService.regist(user);
			if (ret != 0) {
//				int retWallet = walletService.insertOrEditWallet(null, 0D, 0D, user.getId(), null, "注册创建");
//				if (retWallet == 0) {
//					return serverAck.getFailure();
//				}
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 删除账号
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/deleteUsers" })
	@PostMapping("deleteUsers")
	public ResultData deleteUsers(Long id, HttpServletRequest request, String version) {

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
			// 处理
			Optional<Users> userOpt = accountService.getUserById(id);
			Users users = userOpt.get();
			users.setIsDeleted((byte) 1);
			users.setUpdateTime(new Date());
			int ret = accountService.regist(users);
			if (ret != 0) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 设置账号为管理员
	 * 
	 * @param id
	 * @param grade 名单等级 0 黑名单，1 黄名单，2 白名单，3 管理员
	 * @return
	 * @author tenglong
	 * @date 2018年8月23日
	 */

	@RequiresPermissions(value = { "backstageHandleData/setRole" })
	@PostMapping("setRole")
	public ResultData setRole(Long id, Integer grade, HttpServletRequest request, String version) {

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
			// 处理
			Authority authority = new Authority();
			// 获取被操作人信息
			Optional<Users> opt = accountService.getUserById(id);
			Users users = opt.get();
			authority.setCreateUserId(users);
			// 操作人
			authority.setUpdateUserId(user);
			if (grade == 0) {
				authority.setName("黑名单");
			}
			if (grade == 3) {
				authority.setName("管理员");
			}
			authority.setGrade(grade);
			authority.setIsDeleted((byte) 0);
			authority.setCreateTime(new Date());
			authority.setAuthNo(TokenUtil.getUUID());
			int save = authorityService.save(authority);
			if (save != 0) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 解除账号 黑名单， 黄名单， 白名单， 管理员
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月23日
	 */

	@RequiresPermissions(value = { "backstageHandleData/relieveAuthority" })
	@PostMapping("relieveAuthority")
	public ResultData relieveAuthority(Long id, HttpServletRequest request, String version) {

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
			// 处理
			Optional<Authority> opt = authorityService.getAuthorityById(id);
			Authority authority = opt.get();
			authority.setIsDeleted((byte) 1);
			authority.setUpdateTime(new Date());
			int save = authorityService.save(authority);
			if (save != 0) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过账号获取车主信息
	 * 
	 * @param token
	 * @param version
	 * @param usersId 账号id
	 * @return
	 */

	@RequiresPermissions(value = { "backstageHandleData/getContactsByUserId" })
	@PostMapping("getContactsByUserId")
	public ResultData getContactsByUserId(HttpServletRequest request, String version, Long userId) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 车主信息
			Optional<Users> user = accountService.getUserById(userId);
			Users usersTemp = user.get();
			Contacts contact = usersTemp.getContact();
			if (contact == null || contact.getId() == null) {
				return serverAck.getEmptyData();
			}
			ContactView view = new ContactView();
			view.setId(contact.getId());
			view.setName(contact.getName());
			view.setPhone(contact.getPhone());
			view.setDriveLicenseImageUrl(contact.getDrivingLicenseId().getPictureUrl());
			String[] pictures = contact.getIdcardId().getPictureUrl().split(",");
			if (pictures.length == 2) {
				view.setFirstImageUrl(pictures[0]);
				view.setSecondImageUrl(pictures[1]);
			}
			// 审核状态 0 审核中，1 成功，2失败
			int status = 0;
			int licenseStatus = contact.getDrivingLicenseId().getStatus();
			int cardStatus = contact.getIdcardId().getStatus();
			if (licenseStatus == 1 && cardStatus == 1) {
				status = 1;
			}
			if (cardStatus == 2) {
				status = 2;
				view.setReason(contact.getIdcardId().getReason());
			}
			if (licenseStatus == 2) {
				status = 2;
				view.setReason(view.getReason() + contact.getDrivingLicenseId().getReason());
			}
			view.setStatus(status);
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 后台注册车主信息
	 * 
	 * @param firstCardFile    身份证正面
	 * @param secondCardFile   身份证反面
	 * @param driveLicenseFile 驾驶证照片
	 * @param name
	 * @param phone
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "backstageHandleData/insertContacts" })
	@PostMapping("insertContacts")
	public ResultData insertContacts(String firstCardFile, String secondCardFile, String driveLicenseFile, String name,
			String phone, HttpServletRequest request, String version, Long id, Long userId) {
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
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("联系人名称不能为空");
		}
		if (StringUtil.isEmpty(phone)) {
			return serverAck.getParamError().setMessage("联系人电话不能为空");
		}
		if (StringUtil.isEmpty(firstCardFile) || StringUtil.isEmpty(secondCardFile)) {
			return serverAck.getParamError().setMessage("身份证照片不能为空");
		}
		if (StringUtil.isEmpty(driveLicenseFile)) {
			return serverAck.getParamError().setMessage("驾驶证照片不能为空");
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 车主信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Contacts vaildContacts = user.getContact();
			if (id == null) {
				if (vaildContacts != null && vaildContacts.getId() != null) {
					return serverAck.getFailure().setMessage("车主已经存在");
				}
			} else {
				if (!id.equals(vaildContacts.getId())) {
					return serverAck.getParamError().setMessage("ID错误");
				}
			}
			String cardUrl = baseImageUrl + firstCardFile;
			String secondCardUrl = baseImageUrl + secondCardFile;
			String licenseUrl = baseImageUrl + driveLicenseFile;
			IdCard cardInfo = null;
			if (id == null) {
				cardInfo = new IdCard();
				cardInfo.setCreateTime(new Date());
			} else {
				cardInfo = vaildContacts.getIdcardId();
				cardInfo.setStatus((byte) 0);
				cardInfo.setUpdateTime(new Date());
			}

			cardInfo.setPictureUrl(cardUrl + "," + secondCardUrl);
			cardInfo.setCreateUserId(user);
			// 设置驾驶证信息
			DrivingLicense licenseInfo = null;
			if (id == null) {
				licenseInfo = new DrivingLicense();
				licenseInfo.setCreateTime(new Date());
			} else {
				licenseInfo = vaildContacts.getDrivingLicenseId();
				licenseInfo.setStatus((byte) 0);
				licenseInfo.setUpdateTime(new Date());
			}

			licenseInfo.setPictureUrl(licenseUrl);
			licenseInfo.setCreateUserId(user);
			// 设置联系人信息
			Contacts contactsInfo = null;
			if (id == null) {
				contactsInfo = new Contacts();
				contactsInfo.setCreateTime(new Date());
			} else {
				contactsInfo = vaildContacts;
				contactsInfo.setStatus((byte) 0);
				contactsInfo.setUpdateTime(new Date());
			}
			contactsInfo.setName(name);
			contactsInfo.setPhone(phone);

			contactsInfo.setCreateUserId(user);
			contactsInfo.setDrivingLicenseId(licenseInfo);
			contactsInfo.setIdcardId(cardInfo);
			int ret = contactsService.insertData(contactsInfo);
			if (ret <= 0) {
				return serverAck.getServerError();
			}
			return serverAck.getSuccess();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过用户id获取车辆列表
	 * 
	 * @param token
	 * @param version
	 * @param userId
	 * @return
	 */

	@RequiresPermissions(value = { "backstageHandleData/getCarListByUserId" })
	@PostMapping("getCarListByUserId")
	public ResultData getCarListByUserId(HttpServletRequest request, String version, Long userId) {
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
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 车主信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Contacts contact = user.getContact();
			if (contact == null || contact.getId() == null) {
				return serverAck.getEmptyData();
			}
			List<DrivingRegistration> listDr = contact.getListDrivingRegistration();
			if (listDr == null || listDr.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<CarView> resultList = new ArrayList<>();
			for (int i = 0; i < listDr.size(); i++) {
				CarView view = new CarView();
				DrivingRegistration dr = listDr.get(i);
				view.setId(dr.getId());
				view.setCarNumber(dr.getPlateNumber());
				view.setStatus(dr.getStatus());
				view.setCarType(dr.getSponsor().getType());
				resultList.add(view);
			}
			return serverAck.getSuccess().setData(resultList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取车辆信息
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/getCarById" })
	@PostMapping("getCarById")
	public ResultData getCarById(Long id, HttpServletRequest request, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("id错误");
		}
		try {
			Optional<DrivingRegistration> opt = drivingRegistrationService.getById(id);
			if (!opt.isPresent()) {
				return serverAck.getParamError().setMessage("ID错误");
			}
			DrivingRegistration dr = opt.get();
			DrivingRegistrationView view = new DrivingRegistrationView();
			view.setCompany(String.valueOf(dr.getSponsor().getId()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			view.setCarBirthday(sdf.format(dr.getCarBirthday()));
			view.setId(dr.getId());
			if (dr.getInstallTime() != null) {
				view.setInstallTime(sdf.format(dr.getInstallTime()));
			}
			view.setPictureUrl(dr.getPictureUrl());
			view.setStatus(dr.getStatus());
			view.setReason(dr.getReason());
			view.setModel(dr.getModel());
			view.setPlateNumber(dr.getPlateNumber());
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 录入车辆信息
	 * 
	 * @param partFile  行驶证图片
	 * @param carNumber 车牌号
	 * @param birthDate 车辆出厂日期
	 * @param sponsorId 保荐方ID
	 * @param carType   车辆品牌
	 * @param token     用户token
	 * @param version   版本
	 * @param userId    用户id
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/insertDrivingRegistration" })
	@PostMapping("insertDrivingRegistration")
	public ResultData insertDrivingRegistration(String partFile, String carNumber, String birthDate, Long sponsorId,
			String carType, HttpServletRequest request, String version, Long id, Long userId) {
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
		if (StringUtil.isEmpty(carNumber)) {
			return serverAck.getParamError().setMessage("车牌号不能为空");
		}
		if (StringUtil.isEmpty(carType)) {
			return serverAck.getParamError().setMessage("车辆品牌类型不能为空");
		}
		if (StringUtil.isEmpty(birthDate)) {
			return serverAck.getParamError().setMessage("出厂日期不能为空");
		}
		if (sponsorId == null) {
			return serverAck.getParamError().setMessage("保荐方id不能为空");
		}
		if (StringUtil.isEmpty(partFile)) {
			return serverAck.getParamError().setMessage("行驶证照片不能为空");
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 获取保荐方信息
			Optional<Sponsor> sponsorOpt = sponsorService.getSponSorById(sponsorId);
			if (!sponsorOpt.isPresent()) {
				return serverAck.getParamError().setMessage("保荐方ID有误");
			}
			Sponsor sponsor = sponsorOpt.get();
			// 获取车主信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Contacts contact = user.getContact();
			if (contact == null || contact.getId() == null) {
				return serverAck.getParamError().setMessage("车主不存在");
			}
			// 获取图片地址
			String pictureUrl = baseImageUrl + partFile;
			// 设置行驶证信息
			DrivingRegistration dr = null;
			if (id == null) {
				dr = new DrivingRegistration();
				dr.setCreateTime(new Date());
			} else {
				Optional<DrivingRegistration> opt = contactsService.getRegistrationById(id);
				if (!opt.isPresent()) {
					return serverAck.getParamError().setMessage("ID错误");
				}
				dr = opt.get();
				dr.setStatus((byte) 0);
				dr.setUpdateTime(new Date());
			}
			if (carNumber.indexOf(" ") != -1) {
				return serverAck.getParamError().setMessage("车牌输入错误");
			}
			// 校验车牌是否唯一
			if (id == null) {// 新增校验
				DrivingRegistration drTemp = drivingRegistrationService
						.getDrivingRegistrationByPlateNumber(carNumber.toUpperCase());
				if (drTemp != null) {
					return serverAck.getFailure().setMessage("车牌已存在");
				}
			}
			dr.setPlateNumber(carNumber.toUpperCase());
			dr.setModel(carType);
			dr.setPictureUrl(pictureUrl);
			dr.setCreateUserId(user);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date birthday = sdf.parse(birthDate);
			dr.setCarBirthday(birthday);
			dr.setSponsor(sponsor);
			dr.setContact(contact);
			// 数据处理
			int ret = contactsService.insertContractDriveRegist(dr);
			if (ret == 0) {
				return serverAck.getFailure();
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 保荐方信息录入
	 * 
	 * @param name
	 * @param phone
	 * @param email
	 * @param address
	 * @param addressDetail
	 * @param company         公司名称
	 * @param province        省份名
	 * @param businessLicense 营业执照编号
	 * @param businessFile    营业执照图片
	 * @param token
	 * @param version
	 * @param type            类型
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/insertSponsor" })
	@PostMapping("insertSponsor")
	public ResultData insertSponsor(String businessFile, String name, String phone, String email, String address,
			String addressDetail, String province, String company, String companyLittle, String businessLicense,
			HttpServletRequest request, String type, String version, Long id, Long userId) {
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
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("姓名不能为空");
		}
		if (StringUtil.isEmpty(phone)) {
			return serverAck.getParamError().setMessage("手机号不能为空");
		}
		if (!StringUtil.isMobile(phone)) {
			return serverAck.getParamError().setMessage("手机号格式有误");
		}
		if (StringUtil.isEmpty(address)) {
			return serverAck.getParamError().setMessage("地址不能为空");
		}
		if (StringUtil.isEmpty(addressDetail)) {
			return serverAck.getParamError().setMessage("详细地址不能为空");
		}
		if (StringUtil.isEmpty(province)) {
			return serverAck.getParamError().setMessage("省份名不能为空");
		}
		if (StringUtil.isEmpty(company)) {
			return serverAck.getParamError().setMessage("公司名称不能为空");
		}
		if (StringUtil.isEmpty(companyLittle)) {
			return serverAck.getParamError().setMessage("公司名称简称不能为空");
		}
		if (StringUtil.isEmpty(businessLicense)) {
			return serverAck.getParamError().setMessage("营业执照编号不能为空");
		}
		if (StringUtil.isEmpty(type)) {
			return serverAck.getParamError().setMessage("类型不能为空");
		}
		if (!"12".contains(type)) {
			return serverAck.getParamError().setMessage("类型有误");
		}
		if (StringUtil.isEmpty(businessFile)) {
			return serverAck.getParamError().setMessage("营业执照图片不能为空");
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 校验保荐方公司名称是否存在
			boolean checkCompanyNameExist = sponsorService.checkCompanyNameExist(company);
			if (checkCompanyNameExist) {
				return serverAck.getParamError().setMessage("保荐方公司名称已经存在");
			}
			// 校验保荐方公司名称简称是否存在
			boolean checkCompanyLittleNameExist = sponsorService.checkCompanyLittleNameExist(companyLittle);
			if (checkCompanyLittleNameExist) {
				return serverAck.getParamError().setMessage("保荐方公司名称简称已经存在");
			}
			// 获取用户信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Sponsor vaildSponsor = user.getSponsor();
			if (id == null) {
				if (vaildSponsor != null && vaildSponsor.getId() != null) {
					return serverAck.getParamError().setMessage("保荐方已经存在");
				}
			} else {
				if (!id.equals(vaildSponsor.getId())) {
					return serverAck.getParamError().setMessage("ID错误");
				}
			}
			// 图片保存
			String imageUrl = baseImageUrl + businessFile;
			// 设置广告方信息
			Sponsor sponsor = null;
			if (id != null) {
				sponsor = vaildSponsor;
				sponsor.setStatus((byte) 0);
				sponsor.setUpdateTime(new Date());
			} else {
				sponsor = new Sponsor();
				sponsor.setCreateTime(new Date());
				sponsor.setCreateUserId(user);
			}
			sponsor.setAddress(address);
			sponsor.setAddressDetail(addressDetail);
			sponsor.setName(name);
			sponsor.setPhone(phone);
			sponsor.setProvince(province);
			sponsor.setCompany(company);
			sponsor.setCompanyLittle(companyLittle);
			sponsor.setBusinessLicense(businessLicense);
			sponsor.setEmail(email);
			sponsor.setPictureUrl(imageUrl);
			sponsor.setType(Integer.parseInt(type));
			// 处理
			int ret = sponsorService.save(sponsor);
			if (ret == 0) {
				return serverAck.getFailure();
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取保荐方信息
	 * 
	 * @param token
	 * @param version
	 * @param userId
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 * 
	 */

	@RequiresPermissions(value = { "backstageHandleData/getSponsorByUserId" })
	@PostMapping("getSponsorByUserId")
	public ResultData getSponsorByUserId(HttpServletRequest request, String version, Long userId) {
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
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 获取用户信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Sponsor sponsor = user.getSponsor();
			if (sponsor == null) {
				return serverAck.getEmptyData();
			}
			SponsorView view = new SponsorView();
			view.setAddress(sponsor.getAddress());
			view.setAddressDetail(sponsor.getAddressDetail());
			view.setBusinessNumber(sponsor.getBusinessLicense());
			view.setCompany(sponsor.getCompany());
			view.setCompanyLittle(sponsor.getCompanyLittle());
			view.setEmail(sponsor.getEmail());
			view.setId(sponsor.getId());
			view.setLicenseImgUrl(sponsor.getPictureUrl());
			view.setName(sponsor.getName());
			view.setAddress(sponsor.getAddress());
			view.setProvince(sponsor.getProvince());
			view.setStatus(sponsor.getStatus());
			view.setReason(sponsor.getReason());
			view.setType(sponsor.getType());
			view.setPhone(sponsor.getPhone());
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取所有保荐方
	 * 
	 * @param token
	 * @param type
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/getSponsorList" })
	@PostMapping("getSponsorList")
	public ResultData getSponsorList(HttpServletRequest request, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		try {
			List<Sponsor> list = sponsorService.getSponsorList();
			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<SponsorView> resultList = new ArrayList<>();
			int length = list.size();
			for (int i = 0; i < length; i++) {
				Sponsor sponsor = list.get(i);
				SponsorView view = new SponsorView();
				view.setAddress(sponsor.getAddress());
				view.setAddressDetail(sponsor.getAddressDetail());
				view.setBusinessNumber(sponsor.getBusinessLicense());
				view.setCompany(sponsor.getCompany());
				view.setEmail(sponsor.getEmail());
				view.setId(sponsor.getId());
				view.setLicenseImgUrl(sponsor.getPictureUrl());
				view.setName(sponsor.getName());
				view.setPhone(sponsor.getPhone());
				view.setProvince(sponsor.getProvince());
				view.setStatus(sponsor.getStatus());
				view.setReason(sponsor.getReason());
				view.setType(sponsor.getType());
				resultList.add(view);
			}
			return serverAck.getSuccess().setData(resultList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 录入代理商
	 * 
	 * @param businessFile
	 * @param name
	 * @param phone
	 * @param email
	 * @param address
	 * @param addressDetail
	 * @param city
	 * @param company
	 * @param businessLicense
	 * @param token
	 * @param version
	 * @param userId
	 * @return
	 * @author Administrator
	 * @date 2018年8月1日
	 */

	@RequiresPermissions(value = { "backstageHandleData/insertAgent" })
	@PostMapping("insertAgent")
	public ResultData insertAgent(String businessFile, String name, String phone, String email, String address,
			String addressDetail, String city, String company, String businessLicense, HttpServletRequest request,
			String version, Long id, Long userId) {
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
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("姓名不能为空");
		}
		if (StringUtil.isEmpty(phone)) {
			return serverAck.getParamError().setMessage("手机号不能为空");
		}
		if (!StringUtil.isMobile(phone)) {
			return serverAck.getParamError().setMessage("手机号格式有误");
		}
		if (StringUtil.isEmpty(address)) {
			return serverAck.getParamError().setMessage("地址不能为空");
		}
		if (StringUtil.isEmpty(addressDetail)) {
			return serverAck.getParamError().setMessage("详细地址不能为空");
		}
		if (StringUtil.isEmpty(city)) {
			return serverAck.getParamError().setMessage("城市不能为空");
		}
		if (StringUtil.isEmpty(company)) {
			return serverAck.getParamError().setMessage("公司名称不能为空");
		}
		if (StringUtil.isEmpty(businessLicense)) {
			return serverAck.getParamError().setMessage("营业执照编号不能为空");
		}
		if (StringUtil.isEmpty(businessFile)) {
			return serverAck.getParamError().setMessage("营业执照图片不能为空");
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 获取用户信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Agent vaildAgent = user.getAgent();
			if (id == null) {
				if (vaildAgent != null && vaildAgent.getId() != null) {
					return serverAck.getParamError().setMessage("代理商已经存在");
				}
			} else {
				if (!id.equals(vaildAgent.getId())) {
					return serverAck.getParamError().setMessage("ID错误");
				}
			}
			// 图片保存
			String imageUrl = baseImageUrl + businessFile;
			// 设置代理商信息
			Agent agent = null;
			if (id == null) {
				agent = new Agent();
				agent.setCreateTime(new Date());
			} else {
				agent = vaildAgent;
				agent.setStatus((byte) 0);
				agent.setUpdateTime(new Date());
			}
			agent.setAddress(address);
			agent.setAddressDetail(addressDetail);
			agent.setName(name);
			agent.setPhone(phone);
			agent.setCity(city);
			agent.setCompany(company);
			agent.setBusiness(businessLicense);
			agent.setEmail(email);
			agent.setCreateUserId(user);
			agent.setPictureUrl(imageUrl);
			// 处理
			Agent retAgent = agentService.insert(agent);
			if (retAgent != null && retAgent.getId() != null) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取代理商信息
	 * 
	 * @param token
	 * @param version
	 * @param userId
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 * 
	 */

	@RequiresPermissions(value = { "backstageHandleData/getAgentByUserId" })
	@PostMapping("getAgentByUserId")
	public ResultData getAgentByUserId(HttpServletRequest request, String version, Long userId) {
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
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 获取用户信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Agent agent = user.getAgent();
			if (agent == null) {
				return serverAck.getEmptyData();
			}
			AgentView view = new AgentView();
			view.setId(agent.getId());
			view.setCity(agent.getCity());
			view.setCompany(agent.getCompany());
			view.setBusinessNumber(agent.getBusiness());
			view.setLicenseImgUrl(agent.getPictureUrl());
			view.setName(agent.getName());
			view.setPhone(agent.getPhone());
			view.setEmail(agent.getEmail());
			view.setAddress(agent.getAddress());
			view.setAddressDetail(agent.getAddressDetail());
			view.setStatus(agent.getStatus());
			view.setReason(agent.getReason());
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取广告方信息
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 * 
	 */

	@RequiresPermissions(value = { "backstageHandleData/getAdvertiserByUserId" })
	@PostMapping("getAdvertiserByUserId")
	public ResultData getAdvertiserByUserId(HttpServletRequest request, String version, Long userId) {
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
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 获取用户信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Advertiser advertiser = user.getAdvertiser();
			if (advertiser == null) {
				return serverAck.getEmptyData();
			}
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
			view.setReason(advertiser.getReason());
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 广告方信息录入
	 * 
	 * @param name
	 * @param phone
	 * @param email
	 * @param address
	 * @param addressDetail
	 * @param city
	 * @param company
	 * @param businessLicense
	 * @param businessFile
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/insertAdvertiser" })
	@PostMapping("insertAdvertiser")
	public ResultData insertAdvertiser(String businessFile, String name, String phone, String email, String address,
			String addressDetail, String city, String company, String businessLicense, HttpServletRequest request,
			String version, Long id, Long userId) {
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
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("姓名不能为空");
		}
		if (StringUtil.isEmpty(phone)) {
			return serverAck.getParamError().setMessage("手机号不能为空");
		}
		if (!StringUtil.isMobile(phone)) {
			return serverAck.getParamError().setMessage("手机号格式有误");
		}
		if (StringUtil.isEmpty(address)) {
			return serverAck.getParamError().setMessage("地址不能为空");
		}
		if (StringUtil.isEmpty(addressDetail)) {
			return serverAck.getParamError().setMessage("详细地址不能为空");
		}
		if (StringUtil.isEmpty(city)) {
			return serverAck.getParamError().setMessage("城市名不能为空");
		}
		if (StringUtil.isEmpty(company)) {
			return serverAck.getParamError().setMessage("公司名称不能为空");
		}
		if (StringUtil.isEmpty(businessLicense)) {
			return serverAck.getParamError().setMessage("营业执照编号不能为空");
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 校验保荐方公司名称是否存在
			boolean checkCompanyNameExist = advertiserService.checkCompanyNameExist(company);
			if (checkCompanyNameExist) {
				return serverAck.getParamError().setMessage("广告主公司名称已经存在");
			}
			// 获取用户信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Advertiser vaildAdvertiser = user.getAdvertiser();
			if (id == null) {
				if (vaildAdvertiser != null && vaildAdvertiser.getId() != null) {
					return serverAck.getParamError().setMessage("广告主已经存在");
				}
			} else {
				if (!id.equals(vaildAdvertiser.getId())) {
					return serverAck.getParamError().setMessage("ID错误");
				}
			}
			// 图片保存
			String imageUrl = baseImageUrl + businessFile;
			// 设置广告方信息
			Advertiser advertiser = null;
			if (id == null) {
				advertiser = new Advertiser();
				advertiser.setCreateTime(new Date());
			} else {
				advertiser = vaildAdvertiser;
				advertiser.setStatus((byte) 0);
				advertiser.setUpdateTime(new Date());
			}
			advertiser.setAddress(address);
			advertiser.setAddressDetail(addressDetail);
			advertiser.setName(name);
			advertiser.setPhone(phone);
			advertiser.setCity(city);
			advertiser.setCompany(company);
			advertiser.setBusinessLicense(businessLicense);
			advertiser.setEmail(email);
			advertiser.setCreateUserId(user);
			advertiser.setPictureUrl(imageUrl);
			// 处理
			int ret = advertiserService.save(advertiser);
			if (ret == 0) {
				return serverAck.getFailure();
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取广告信息
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageHandleData/getAdvertisementById" })
	@PostMapping("getAdvertisementById")
	public ResultData getAdvertisementById(Long id, HttpServletRequest request, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("id错误");
		}
		try {
			Optional<Advertisement> opt = advertisementService.getAdvertisementById(id);
			if (!opt.isPresent()) {
				return serverAck.getParamError().setMessage("ID错误");
			}
			Advertisement a = opt.get();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			AdvertisementView view = new AdvertisementView();
			view.setId(id);
			view.setReason(a.getReason());
			view.setStatus(a.getStatus());
			view.setTitle(a.getTitle());
			view.setTitleImgUrl(a.getTitlePicUrl());
			view.setAdvertisementType(a.getAdvertisementType().getTypeNo());
			view.setAdvertisementTypeName(a.getAdvertisementType().getName());
			view.setAdvertiserName(a.getAdvertiser().getName());
			view.setAdvertiserPhone(a.getAdvertiser().getPhone());
			view.setContentImages(a.getContentPicUrl());
			view.setCreateTime(sdf.format(a.getCreateTime()));
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 录入广告信息
	 * 
	 * @param title               标题
	 * @param titlePicUrl         标题图片
	 * @param contentPicUrl       内容图片
	 * @param content             内容
	 * @param advertisementTypeId 广告类型
	 * @param token               用户token
	 * @param version             版本
	 * @param userId              用户id
	 * @return
	 * @author tenglong
	 * @date 2018年8月21日
	 */

	@RequiresPermissions(value = { "backstageHandleData/insertAdvertisement" })
	@PostMapping("insertAdvertisement")
	public ResultData insertAdvertisement(String title, String titlePicUrl, String contentPicUrl, String content,
			Long advertisementTypeId, HttpServletRequest request, String version, Long id, Long userId, Long priceId,
			String iconUrl) {
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
		if (advertisementTypeId == null) {
			return serverAck.getParamError().setMessage("广告类型不能为空");
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		if (priceId == null) {
			return serverAck.getParamError().setMessage("套餐id不能为空");
		}
		try {
			// 获取广告主信息
			// 获取用户信息
			Optional<Users> userOpt = accountService.getUserById(userId);
			user = userOpt.get();
			Advertiser advertiser = user.getAdvertiser();
			if (advertiser == null) {
				return serverAck.getParamError().setMessage("未找到广告主");
			}
			// 获取图片地址
			long typeId = advertisementTypeId;
			if (typeId == 2) {
				String[] images = contentPicUrl.split(",");
				String split = "";
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < images.length; i++) {
					sb.append(split);
					sb.append(baseImageUrl + images[i]);
					split = ",";
				}
				contentPicUrl = sb.toString();
			}else {
				titlePicUrl = baseImageUrl + titlePicUrl;
				if (!StringUtil.isEmpty(iconUrl)) {
					iconUrl = baseImageUrl + iconUrl;
				}
				String[] images = contentPicUrl.split(",");
				String split = "";
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < images.length; i++) {
					sb.append(split);
					sb.append(baseImageUrl + images[i]);
					split = ",";
				}
				contentPicUrl = sb.toString();
			}
			Optional<AdvertisementType> optType = advertisementService.getAdverTypeById(advertisementTypeId);
			if (!optType.isPresent()) {
				return serverAck.getParamError().setMessage("类型错误");
			}
			Optional<Price> optPrice = advertisementService.getPriceById(priceId);
			if (!optPrice.isPresent()) {
				return serverAck.getParamError().setMessage("套餐错误");
			}
			// 设置广告信息
			Advertisement advertisement = null;
			if (id == null) {
				advertisement = new Advertisement();
				advertisement.setCreateTime(new Date());
			} else {
				Optional<Advertisement> opt = advertisementService.getAdvertisementById(id);
				if (!opt.isPresent()) {
					return serverAck.getParamError().setMessage("ID错误");
				}
				advertisement = opt.get();
				advertisement.setStatus((byte) 0);
				advertisement.setUpdateTime(new Date());
			}
			advertisement.setTitle(title);
			advertisement.setTitlePicUrl(titlePicUrl);
			advertisement.setIconUrl(iconUrl);
			advertisement.setContentPicUrl(contentPicUrl);
			advertisement.setContent(content);
			advertisement.setPrice(optPrice.get());
			advertisement.setAdvertisementType(optType.get());
			advertisement.setCreateUserId(user);
			advertisement.setAdvertiser(advertiser);
			// 数据处理
			int ret = advertisementService.save(advertisement);
			if (ret == 0) {
				return serverAck.getFailure();
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取地图红包数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return yucheng
	 * @data 2018年12月13号
	 */

	@RequiresPermissions(value = { "backstageHandleData/getMapRedEnvelopesAdvertisementList" })
	@PostMapping("getMapRedEnvelopesAdvertisementList")
	public ResultData getMapRedEnvelopesAdvertisementList(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, String name) {
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
		try {
			List<MapRedEnvelopesAdvertisementView> resultList = new ArrayList<>();

			Long totalCount = redPacketService.getRedPacketCount(5, name);
			if (totalCount == null || totalCount == 0) {
				return serverAck.getEmptyData();
			}
			// 商户数据列表
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			List<RedPacket> list = redPacketService.getRedPacketList(startPage, pageSize, 5, name);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (RedPacket redPacket : list) {
				MapRedEnvelopesAdvertisementView view = new MapRedEnvelopesAdvertisementView();
				view.setId(redPacket.getId());
				// 红包总数
				double total = redPacket.getTotal().doubleValue();
				// 红包单个金额
				double oneRedPacketMoney = redPacket.getMoney().doubleValue();
				// 当前红包剩余的数量
				double currentTotal = redPacket.getCurrentTotal().doubleValue();
				// 红包领取数量
				double contactsNum = (total - currentTotal);
				// 获取红包金额
				Double money = (oneRedPacketMoney * total);
				BigDecimal b = new BigDecimal(money);
				// 小数保留2位
				money = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				view.setMoney(money.toString());
				if (redPacket.getCurrentTotal() != null) {
					view.setCurrentTotal(redPacket.getCurrentTotal().toString());
				}
				view.setTotal(redPacket.getTotal().toString());
				// 获取乘客领取总金额
				double contactsTotalMoney = (contactsNum * oneRedPacketMoney);
				b = new BigDecimal(contactsTotalMoney);
				// 小数保留2位
				contactsTotalMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				view.setContactsMoney(String.valueOf(contactsTotalMoney));
				// 获取剩余金额
				double balance = (currentTotal * oneRedPacketMoney);
				b = new BigDecimal(balance);
				// 小数保留2位
				balance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				view.setBalance(String.valueOf(balance));
				// 获取商家名称
				Optional<Advertisement> advertisement = advertisementService
						.getAdvertisementById(redPacket.getAdverId());
				if (advertisement != null) {
					view.setName(advertisement.get().getTitle());
					Order order = orderService.getOrderByAdvertisementId(advertisement.get().getId());
					if (order != null) {
						if (order.getStartTime() != null) {
							view.setPublishTime(sdf.format(order.getStartTime()));
						} else {
							view.setPublishTime(" ");
						}
					}
				}
				resultList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 地图红包领取详情
	 * 
	 * @param request
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @param phone
	 * @return yucheng
	 * @data 2018年12月14号
	 */
	@RequiresPermissions(value = { "backstageHandleData/mapRedEnvelopesAdvertisementDetail" })
	@PostMapping("mapRedEnvelopesAdvertisementDetail")
	public ResultData getmapRedEnvelopesAdvertisementDetail(HttpServletRequest request, String version, Integer index,
			Integer size, Long id, String name, String phone) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (index == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		if (size == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			Long totalCount = userRedPacketService.getUserRedPacketCountByRedPcaketId(id);
			if (totalCount == 0 || totalCount == null) {
				return serverAck.getEmptyData();
			}
			index = (index - 1) * size;// 计算起始页
			List<UserRedPacketView> resultList = new ArrayList<>();
			List<UserRedPacket> list = userRedPacketService.getUserRedPacketListByRedPcaketId(index, size, id);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (UserRedPacket u : list) {
				UserRedPacketView view = new UserRedPacketView();
				view.setId(u.getId());
				view.setCreateTime(sdf.format(u.getCreateTime()));
				if (u.getRedPacket() != null) {
					view.setMoney(u.getRedPacket().getMoney().doubleValue());
				}
				com.cegz.sys.model.shop.Users createUser = u.getCreateUser();
				if (createUser != null) {
					WeChat weChat = usersService.getWeChatByUsersId(createUser.getId());
					if (weChat != null) {
						view.setWxName(weChat.getName());// 微信名
					}
					DrivingRegistration dr = drivingRegistrationService.queryPlateNumberByDeviceImei(u.getImei());
					if (dr != null) {
						view.setPlateNumber(dr.getPlateNumber());
					}
				}
				resultList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("result", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

}
