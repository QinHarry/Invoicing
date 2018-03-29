package com.halen.repository;

import com.halen.entity.SaleListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleListGoodsRepository extends JpaRepository<SaleListGoods, Integer>, JpaSpecificationExecutor<SaleListGoods> {

    @Query(value="SELECT * FROM t_sale_list_goods WHERE sale_list_id=?1",nativeQuery=true)
    public List<SaleListGoods> listBySaleListId(Integer saleListId);

    @Query(value="DELETE FROM t_sale_list_goods WHERE sale_list_id=?1",nativeQuery=true)
    @Modifying
    public void deleteBySaleListId(Integer saleListId);

    @Query(value = "SELECT SUM(num) AS total FROM t_sale_list_goods WHERE goods_id=?1", nativeQuery = true)
    public Integer getTotalByGoodsId(Integer goodsId);
}
