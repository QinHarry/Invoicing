package com.halen.repository;

import com.halen.entity.CustomerReturnListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerReturnListGoodsRepository extends JpaRepository<CustomerReturnListGoods, Integer>, JpaSpecificationExecutor<CustomerReturnListGoods> {

    @Query(value="SELECT * FROM t_customer_return_list_goods WHERE customer_return_list_id=?1",nativeQuery=true)
    public List<CustomerReturnListGoods> listByCustomerReturnListId(Integer customerReturnListId);

    @Query(value="DELETE FROM t_customer_return_list_goods WHERE customer_return_list_id=?1",nativeQuery=true)
    @Modifying
    public void deleteByCustomerReturnListId(Integer customerReturnListId);

    @Query(value = "SELECT SUM(num) AS total FROM t_customer_return_list_goods WHERE goods_id=?1", nativeQuery = true)
    public Integer getTotalByGoodsId(Integer goodsId);
}
