package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet{

    private RouteService mRouteService = new RouteServiceImpl();

    /**
     * 分页查询
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String s = null;
        String data = "";
        while((s = br.readLine()) != null) {
            data = data.concat(s).concat("\n");
        }
        //data = data.substring(0,data.length()-1);
        System.out.println(data);

        String currentPageStr = req.getParameter("currentPage");
        String pageSizeStr = req.getParameter("pageSize");
        String cidStr = "5";//req.getParameter("cid");

        String rname = "a"; //req.getParameter("thename");
//        rname = (rname == null || rname.length() == 0 ) ? "" : new String(rname.getBytes("iso-8859-1"), "utf-8");
//        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");

        int cid = 0;
        if (cidStr != null && cidStr.length() > 0){
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 1;
        if (currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }

        int pageSize = 10;
        if (pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 调用service查询PageBean对象
        PageBean<Route> pageBean = mRouteService.pageQuery(cid,currentPage, pageSize, rname);

        //序列化PageBean返回
        writeValue(resp,pageBean);
    }

    /**
     * 根据id查看详情（返回一条详情数据）
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ridStr = req.getParameter("rid");
        Route route = mRouteService.findOne(ridStr);
        //序列化PageBean返回
        writeValue(resp,route);
    }


}
