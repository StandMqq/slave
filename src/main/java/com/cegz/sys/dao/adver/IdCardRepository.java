package com.cegz.sys.dao.adver;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.IdCard;

/**
 * 身份证持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Transactional
public interface IdCardRepository extends JpaRepository<IdCard, Long> {

	/**
	 * 审核身份证数据
	 */
	@Modifying
	@Query(value = "update id_card set status = ?2, reason = ?3, update_time = ?4 where id = ?1", nativeQuery = true)
	int idCardStatusExamine(Long id, byte status, String reason, Date updateTime);
}
