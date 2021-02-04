package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResultInfo info = new ResultInfo();
        //1. 获取激活码
        String code = req.getParameter("code");
        if (code != null){
            //2. 调用service完成激活操作
            UserService userService = new UserServiceImpl();
            boolean flag = userService.activate(code);

            //3. 判断标记响应不同信息
            info.setFlag(flag);

            if (flag){
                info.setErrorMsg("激活成功, 请<a href='login.html'>登录</a>");
            }else {
                info.setErrorMsg("激活失败，请联系管理员！");
            }
        }else {
            info.setFlag(false);
            info.setErrorMsg("激活失败，请联系管理员！");
        }

        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(info.getErrorMsg());

//        ObjectMapper mapper = new ObjectMapper();
//        String jsonStr = mapper.writeValueAsString(info);
//
//        //将json写回客户端
//        resp.setContentType("application/json; charset=utf-8");
//        resp.getWriter().write(jsonStr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
