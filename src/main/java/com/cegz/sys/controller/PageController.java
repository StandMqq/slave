
package com.cegz.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台页面跳转控制类
 *
 */
@Controller
@RequestMapping("page")
public class PageController {

	/**
	 * 服务url
	 */
	@Value("${server.sys_url}")
	private String serverSysUrl;

	@GetMapping("welcome")
	public String welcome() {
		return "welcome";
	}

	@GetMapping("index")
	public String index() {
		return "index";
	}

	@GetMapping("login")
	public String login() {
		return "login";
	}

	@GetMapping("loginout")
	public String loginout(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Subject sb = SecurityUtils.getSubject();
		sb.logout();
		response.sendRedirect(request.getContextPath() + "/login.html");
//			request.getRequestDispatcher(request.getContextPath() + "/login.html").forward(request,response);
		return null;
	}

	@GetMapping("main")
	public String main() {
		return "main";
	}

	@GetMapping("unicode")
	public String unicode() {
		return "unicode";
	}

	@GetMapping("403")
	public String authority() {
		return "403";
	}

	// ---------------------------------------adver（车而告之小程序）---------------------------------------

	@GetMapping("sponsorList")
	public String sponsorList() {
		return "adver/sponsor_list";
	}

	@GetMapping("sponsorInfo")
	public String sponsorInfo() {
		return "adver/sponsor_info";
	}

	@GetMapping("carContactList")
	public String carContactList() {
		return "adver/car_contact_list";
	}
	
	@GetMapping("carAdvertisementsDetail")
	public String carAdvertisementsDetail() {
		return "adver/car_advertisements_detail";
	}
	
	@GetMapping("mapRedEnvelopesAdvertisement")
	public String mapRedEnvelopesAdvertisement() {
		return "adver/map_red_envelopes_advertisement";
	}
	
	@GetMapping("mapRedEnvelopesAdvertisementDetail")
	public String mapRedEnvelopesAdvertisementDetail() {
		return "adver/map_red_envelopes_advertisement_detail";
	}
	
	@GetMapping("abnormityDevice")
	public String abnormityDevice() {
		return "adver/abnormity_device";
	}
	
	@GetMapping("carContactDetail")
	public String carContactDetail() {
		return "adver/car_contact_detail";
	}

	@GetMapping("carList")
	public String carList() {
		return "adver/car_list";
	}

	@GetMapping("carDetail")
	public String carDetail() {
		return "adver/car_detail";
	}

	@GetMapping("admasterList")
	public String admasterList() {
		return "adver/admaster_list";
	}

	@GetMapping("admasterInfo")
	public String admasterInfo() {
		return "adver/admaster_info";
	}

	@GetMapping("agentList")
	public String agentList() {
		return "adver/agent_list";
	}

	@GetMapping("agentInfo")
	public String agentInfo() {
		return "adver/agent_info";
	}

	@GetMapping("advertisementList")
	public String advertisementList() {
		return "adver/advertisement_list";
	}

	@GetMapping("advertisementDetail")
	public String advertisementDetail() {
		return "adver/advertisement_detail";
	}

	@GetMapping("plateNumberInfo")
	public String plateNumberInfo() {
		return "adver/plate_number_info";
	}

	@GetMapping("admasters")
	public String admasters() {
		return "adver/admasters";
	}

	@GetMapping("authoritys")
	public String authoritys() {
		return "adver/authoritys";
	}

	@GetMapping("users")
	public String users() {
		return "adver/users";
	}

	@GetMapping("sponsors")
	public String sponsors() {
		return "adver/sponsors";
	}

	@GetMapping("sponsorsDetail")
	public String sponsorsDetail() {
		return "adver/sponsor_detail";
	}

	@GetMapping("sponsorsDetailAdvertisement")
	public String sponsorsDetailAdvertisement() {
		return "adver/sponsor_detail_advertisement";
	}

	@GetMapping("cars")
	public String cars() {
		return "adver/cars";
	}

	@GetMapping("carContacts")
	public String carContacts() {
		return "adver/car_contacts";
	}

	@GetMapping("advertisements")
	public String advertisements() {
		return "adver/advertisements";
	}

	@GetMapping("publishAdvertisement")
	public String publishAdvertisement() {
		return "adver/publish_advertisement";
	}

	@GetMapping("agents")
	public String agents() {
		return "adver/agents";
	}

	@GetMapping("insertCarContact")
	public String insertCarContact() {
		return "adver/insert_car_contact";
	}

	@GetMapping("insertCar")
	public String insertCar() {
		return "adver/insert_car";
	}

	@GetMapping("insertAgent")
	public String insertAgent() {
		return "adver/insert_agent";
	}

	@GetMapping("insertSponsor")
	public String insertSponsor() {
		return "adver/insert_sponsor";
	}

	@GetMapping("insertUsers")
	public String insertusers() {
		return "adver/insert_users";
	}

	@GetMapping("insertAdvertisement")
	public String insertAdvertisement() {
		return "adver/insert_advertisement";
	}

	@GetMapping("insertAdvertiser")
	public String insertAdvertiser() {
		return "adver/insert_advertiser";
	}

	@GetMapping("insertUsersBlock")
	public String insertUsersBlock() {
		return "adver/insert_users_block";
	}

	@GetMapping("usersBlock")
	public String userBlock() {
		return "adver/users_block";
	}

