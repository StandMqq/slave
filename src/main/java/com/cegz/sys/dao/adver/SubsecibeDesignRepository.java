package com.cegz.sys.dao.adver;

import com.cegz.sys.model.adver.SubscribeDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * 广告预约申请
 *
 * @author maqianqian
 * @date 2019年02月15日
 */
@Transactional
public interface SubsecibeDesignRepository extends JpaRepository<SubscribeDesign,Long> {


    /**
     * 通类型和状态获取广告预约总记录数
     * @param status
     * @return
     */
    @Query(value = "select count(sd.id) from subscribe_design sd where status = ?1 ", nativeQuery = true)
    Long countSubscribeDesignByStatus(Byte status);

    /**
     * 获取不同审核状态的广告预约申请列表数据
     * @param status
     * @param startPage
     * @param pageSize
     * @return
     */
    @Query(value = "select sd.* from subscribe_design sd where sd.status = ?1 " +
            "order by sd.create_time desc limit ?2,?3 ", nativeQuery = true)
    List<SubscribeDesign> listSubscribeDesignByStatus(Byte status, int startPage, Integer pageSize);

    /**
     * 修改广告审核状态
     * @param id
     * @param status
     * @param updateUserId
     * @param updateTime
     */
    @Modifying
    @Query(value = "update subscribe_design sd set sd.status = ?2, sd.update_user_id = ?3, sd.update_time = ?4 where sd.id = ?1 ", nativeQuery = true)
    void updateSubscribeDesignStatusById(Long id, Byte status, Long updateUserId, String updateTime);

    /**
     * 获取审核者电话
     * @param updateUserId
     * @return
     */
    @Query(value = "select u.phone from users u where id = ?1 ", nativeQuery = true)
    String getUpDateUserPhone(Long updateUserId);
}
