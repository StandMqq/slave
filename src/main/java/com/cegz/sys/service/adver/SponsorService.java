package com.cegz.sys.service.adver;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.Sponsor;

/**
 * 保荐方后台服务
 * 
 * @author tenglong
 * @date 2018年7月30日
 */
public interface SponsorService {

	/**
	 * 查询保荐方数据总条数
	 * 
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	Long querySponsorExamineTotalCount(Integer status, String name, String phone);

	/**
	 * 获取保荐方未审核列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年7月30日
	 */
	List<Sponsor> listSponsorExamine(Integer curPage, Integer pageSize, Integer status, String name, String phone);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月23日
	 */
	Optional<Sponsor> getSponSorById(Long id);

	/**
	 * 添加保荐方信息
	 * 
	 * @param sponsor
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月24日
	 */
	int save(Sponsor sponsor);

	/**
	 * 审核保荐方信息
	 * 
	 * @param sponsor
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月24日
	 */
	int statusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String companyLittle, String businessNumber, String name, String phone, String email, String address,
			String addressDetail);

	/**
	 * 获取保荐方列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */
	List<Sponsor> getSponsorList();

	/**
	 * 校验保荐方公司名称是否存在
	 * 
	 * @param company : 保荐方公司名称
	 * @return
	 * @author tenglong
	 * @date 2018年10月29日
	 */
	boolean checkCompanyNameExist(String company);

	/**
	 * 校验保荐方公司名称简称是否存在
	 * 
	 * @param companyLittle : 保荐方公司名称简称
	 * @return
	 * @author tenglong
	 * @date 2018年11月19日
	 */
	boolean checkCompanyLittleNameExist(String companyLittle);

	/**
	 * @param id
	 * @param name
	 * @param phone
	 * @return
	 */
	Long querySponsorDetailListCountId(Long id, String name, String phone);
//
//	/**
//	 * @param startPage
//	 * @param pageSize
//	 * @param id
//	 * @param name
//	 * @param phone
//	 * @return
//	 */
//	List<Sponsor> querySponsorList(Integer startPage, Integer pageSize, Long id, String name, String phone);
}