	@GetMapping("updatePassWord")
	public String updatePassWord() {
		return "adver/update_password";
	}

	@GetMapping("deviceUntie")
	public String deviceUntie() {
		return "adver/device_untie";
	}

	@GetMapping("deviceList")
	public String deviceList() {
		return "adver/devices";
	}

	@GetMapping("subscribeDesignlist")
	public String getSubscribeDesignlist(){
		return "adver/subscribe_design_list";
	}

	@GetMapping("updateSubscribeDesignStatusById")
	public String updateSubscribeDesignStatusById(){
		return "adver/subscribe_design_list";
	}

	@GetMapping("getCaptionShowContentByIsDeleted")
	public String getCaptionShowContentByIsDeleted(){
		return "adver/caption_show";
	}

	@GetMapping("updateCaptionShowContentById")
	public String updateCaptionShowContentById(){
		return "adver/caption_show";
	}

	@GetMapping("getAuthorityList")
	public String getAuthorityList(){
		return "adver/authority_list";
	}

	@GetMapping("queryAuthorityByConditions")
	public String queryAuthorityByConditions(){
		return "adver/insert_authority";
	}


	// ---------------------------------------finance（财务系统）---------------------------------------

	@GetMapping("insertWallet")
	public String insertWallet() {
		return "finance/insert_wallet";
	}

	@GetMapping("wallets")
	public String wallets() {
		return "finance/wallets";
	}

	@GetMapping("checkMoneys")
	public String checkMoneys() {
		return "finance/check_moneys";
	}

	@GetMapping("invoiceList")
	public String invoiceList() {
		return "finance/invoices";
	}

	@GetMapping("invoiceOperation")
	public String invoiceOperation() {
		return "finance/invoice_operation";
	}

	@GetMapping("invoiceDetail")
	public String invoiceDetail() {
		return "finance/invoice_detail";
	}
	
	@GetMapping("contactIncomeRecord")
	public String contactIncomeRecord() {
		return "finance/contact_income_record";
	}
	
	@GetMapping("contactIncomeRecordDetail")
	public String contactIncomeRecordDetail() {
		return "finance/contact_income_record_detail";
	}



	// ---------------------------------------sale（销售系统）---------------------------------------

	@GetMapping("saleBindingAll")
	public String saleBindingAll() {
		return "sale/sale_binding_all";
	}

	@GetMapping("saleBindingUnit")
	public String saleBindingUnit() {
		return "sale/sale_binding_unit";
	}

	@GetMapping("bindingClientsList")
	public String bindingClientsList() {
		return "sale/binding_clients_list";
	}

	@GetMapping("clientsRechargeList")
	public String clientsRechargeList() {
		return "sale/clients_recharge_list";
	}

	@GetMapping("withdrawCashList")
	public String withdrawCashList() {
		return "sale/withdraw_cash_list";
	}

	// ---------------------------------------shop（全城惠小程序）---------------------------------------

	@GetMapping("shopUsers")
	public String shopUsers() {
		return "shop/users";
	}

	@GetMapping("usersTicketList")
	public String usersTicketList() {
		return "shop/users_ticket_list";
	}

	@GetMapping("usersIntegralList")
	public String usersIntegralList() {
		return "shop/users_integral_list";
	}

	@GetMapping("businessList")
	public String businessList() {
		return "shop/business_list";
	}

	@GetMapping("insertBusiness")
	public String insertBusiness() {
		return "shop/insert_business";
	}

	@GetMapping("ticketList")
	public String ticketList() {
		return "shop/ticket_list";
	}

	@GetMapping("insertTicket")
	public String insertTicket() {
		return "shop/insert_ticket";
	}

	@GetMapping("ticketTypeList")
	public String ticketTypeList() {
		return "shop/ticket_type_list";
	}

	@GetMapping("insertTicketType")
	public String insertTicketType() {
		return "shop/insert_ticket_type";
	}

	@GetMapping("ticketDetail")
	public String ticketDetail() {
		return "shop/ticket_detail";
	}

	@GetMapping("gainTicketUserDetail")
	public String gainTicketUserDetail() {
		return "shop/gain_ticket_user_detail";
	}

	@GetMapping("userRedPacketDetail")
	public String userRedPacketDetail() {
		return "shop/user_red_packet_detail";
	}

	@GetMapping("shopWithdrawCashList")
	public String shopWithdrawCashList() {
		return "shop/withdraw_cash_list";
	}
	
	@GetMapping("shopWithdrawCashInfo")
	public String shopWithdrawCashInfo() {
		return "shop/withdraw_cash_info";
	}

	@GetMapping("integralGoodList")
	public String integralGoodList() {
		return "shop/integral_goods";
	}

	@GetMapping("integralGoodsRecord")
	public String integralGoodsRecord() {
		return "shop/integral_goods_record";
	}
	
	@GetMapping("insertIntegralGoods")
	public String insertIntegralGoods() {
		return "shop/insert_integral_goods";
	}

	// ---------------------------------------权限管理（销售系统）---------------------------------------

	@GetMapping("userRoleManage")
	public String userRoleManage() {
		return "authority/user_role_manage";
	}

	@GetMapping("roles")
	public String roles() {
		return "authority/roles";
	}

	@GetMapping("roleMenuManage")
	public String roleMenuManage() {
		return "authority/role_menu_manage";
	}

	@GetMapping("setManage")
	public String setManage() {
		return "authority/set_manage";
	}

}
