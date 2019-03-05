package com.cegz.sys.controller.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import com.cegz.sys.model.shop.Business;
import com.cegz.sys.model.shop.Ticket;
import com.cegz.sys.model.view.shop.BusinessView;
import com.cegz.sys.service.shop.BusinessService;
import com.cegz.sys.service.shop.TicketService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 商户后台控制类
 * 
 * @author tenglong
 * @date 2018年9月18日
 */
@RestController
@RequestMapping("/business")
@Slf4j
public class BusinessController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private TicketService ticketService;

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
		com.cegz.sys.model.adver.Users user = (com.cegz.sys.model.adver.Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		return null;
	}

	/**
	 * 获取商户数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */

	@RequiresPermissions(value = { "business/getBusinessList" })
	@PostMapping("getBusinessList")
	public ResultData getBusinessList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String name) {
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
			List<BusinessView> businessList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = businessService.getBusinessCount(name);
			// 商户数据列表
			List<Business> list = businessService.getBusinessList(startPage, pageSize, name);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Business b : list) {
				BusinessView view = new BusinessView();
				view.setId(b.getId());
				view.setName(b.getName());
				view.setPhone(b.getPhone());
				view.setAddress(b.getAddress());
				view.setCreateTime(sdf.format(b.getCreateTime()));
				view.setBusinessNo(b.getBusinessNo());
				view.setLng(b.getLng());
				view.setLat(b.getLat());

				// 商户关联的劵
				List<Ticket> ticketList = ticketService.getTicketListByBusinessId(b.getId());
				view.setTicketTag(String.valueOf(ticketList.size()));
				businessList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", businessList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过id获取单条商户数据
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年9月20日
	 */

	@RequiresPermissions(value = { "business/getBusinessById" })
	@PostMapping("getBusinessById")
	public ResultData getBusinessById(Long id, HttpServletRequest request, String version) {
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
			// 商户数据
			Optional<Business> businessOpt = businessService.getBusinessById(id);
			if (!businessOpt.isPresent()) {
				return serverAck.getParamError().setMessage("商户ID有误");
			}
			Business business = businessOpt.get();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BusinessView view = new BusinessView();
			view.setId(business.getId());
			view.setName(business.getName());
			view.setPhone(business.getPhone());
			view.setAddress(business.getAddress());
			view.setAddressDetail(business.getAddressDetail());
			view.setBusinessNo(business.getBusinessNo());
			view.setLng(business.getLng());
			view.setLat(business.getLat());
			view.setPictureUrls(business.getPictureUrls());
			view.setCreateTime(sdf.format(business.getCreateTime()));
			view.setIsDeleted(business.getIsDeleted());
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 录入商户
	 * 
	 * @param token
	 * @param version
	 * @param name
	 * @param ticketTypeNo
	 * @return
	 */

	@RequiresPermissions(value = { "business/insertBusiness" })
	@PostMapping("insertBusiness")
	public ResultData insertBusiness(HttpServletRequest request, String version, Long id, String name, String phone,
			String address, String addressDetail, String businessNo, String lng, String lat, String pictureUrls) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("商户名称不能为空");
		}
		if (StringUtil.isEmpty(phone)) {
			return serverAck.getParamError().setMessage("商户电话不能为空");
		}
		if (StringUtil.isEmpty(address)) {
			return serverAck.getParamError().setMessage("地址不能为空");
		}
		if (StringUtil.isEmpty(addressDetail)) {
			return serverAck.getParamError().setMessage("详细地址不能为空");
		}
//		if (StringUtil.isEmpty(businessNo)) {
//			return serverAck.getParamError().setMessage("商店编号不能为空");
//		}
		businessNo = UUID.randomUUID().toString();
		if (StringUtil.isEmpty(lng) || StringUtil.isEmpty(lat)) {
			return serverAck.getParamError().setMessage("经纬度不能为空");
		}
		if (pictureUrls == null) {
			return serverAck.getParamError().setMessage("图片不能为空");
		}

		try {
			Business business = null;
			if (id != null) {
				Optional<Business> businessOpt = businessService.getBusinessById(id);
				if (!businessOpt.isPresent()) {
					return serverAck.getParamError().setMessage("id无效");
				}
				business = businessOpt.get();
				String[] images = pictureUrls.split(",");
				String[] oldImages = null;
				if (!StringUtils.isEmpty(business.getPictureUrls())) {
					oldImages = business.getPictureUrls().split(",");
				}

				String split = "";
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < images.length; i++) {
					sb.append(split);
					if (StringUtil.isEmpty(images[i])) {
						if (oldImages != null) {
							sb.append(oldImages[i]);
						}
					} else {
						sb.append(baseImageUrl + images[i]);
					}
					split = ",";
				}
				pictureUrls = sb.toString();
				business.setUpdateTime(new Date());
			} else {
				// 获取图片地址
				String[] images = pictureUrls.split(",");
				String split = "";
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < images.length; i++) {
					sb.append(split);
					sb.append(baseImageUrl + images[i]);
					split = ",";
				}
				pictureUrls = sb.toString();
				business = new Business();
				business.setId(id);
				business.setCreateTime(new Date());
				business.setIsDeleted((byte) 0);
			}

			business.setName(name);
			business.setPhone(phone);
			business.setAddress(address);
			business.setAddressDetail(addressDetail);
			business.setBusinessNo(businessNo);
			business.setLng(lng);
			business.setLat(lat);
			business.setPictureUrls(pictureUrls);

			// 数据存储
			int ret = businessService.saveBusiness(business);
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
	 * 删除商户
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "business/deleteBusiness" })
	@PostMapping("deleteBusiness")
	public ResultData deleteBusiness(HttpServletRequest request, String version, Long id) {
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
			Optional<Business> opt = businessService.getBusinessById(id);
			// 处理
			Business business = opt.get();
			business.setIsDeleted((byte) 1);
			business.setUpdateTime(new Date());
			int ret = businessService.saveBusiness(business);
			if (ret != 0) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
