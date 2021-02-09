package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImageDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImageDao routeImageDao = new RouteImageDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();

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

    /**
     * 根据rid查询详情
     *
     * @param rid
     * @return
     */
    @Override
    public Route findOne(String rid) {
        //1 根据id去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2. 根据id，查询图片集合信息
        List<RouteImg> list = routeImageDao.findByRid(Integer.parseInt(rid));
        route.setRouteImgList(list);

        //3. 根据id，查询卖家信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        return route;
    }


}
