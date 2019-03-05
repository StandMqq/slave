package com.cegz.sys.controller.shop;

import java.math.BigDecimal;
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
import com.cegz.sys.model.shop.TicketType;
import com.cegz.sys.model.shop.UserTicket;
import com.cegz.sys.model.view.adver.UserTicketView;
import com.cegz.sys.model.view.shop.TicketTypeView;
import com.cegz.sys.model.view.shop.TicketView;
import com.cegz.sys.service.shop.BusinessService;
import com.cegz.sys.service.shop.TicketService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 优惠券后台控制类
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private BusinessService businessService;

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
	 * 获取券数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @param businessId   商户id
	 * @param ticketTypeId 劵类型id
	 * @param contacts     联系人
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/getTicketList" })
	@PostMapping("getTicketList")
	public ResultData getTicketList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Long businessId, String name, Long ticketTypeId) {
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
		if (businessId == null) {
			businessId = null;
		}
		if (ticketTypeId == null) {
			ticketTypeId = null;
		}
		try {
			List<TicketView> resultList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = ticketService.getTicketCount(name, businessId, ticketTypeId);

			// 劵数据列表
			List<Ticket> list = ticketService.getTicketList(startPage, pageSize, name, businessId, ticketTypeId);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Ticket t : list) {
				TicketView view = new TicketView();
				view.setId(t.getId());
				view.setName(t.getName());
				view.setTypeName(t.getType().getName());
				view.setTicketNo(t.getTicketNo());
				view.setTotal(t.getTotal());
				view.setStartTime(t.getStartTime());
				view.setEndTime(t.getEndTime());
				view.setCreateTime(sdf.format(t.getCreateTime()));
				view.setIsDeleted(t.getIsDeleted());
				// 通过劵id查询被领取数
				Long gainCount = ticketService.gainTicketUserDetailCount(t.getId(), null);
				view.setGainCount(Integer.valueOf(gainCount.toString()));
				// 联系人
				if (t.getCreateUser() != null && !StringUtils.isEmpty(t.getCreateUser().getUserName())) {
					view.setContacts(t.getCreateUser().getUserName());
				}
				resultList.add(view);
			}
			List<TicketType> ticketTypeList = ticketService.getTicketTypeList(0, 100, null);
			List<TicketTypeView> resultTypeList = new ArrayList<>();
			if (ticketTypeList != null && ticketTypeList.size() > 0) {
				for (TicketType tt : ticketTypeList) {
					TicketTypeView view = new TicketTypeView();
					view.setId(tt.getId());
					view.setName(tt.getName());
					view.setRemark(tt.getRemark());
					view.setTicketTypeNo(tt.getTicketTypeNo());
					view.setCreateTime(sdf.format(tt.getCreateTime()));
					resultTypeList.add(view);
				}
			}

			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", resultList);
			map.put("businessId", businessId);// 劵关联的商户id
			map.put("ticketTypeList", resultTypeList);// 劵类型列表
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 录入优惠券
	 * 
	 * @param token
	 * @param version
	 * @param name      券名称
	 * @param typeId    券类型
	 * @param ticketNo  编号
	 * @param total     总记录数
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/insertTicket" })
	@PostMapping("insertTicket")
	public ResultData insertTicket(HttpServletRequest request, String version, Long id, String name, Long typeId,
			String ticketNo, Integer total, String startTime, String endTime, String remark, BigDecimal money,
			String pictureUrl, Long businessId) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("券名称不能为空");
		}
		if (typeId == null) {
			return serverAck.getParamError().setMessage("类型不能为空");
		}
		if (total == null) {
			return serverAck.getParamError().setMessage("券数量不能为空");
		}
		if (StringUtil.isEmpty(startTime)) {
			return serverAck.getParamError().setMessage("开始时间不能为空");
		}
		if (StringUtil.isEmpty(endTime)) {
			return serverAck.getParamError().setMessage("结束时间不能为空");
		}
		ticketNo = UUID.randomUUID().toString();
		try {
			Ticket ticket = new Ticket();
			if (id != null) {
				Optional<Ticket> ticketOpt = ticketService.getTicketById(id);
				ticket = ticketOpt.get();
				ticket.setUpdateTime(new Date());
				ticket.setIsDeleted((byte) 0);
			} else {
				ticket.setId(id);
				ticket.setCreateTime(new Date());
				ticket.setIsDeleted((byte) 0);
			}

			ticket.setName(name);
			Optional<TicketType> ticketTypeOpt = ticketService.getTicketTypeById(typeId);
			if (ticketTypeOpt != null) {
				TicketType ticketType = ticketTypeOpt.get();
				ticket.setType(ticketType);
			}
			ticket.setTicketNo(ticketNo);
			ticket.setTotal(total);
			ticket.setStartTime(startTime);
			ticket.setEndTime(endTime);
			ticket.setRemark(remark);
			ticket.setMoney(money);
			ticket.setCurrentTotal(total);
			if (!StringUtils.isEmpty(pictureUrl)) {
				ticket.setPictureUrl(baseImageUrl + pictureUrl);
			}
			Optional<Business> businessOpt = businessService.getBusinessById(businessId);
			if (businessOpt != null) {
				Business business = businessOpt.get();
				ticket.setBusiness(business);
			}

			// 数据存储
			int ret = ticketService.saveTicket(ticket);
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
	 * 获取券类型数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/getTicketTypeList" })
	@PostMapping("getTicketTypeList")
	public ResultData getTicketTypeList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
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
			List<TicketTypeView> resultTypeList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = ticketService.getTicketTypeCount(name);

			// 劵类型数据列表
			List<TicketType> list = ticketService.getTicketTypeList(startPage, pageSize, name);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (TicketType tt : list) {
				TicketTypeView view = new TicketTypeView();
				view.setId(tt.getId());
				view.setName(tt.getName());
				view.setRemark(tt.getRemark());
				view.setTicketTypeNo(tt.getTicketTypeNo());
				view.setCreateTime(sdf.format(tt.getCreateTime()));
				resultTypeList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", resultTypeList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取券类型单条数据
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/getTicketTypeById" })
	@PostMapping("getTicketTypeById")
	public ResultData getTicketTypeById(HttpServletRequest request, String version, Long id) {
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
			// 劵类型数据列表
			Optional<TicketType> opt = ticketService.getTicketTypeById(id);
			TicketType ticketType = opt.get();
			TicketTypeView view = new TicketTypeView();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			view.setId(ticketType.getId());
			view.setName(ticketType.getName());
			view.setRemark(ticketType.getRemark());
			view.setTicketTypeNo(ticketType.getTicketTypeNo());
			view.setCreateTime(sdf.format(ticketType.getCreateTime()));
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 录入优惠券类型
	 * 
	 * @param token
	 * @param version
	 * @param name
	 * @param ticketTypeNo
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/insertTicketType" })
	@PostMapping("insertTicketType")
	public ResultData insertTicketType(HttpServletRequest request, String version, Long id, String name,
			String ticketTypeNo, String remark) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("券类型名称不能为空");
		}
		ticketTypeNo = UUID.randomUUID().toString();
		try {
			TicketType ticketType = new TicketType();
			ticketType.setId(id);
			ticketType.setName(name);
			ticketType.setRemark(remark);
			ticketType.setTicketTypeNo(ticketTypeNo);
			ticketType.setCreateTime(new Date());
			ticketType.setIsDeleted((byte) 0);

			// 数据存储
			int ret = ticketService.saveTicketType(ticketType);
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
	 * 下架优惠券数据
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/downTicket" })
	@PostMapping("downTicket")
	public ResultData downTicket(HttpServletRequest request, String version, Long id) {
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
			Optional<Ticket> opt = ticketService.getTicketById(id);
			// 处理
			Ticket ticket = opt.get();
			ticket.setIsDeleted((byte) 1);
			ticket.setUpdateTime(new Date());
			int ret = ticketService.saveTicket(ticket);
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
	 * 获取券单条数据
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/getTicketById" })
	@PostMapping("getTicketById")
	public ResultData getTicketById(HttpServletRequest request, String version, Long id) {
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
			Optional<Ticket> opt = ticketService.getTicketById(id);
			Ticket ticket = opt.get();
			TicketView view = new TicketView();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			view.setId(ticket.getId());
			view.setName(ticket.getName());
			view.setTypeId(ticket.getType().getId());
			view.setTicketNo(ticket.getTicketNo());
			view.setTotal(ticket.getTotal());
			view.setStartTime(ticket.getStartTime());
			view.setEndTime(ticket.getEndTime());
			view.setCreateTime(sdf.format(ticket.getCreateTime()));
			view.setPictureUrl(ticket.getPictureUrl());
			view.setBusinessId(ticket.getBusiness().getId());
			view.setRemark(ticket.getRemark());
			view.setMoney(ticket.getMoney());

//			List<TicketType> ticketTypeList = ticketService.getTicketTypeList(0, 100, null);
//			List<TicketTypeView> resultTypeList = new ArrayList<>();
//			if (ticketTypeList != null && ticketTypeList.size() > 0) {
//				for (TicketType tt : ticketTypeList) {
//					TicketTypeView typeView = new TicketTypeView();
//					typeView.setId(tt.getId());
//					typeView.setName(tt.getName());
//					typeView.setRemark(tt.getRemark());
//					typeView.setTicketTypeNo(tt.getTicketTypeNo());
//					typeView.setCreateTime(sdf.format(tt.getCreateTime()));
//					resultTypeList.add(typeView);
//				}
//			}

//			Map<String, Object> map = new HashMap<>();
//			map.put("view", view);
//			map.put("resultTypeList", resultTypeList);
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过广告id查询广告下关联的劵
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年9月11日
	 */

	@RequiresPermissions(value = { "ticket/listTicketByAdvertId" })
	@PostMapping("listTicketByAdvertId")
	public ResultData listTicketByAdvertId(HttpServletRequest request, String version, Long advertisementId) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (advertisementId == null) {
			return serverAck.getParamError().setMessage("广告不能为空");
		}
		try {
			List<Ticket> tickets = ticketService.listTicketByAdvertId(advertisementId);
			if (tickets == null || tickets.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<TicketView> list = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Ticket ticket : tickets) {
				TicketView view = new TicketView();
				view.setId(ticket.getId());
				view.setName(ticket.getName());
				view.setTypeName(ticket.getType().getName());
				view.setTicketNo(ticket.getTicketNo());
				view.setTotal(ticket.getTotal());
				view.setStartTime(ticket.getStartTime());
				view.setEndTime(ticket.getEndTime());
				view.setCreateTime(sdf.format(ticket.getCreateTime()));
				view.setRemark(ticket.getRemark());
				view.setMoney(ticket.getMoney());
				list.add(view);
			}
			return serverAck.getSuccess().setData(list);
		} catch (Exception e) {
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取券领取人详情
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */

	@RequiresPermissions(value = { "ticket/gainTicketUserDetail" })
	@PostMapping("gainTicketUserDetail")
	public ResultData gainTicketUserDetail(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Long ticketId, String phone) {
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
		if (ticketId == null) {
			return serverAck.getParamError().setMessage("劵id不能为空");
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			List<UserTicketView> userTicketList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = ticketService.gainTicketUserDetailCount(ticketId, phone);

			// 劵数据
			Optional<Ticket> ticketOpt = ticketService.getTicketById(ticketId);
			Ticket ticket = ticketOpt.get();
			// 领取人详情列表
			List<UserTicket> list = ticketService.gainTicketUserDetail(startPage, pageSize, ticketId, phone);
			if (list != null && list.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (UserTicket ut : list) {
					UserTicketView view = new UserTicketView();
					view.setId(ut.getId());
					view.setPhone(ut.getCreateUser().getUserName());
					view.setMoney(ut.getTicket().getMoney());
					view.setReceiveTime(sdf.format(ut.getCreateTime()));
					if (ut.getUpdateTime() != null) {
						view.setConsumeTime(sdf.format(ut.getUpdateTime()));
					} else {
						view.setConsumeTime("未使用");
					}
					userTicketList.add(view);
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("ticketSum", ticket.getTotal());
			map.put("receiveNum", ticket.getCurrentTotal());
			map.put("totalCount", totalCount);
			map.put("resultList", userTicketList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
