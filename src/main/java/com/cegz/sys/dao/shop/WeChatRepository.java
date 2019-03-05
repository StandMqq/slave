package com.cegz.sys.dao.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.WeChat;

/**
 * 微信持久化接口
 * 
 * @author tenglong
 * @date 2018年9月24日
 */
public interface WeChatRepository extends JpaRepository<WeChat, Long> {
	@Query(value = "select * from wechat where user_id = ?1 and is_deleted = 0", nativeQuery = true)
	WeChat getWeChatByUsersId(Long userId);
}
