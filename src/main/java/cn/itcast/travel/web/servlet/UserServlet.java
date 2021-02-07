package cn.itcast.travel.web.servlet;


import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet{

    private UserService mUserService = new UserServiceImpl();

    /**
     * 注册
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
            boolean flag = mUserService.regist(user);

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

    /**
     * 登陆
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 获取用户名和密码数据
        Map<String, String[]> map = req.getParameterMap();
        //2. 封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3. 调用Service查询
        User loginUser = mUserService.login(user);

        //4. 响应数据
        ResultInfo info = new ResultInfo();

        //4.1 判断用户对象是否为null
        if (loginUser == null){ // 用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }else {
            //4.2 判断用户是否激活
            if (!loginUser.getStatus().equalsIgnoreCase("Y")){
                info.setFlag(false);
                info.setErrorMsg("请登陆注册邮箱进行激活");
            }else {
                info.setFlag(true);
                info.setErrorMsg("登陆成功");

                req.getSession().setAttribute("user", loginUser);
            }
        }

//        ObjectMapper mapper = new ObjectMapper();
//        String jsonStr = mapper.writeValueAsString(info);
//
//        //将json写回客户端
//        resp.setContentType("application/json; charset=utf-8");
//        resp.getWriter().write(jsonStr);
        writeValue(resp, info);
    }

    /**
     * 查询单个用户（当前登陆用户）
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findAUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Object user = req.getSession().getAttribute("user");

//        ObjectMapper mapper = new ObjectMapper();
//        String jsonStr = mapper.writeValueAsString(user);
//
//        //将json写回客户端
//        resp.setContentType("application/json; charset=utf-8");
//        resp.getWriter().write(jsonStr);
        writeValue(resp, user);

    }

    /**
     * 激活账号
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void activate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResultInfo info = new ResultInfo();
        //1. 获取激活码
        String code = req.getParameter("code");
        if (code != null){
            //2. 调用service完成激活操作
            boolean flag = mUserService.activate(code);

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

    /**
     * 退出登陆
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(req.getSession().getAttribute("user")!=null){
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.html");
        }
    }

}
