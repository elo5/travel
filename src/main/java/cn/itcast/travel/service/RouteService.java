package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

/**
 * 线路Service
 */
public interface RouteService {


    /**
     * 根据类型分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname);

}
