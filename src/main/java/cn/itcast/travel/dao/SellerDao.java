package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {

    /**
     * 根据id查询卖家
     * @param id
     * @return
     */
    Seller findById(int id);
}
