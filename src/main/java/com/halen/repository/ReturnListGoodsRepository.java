package com.halen.repository;


import com.halen.entity.ReturnListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReturnListGoodsRepository extends JpaRepository<ReturnListGoods, Integer>, JpaSpecificationExecutor<ReturnListGoods> {

    @Query(value = "SELECT * FROM t_return_list_goods WHERE return_list_id=?1", nativeQuery = true)
    public List<ReturnListGoods> listByReturnListId(Integer returnListId);

    @Query(value = "DELETE FROM t_return_list_goods WHERE return_list_id=?1", nativeQuery = true)
    @Modifying
    public void deleteByReturnListId(Integer returnListId);
}
