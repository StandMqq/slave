package com.cegz.sys.controller.shop;

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
import com.cegz.sys.model.shop.IntegralGoods;
import com.cegz.sys.model.view.shop.IntegralGoodsView;
import com.cegz.sys.service.shop.IntegralGoodsService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分商品后台控制类
 * 
 * @author tenglong
 * @date 2018年11月29日
 */
@RestController
@RequestMapping("/integralGoods")
@Slf4j
public class IntegralGoodsController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private IntegralGoodsService integralGoodsService;

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
	 * 获取积分商品数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name      ：商品名称
	 * @param isDeleted ：上架、下架
	 * @return
	 */

	@RequiresPermissions(value = { "integralGoods/getIntegralGoodsList" })
	@PostMapping("getIntegralGoodsList")
	public ResultData getIntegralGoodsList(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, String name, String isDeleted) {
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
		if (StringUtils.isEmpty(isDeleted)) {
			isDeleted = null;
		}
		try {
			List<IntegralGoodsView> integralGoodsList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = integralGoodsService.getIntegralGoodsCount(name, isDeleted);
			// 积分商品数据列表
			List<IntegralGoods> list = integralGoodsService.getIntegralGoodsList(startPage, pageSize, name, isDeleted);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (IntegralGoods ig : list) {
				IntegralGoodsView view = new IntegralGoodsView();
				view.setId(ig.getId());
				view.setName(ig.getName());
				view.setIntegral(ig.getIntegral());
				view.setTotal(ig.getTotal());
				view.setCurrentTotal(ig.getCurrentTotal());
				view.setCreateTime(sdf.format(ig.getCreateTime()));
				view.setIsDeleted(ig.getIsDeleted());
				integralGoodsList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", integralGoodsList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过id获取单条积分商品数据
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年9月20日
	 */

	@RequiresPermissions(value = { "integralGoods/getIntegralGoodsById" })
	@PostMapping("getIntegralGoodsById")
	public ResultData getIntegralGoodsById(Long id, HttpServletRequest request, String version) {
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
			// 积分商品数据
			Optional<IntegralGoods> integralGoodsOpt = integralGoodsService.getIntegralGoodsById(id);
			if (!integralGoodsOpt.isPresent()) {
				return serverAck.getParamError().setMessage("积分商品ID有误");
			}
			IntegralGoods ig = integralGoodsOpt.get();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			IntegralGoodsView view = new IntegralGoodsView();
			view.setId(ig.getId());
			view.setName(ig.getName());
			view.setIntegral(ig.getIntegral());
			view.setTotal(ig.getTotal());
			view.setCurrentTotal(ig.getCurrentTotal());
			view.setCreateTime(sdf.format(ig.getCreateTime()));
			view.setIsDeleted(ig.getIsDeleted());
			view.setImages(ig.getImages());
			return serverAck.getSuccess().setData(view);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 录入积分商品
	 * 
	 * @param token
	 * @param version
	 * @param name
	 * @param ticketTypeNo
	 * @return
	 */

	@RequiresPermissions(value = { "integralGoods/insertIntegralGoods" })
	@PostMapping("insertIntegralGoods")
	public ResultData insertIntegralGoods(HttpServletRequest request, String version, Long id, String name,
			Integer integral, Integer total, String images) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (StringUtil.isEmpty(name)) {
			return serverAck.getParamError().setMessage("商品名称不能为空");
		}
		if (integral == null) {
			return serverAck.getParamError().setMessage("所需积分不能为空");
		}
		if (total == null) {
			return serverAck.getParamError().setMessage("发放数量不能为空");
		}
		try {
			IntegralGoods integralGoods = null;
			if (id != null) {
				Optional<IntegralGoods> integralGoodsOpt = integralGoodsService.getIntegralGoodsById(id);
				if (!integralGoodsOpt.isPresent()) {
					return serverAck.getParamError().setMessage("id无效");
				}
				integralGoods = integralGoodsOpt.get();
				if (total < integralGoods.getCurrentTotal()) {
					return serverAck.getParamError().setMessage("总数不能小于领取数，当前领取数：" + integralGoods.getCurrentTotal());
				}
				if (!StringUtil.isEmpty(images)) {
					integralGoods.setImages(baseImageUrl + images);
				}
				integralGoods.setUpdateTime(new Date());
			} else {
				integralGoods = new IntegralGoods();
				integralGoods.setId(id);
				integralGoods.setCreateTime(new Date());
				integralGoods.setIsDeleted((byte) 0);
				integralGoods.setCurrentTotal(total);
				integralGoods.setImages(baseImageUrl + images);
			}
			integralGoods.setName(name);
			integralGoods.setTotal(total);
			integralGoods.setIntegral(integral);

			// 数据存储
			int ret = integralGoodsService.saveIntegralGoods(integralGoods);
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
	 * 下架积分商品
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "integralGoods/downIntegralGoods" })
	@PostMapping("downIntegralGoods")
	public ResultData deleteIntegralGoods(HttpServletRequest request, String version, Long id) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		com.cegz.sys.model.adver.Users user = (com.cegz.sys.model.adver.Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("主键不能为空");
		}
		try {
			Optional<IntegralGoods> opt = integralGoodsService.getIntegralGoodsById(id);
			// 处理
			IntegralGoods integralGoods = opt.get();
			integralGoods.setIsDeleted((byte) 1);
			integralGoods.setUpdateTime(new Date());
			int ret = integralGoodsService.saveIntegralGoods(integralGoods);
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
