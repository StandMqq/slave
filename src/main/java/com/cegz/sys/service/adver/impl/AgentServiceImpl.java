package com.cegz.sys.service.adver.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.AgentRepository;
import com.cegz.sys.model.adver.Agent;
import com.cegz.sys.service.adver.AgentService;

/**
 * 代理服务
 * 
 * @author tenglong
 * @date 2018年8月1日
 */
@Service("agentService")
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentRepository agentRepository;

	@Override
	public List<Agent> listAgentExamine(Integer curPage, Integer pageSize, Integer status, String name, String phone) {
		return agentRepository.getAgentExamineList(curPage, pageSize, status, name, phone);
	}

	@Override
	public Long queryAgentExamineTotalCount(Integer status, String name, String phone) {
		return agentRepository.queryAgentExamineTotalCount(status, name, phone);
	}

	@Override
	public Optional<Agent> getAgentById(Long id) {
		return agentRepository.findById((Long) id);
	}

	@Override
	public Agent insert(Agent agent) {
		return agentRepository.save(agent);
	}

	@Override
	public int statusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String businessNumber, String name, String phone, String email, String address, String addressDetail) {
		return agentRepository.statusExamine(id, status, reason, updateTime, city, company, businessNumber, name, phone,
				email, address, addressDetail);
	}
}
