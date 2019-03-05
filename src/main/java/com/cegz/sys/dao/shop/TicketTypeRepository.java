package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.TicketType;

/**
 * 优惠券类型持久化服务
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

	/**
	 * 获取所有劵类型数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@Query(value = "select * from ticket_type tt where 1=1 and tt.is_deleted = 0 and if(?3 != '', tt.name like %?3%, 1 = 1) "
			+ "order by tt.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<TicketType> getTicketTypeList(Integer curPage, Integer pageSize, String name);

	/**
	 * 查询所有劵类型数据总条数
	 * 
	 * @param name
	 * @return
	 */
	@Query(value = "select count(1) from ticket_type tt where 1=1 and tt.is_deleted = 0 and if(?1 != '', tt.name like %?1%, 1 = 1) ", nativeQuery = true)
	Long getTicketTypeCount(String name);
}
