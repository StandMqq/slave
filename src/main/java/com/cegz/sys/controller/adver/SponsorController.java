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
import com.cegz.sys.model.adver.DataDictionary;
import com.cegz.sys.model.adver.Sponsor;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.SponsorView;
import com.cegz.sys.service.adver.DataDictionaryService;
import com.cegz.sys.service.adver.SponsorService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 保荐方后台控制类
 * 
 * @author tenglong
 * @date 2018年7月30日
 */
@Slf4j
@RestController
@RequestMapping("/sponsor")
public class SponsorController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private SponsorService sponsorService;

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
	 * 获取保荐方待审核列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月30日
	 */

	@RequiresPermissions(value = { "sponsor/getExamineList" })
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
			Long totalCount = sponsorService.querySponsorExamineTotalCount(status, name, phone);
			// 保荐方审核数据列表
			List<Sponsor> list = sponsorService.listSponsorExamine(startPage, pageSize, status, name, phone);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<SponsorView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Sponsor sponsor : list) {
				SponsorView view = new SponsorView();
				view.setId(sponsor.getId());
				view.setProvince(sponsor.getProvince());
				view.setCompany(sponsor.getCompany());
				view.setCompanyLittle(sponsor.getCompanyLittle());
				view.setBusinessNumber(sponsor.getBusinessLicense());
				view.setName(sponsor.getName());
				view.setPhone(sponsor.getPhone());
				view.setEmail(sponsor.getEmail());
				view.setAddress(sponsor.getAddress());
				view.setAddressDetail(sponsor.getAddressDetail());
				view.setStatus(sponsor.getStatus());
				view.setCreateTime(sdf.format(sponsor.getCreateTime()));
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
	 * 通过id获取单条保荐方审核数据
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月30日
	 */

	@RequiresPermissions(value = { "sponsor/getExamineById" })
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
			// 保荐方单条审核数据
			Optional<Sponsor> sponsorOpt = sponsorService.getSponSorById(id);
			if (!sponsorOpt.isPresent()) {
				return serverAck.getParamError().setMessage("保荐方ID有误");
			}
			Sponsor sponsor = sponsorOpt.get();
			SponsorView view = new SponsorView();
			view.setId(sponsor.getId());
			view.setAddress(sponsor.getAddress());
			view.setAddressDetail(sponsor.getAddressDetail());
			view.setBusinessNumber(sponsor.getBusinessLicense());
			view.setCompany(sponsor.getCompany());
			view.setCompanyLittle(sponsor.getCompanyLittle());
			view.setName(sponsor.getName());
			view.setPhone(sponsor.getPhone());
			view.setLicenseImgUrl(sponsor.getPictureUrl());
			view.setProvince(sponsor.getProvince());
			view.setStatus(sponsor.getStatus());
			view.setType(sponsor.getType());
			view.setEmail(sponsor.getEmail());
			view.setReason(sponsor.getReason());
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
	 * @date 2018年7月30日
	 */

	@RequiresPermissions(value = { "sponsor/statusExamine" })
	@PostMapping("statusExamine")
	public ResultData statusExamine(Long id, Integer status, String reason, HttpServletRequest request, String version,
			String city, String company, String companyLittle, String businessNumber, String name, String phone,
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
		if (status != null && 1 == status) {
			if (StringUtils.isEmpty(city)) {
				return serverAck.getParamError().setMessage("城市不能为空");
			}
			if (StringUtils.isEmpty(company)) {
				return serverAck.getParamError().setMessage("公司名称不能为空");
			}
			if (StringUtils.isEmpty(companyLittle)) {
				return serverAck.getParamError().setMessage("公司名简称不能为空");
			}
			if (StringUtils.isEmpty(businessNumber)) {
				return serverAck.getParamError().setMessage("营业执照号不能为空");
			}
			if (StringUtils.isEmpty(name)) {
				return serverAck.getParamError().setMessage("联系人不能为空");
			}
			if (StringUtils.isEmpty(phone)) {
				return serverAck.getParamError().setMessage("手机号不能为空");
			}
		}
		try {
			// 获取保荐方信息
			Optional<Sponsor> sponsorOpt = sponsorService.getSponSorById(id);
			if (!sponsorOpt.isPresent()) {
				return serverAck.getParamError().setMessage("保荐方ID有误");
			}
			Sponsor sponsor = sponsorOpt.get();
			sponsor.setStatus(status.byteValue());
			sponsor.setReason(status.byteValue() == 1 ? null : reason);// 如果状态为1，原因置为null
			sponsor.setUpdateTime(new Date());

			int ret = sponsorService.statusExamine(sponsor.getId(), sponsor.getStatus(), sponsor.getReason(),
					sponsor.getUpdateTime(), city, company, companyLittle, businessNumber, name, phone, email, address,
					addressDetail);

//			int ret = sponsorService.save(sponsor);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (ret == 0) {
				// 记录失败日志
				log.info("保荐方审核失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ sponsor.getStatus() + ";" + "审核内容-->" + sponsor.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}
			// 记录成功日志
			log.info("保荐方审核成功：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->" + sponsor.getStatus()
					+ ";" + "审核内容-->" + sponsor.getReason() + ";" + "审核时间-->" + df.format(new Date()));
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
