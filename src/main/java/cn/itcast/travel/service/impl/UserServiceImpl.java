package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {


    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {

        //1. 根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());

       //如果用户存在
        if (u != null) {
            return false;
        }

        //2. 保存用户信息
        //2.1 设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2 设置激活状态
        user.setStatus("N");
        userDao.save(user);

        //3. 激活邮件发送
        String content = "<a href='http://localhost/travel/activeUserServlet?code=" +user.getCode()+"'>点击激活【hhhh】</a>";
//        MailUtils.sendMail(user.getEmail(), content, "激活账号");
        System.out.println(content);
        return true;
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    public boolean activate(String code) {

        //1. 根据激活码查询用户对象
        User user = userDao.findByCode(code);

        //2. 调用dao的修改激活状态的方法
        if (user != null) {
            userDao.updateStatus(user);
            return true;
        }
        return false;
    }

    /**
     * 用户登陆
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
