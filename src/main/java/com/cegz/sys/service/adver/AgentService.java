package com.cegz.sys.service.adver;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.Agent;

/**
 * 代理商后台服务
 * 
 * @author tenglong
 * @date 2018年8月1日
 */
public interface AgentService {

	/**
	 * 查询代理商数据总条数
	 * 
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	Long queryAgentExamineTotalCount(Integer status, String name, String phone);

	/**
	 * 获取代理商未审核列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年7月30日
	 */
	List<Agent> listAgentExamine(Integer curPage, Integer pageSize, Integer status, String name, String phone);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */
	Optional<Agent> getAgentById(Long id);

	/**
	 * 录入代理商信息
	 * 
	 * @param agent
	 * @return
	 * @author Administrator
	 * @date 2018年8月1日
	 */
	Agent insert(Agent agent);

	/**
	 * 审核代理商信息
	 * 
	 * @param agent
	 * @return
	 * @author Administrator
	 * @date 2018年8月1日
	 */
	int statusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String businessNumber, String name, String phone, String email, String address, String addressDetail);
}
