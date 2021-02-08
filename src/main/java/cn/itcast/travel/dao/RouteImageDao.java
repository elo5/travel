package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImageDao {

    /**
     * 根据id，查询图片集合信息
     * @param rid
     * @return
     */
    List<RouteImg> findByRid(int rid);
}
