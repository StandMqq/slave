package com.cegz.sys.controller.finance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.model.adver.Invoice;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.InvoiceView;
import com.cegz.sys.service.adver.CheckMoneyService;
import com.cegz.sys.service.finance.InvoiceService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 发票控制类
 * 
 * @author tenglong
 * @date 2018年11月20日
 */
@Slf4j
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CheckMoneyService checkMoneyService;

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
	 * 获取发票信息列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年11月20日
	 */

	@RequiresPermissions(value = { "invoice/getInvoiceList" })
	@PostMapping("getInvoiceList")
	public ResultData getInvoiceList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String startTime, String endTime, String company) {
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
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		if (StringUtils.isEmpty(company)) {
			company = null;
		}

		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = invoiceService.queryInvoiceTotalCount(startTime, endTime, company);
			// 发票数据列表
			List<Invoice> list = invoiceService.queryInvoiceList(startPage, pageSize, startTime, endTime, company);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<InvoiceView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Invoice invoice : list) {
				InvoiceView view = new InvoiceView();
				view.setId(invoice.getId());
				if (invoice.getCreateTime() != null) {
					view.setCreateTime(sdf.format(invoice.getCreateTime()));
				}
				view.setCompany(invoice.getCompany());
				view.setNumber(invoice.getNumber());
				view.setRemark(invoice.getRemark());
				view.setMoney(Double.valueOf(String.valueOf(invoice.getMoney())));
				CheckMoney check = invoice.getCheck();
				if (check != null) {
					view.setTaxation(check.getMoney());
					view.setPriceTaxationSum(check.getMoney() + Double.valueOf(String.valueOf(invoice.getMoney())));
				}
				view.setImgUrl(invoice.getImgUrl());
				view.setStatus(invoice.getStatus());
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
	 * 通过发票id获取发票信息
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年11月20日
	 */

	@RequiresPermissions(value = { "invoice/getInvoiceById" })
	@PostMapping("getInvoiceById")
	public ResultData getInvoiceById(HttpServletRequest request, String version, Long id) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}

		try {
			// 发票数据信息
			Invoice invoice = null;
			Optional<Invoice> invoiceOpt = invoiceService.queryInvoiceById(id);
			if (invoiceOpt != null) {
				invoice = invoiceOpt.get();
			}
			InvoiceView view = new InvoiceView();
			if (invoice != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				view.setId(invoice.getId());
				if (invoice.getCreateTime() != null) {
					view.setCreateTime(sdf.format(invoice.getCreateTime()));
				}
				if (invoice.getUser() != null && !StringUtils.isEmpty(invoice.getUser().getPhone())) {
					view.setCreatePerson(invoice.getUser().getPhone());
				}
				view.setCompany(invoice.getCompany());
				view.setNumber(invoice.getNumber());
				view.setRemark(invoice.getRemark());
				view.setMoney(Double.valueOf(String.valueOf(invoice.getMoney())));
				CheckMoney check = invoice.getCheck();
				if (check != null) {
					view.setTaxation(check.getMoney());
					view.setPriceTaxationSum(check.getMoney() + Double.valueOf(String.valueOf(invoice.getMoney())));
				}
				view.setImgUrl(invoice.getImgUrl());
			}
			Map<String, Object> map = new HashMap<>();
			map.put("view", view);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 增加发票图片信息
	 * 
	 * @param urls 发票图片（多张）
	 * @param id   发票id
	 * @return
	 * @author tenglong
	 * @date 2018年11月22日
	 */

	@RequiresPermissions(value = { "invoice/insertInvoiceImg" })
	@PostMapping("insertInvoiceImg")
	public ResultData insertInvoiceImg(String urls, HttpServletRequest request, String version, Long id) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("主键不能为空");
		}
		if (StringUtil.isEmpty(urls)) {
			return serverAck.getParamError().setMessage("图片地址不能为空");
		}
		try {
			// 获取发票信息
			Optional<Invoice> invoiceOpt = invoiceService.queryInvoiceById(id);
			if (!invoiceOpt.isPresent()) {
				return serverAck.getParamError().setMessage("发票ID有误");
			}
			Invoice invoice = invoiceOpt.get();
			// 获取图片地址
			String[] urlArr = urls.split(",");
			if (urlArr != null && urlArr.length > 0) {
				String urlTemp = "";
				for (String url : urlArr) {
					urlTemp += baseImageUrl + url + ",";
				}
				urls = urlTemp;
			}
			urls = urls.substring(0, urls.length() - 1);
			// 操作数据
			invoice.setImgUrl(urls);
			invoice.setStatus((byte) 2);
			CheckMoney check = invoice.getCheck();
			if(check != null) {
				check.setStatus((byte)2);
				checkMoneyService.saveStatus(check);
			}
			int ret = invoiceService.saveInvoice(invoice);
			if (ret == 0) {
				return serverAck.getFailure();
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
