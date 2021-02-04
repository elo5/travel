package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registUserServlet")   //@WebServlet(name = "UserServlet", urlPatterns = "/user/*")
public class RegistUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //0. 判断验证码是否正确
        String check = req.getParameter("check");
        HttpSession session = req.getSession(); // 从session中获取验证码
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); // 为了保证验证码只能使用一次

        ResultInfo info = new ResultInfo();

        if (!checkcode_server.equalsIgnoreCase(check)){

            info.setFlag(false);
            info.setErrorMsg("验证码错误");

        }else {

            //1. 获取数据
            Map<String, String[]> map = req.getParameterMap();

            //2. 封装对象
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            //3. 调用service完成注册
            UserService service = new UserServiceImpl();
            boolean flag = service.regist(user);

            //4. 响应结果
//        ResultInfo info = new ResultInfo();
            info.setFlag(flag);
            if (flag) { // 注册成功
                info.setErrorMsg("注册成功");
            } else { //注册失败
                info.setErrorMsg("注册失败");
            }
        }

        // 将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(info);

        //将json写回客户端
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(jsonStr);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
