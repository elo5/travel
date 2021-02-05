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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        UserService userService = new UserServiceImpl();
        User loginUser = userService.login(user);

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
