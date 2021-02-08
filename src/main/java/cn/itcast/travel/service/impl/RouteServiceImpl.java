package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();

    /**
     * 根据类型分页查询
     *
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pageBean = new PageBean<Route>();
        pageBean.currentPage = currentPage;
        pageBean.pageSize = pageSize;
        pageBean.totalCount = routeDao.findTotalCount(cid, rname);
        pageBean.list  = routeDao.findByPage(cid, rname,(currentPage -1) * pageSize, pageSize);
        pageBean.totalPage = (pageBean.totalCount % pageSize == 0 ? pageBean.totalCount/pageSize : pageBean.totalCount / pageSize + 1);
        return pageBean;
    }
}
