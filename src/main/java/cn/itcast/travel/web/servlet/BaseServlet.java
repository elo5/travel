package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 完成方法分发
        //1. 获取请求路径
        String uri = req.getRequestURI(); //  eg. :  /user/add
        //2. 获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println("BaseServlet -- service: 方法名称 " + methodName);
        //3. 获取方法对象Method   this关键字，谁调用我？我代表谁
        try {
//            //getDeclaredMethod 忽略访问权限修饰符
//            Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
//            //4. 执行方法
//            //暴力反射
//            method.setAccessible(true);   // 最好不要暴露protected方法，而是修改访问修饰符

            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
            //4. 执行方法
            method.invoke(this, req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将传入的对象序列化为json，并且写回客户端
     * @param resp
     * @param obj
     */
    public void writeValue(HttpServletResponse resp, Object obj) throws IOException{
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonStr = mapper.writeValueAsString(obj);
//        //将json写回客户端
//        resp.setContentType("application/json; charset=utf-8");
//        resp.getWriter().write(jsonStr);


        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json; charset=utf-8");
        mapper.writeValue(resp.getOutputStream(), obj);
    }

    /**
     *
     * @param obj
     */
    public void writeValueAsString(Object obj) {

    }
}
