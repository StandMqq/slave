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
import com.cegz.sys.model.adver.Agent;
import com.cegz.sys.model.adver.DataDictionary;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.AgentView;
import com.cegz.sys.service.adver.AgentService;
import com.cegz.sys.service.adver.DataDictionaryService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 代理商后台控制类
 * 
 * @author tenglong
 * @date 2018年8月1日
 */
@Slf4j
@RestController
@RequestMapping("/agent")
public class AgentController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AgentService agentService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

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
	 * 获取代理商待审核列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */

	@RequiresPermissions(value = { "agent/getExamineList" })
	@PostMapping("getExamineList")
	public ResultData getExamineList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Integer status, String name, String phone) {
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
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = agentService.queryAgentExamineTotalCount(status, name, phone);
			// 代理商审核数据列表
			List<Agent> list = agentService.listAgentExamine(startPage, pageSize, status, name, phone);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<AgentView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Agent agent : list) {
				AgentView view = new AgentView();
				view.setId(agent.getId());
				view.setCity(agent.getCity());
				view.setCompany(agent.getCompany());
				view.setBusinessNumber(agent.getBusiness());
				view.setName(agent.getName());
				view.setPhone(agent.getPhone());
				view.setEmail(agent.getEmail());
				view.setAddress(agent.getAddress());
				view.setAddressDetail(agent.getAddressDetail());
				view.setStatus(agent.getStatus());
				view.setCreateTime(sdf.format(agent.getCreateTime()));
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
	 * 通过id获取单条代理商审核数据
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月30日
	 */

	@RequiresPermissions(value = { "agent/getExamineById" })
	@PostMapping("getExamineById")
	public ResultData getExamineById(Long id, HttpServletRequest request, String version) {
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
			// 代理商单条审核数据
			Optional<Agent> agentOpt = agentService.getAgentById(id);
			if (!agentOpt.isPresent()) {
				return serverAck.getParamError().setMessage("代理商ID有误");
			}
			Agent agent = agentOpt.get();
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
	 * 状态审核
	 * 
	 * @param id
	 * @param token
	 * @param reason
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */

	@RequiresPermissions(value = { "agent/statusExamine" })
	@PostMapping("statusExamine")
	public ResultData statusExamine(Long id, Integer status, String reason, HttpServletRequest request, String version,
			String city, String company, String businessNumber, String name, String phone, String email, String address,
			String addressDetail) {

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
				return serverAck.getParamError().setMessage("营业执照号不能为空");
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
			// 获取代理商信息
			Optional<Agent> agentOpt = agentService.getAgentById(id);
			if (!agentOpt.isPresent()) {
				return serverAck.getParamError().setMessage("代理商ID有误");
			}
			Agent agent = agentOpt.get();
			agent.setStatus(status.byteValue());
			agent.setReason(status.byteValue() == 1 ? null : reason);// 如果状态为1，原因置为null
			agent.setUpdateTime(new Date());

			int ret = agentService.statusExamine(agent.getId(), agent.getStatus(), agent.getReason(),
					agent.getUpdateTime(), city, company, businessNumber, name, phone, email, address, addressDetail);

//			agent = agentService.insert(agent);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			if (agent == null || agent.getId() == null) {
			if (ret == 0) {
				// 记录失败日志
				log.info("代理商审核失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ agent.getStatus() + ";" + "审核内容-->" + agent.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}
			// 记录成功日志
			log.info("代理商审核成功：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->" + agent.getStatus()
					+ ";" + "审核内容-->" + agent.getReason() + ";" + "审核时间-->" + df.format(new Date()));
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
