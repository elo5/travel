package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
   boolean regist(User user);

    /**
     * 激活用户
     * @param code
     * @return
     */
   boolean activate(String code);

    /**
     * 用户登陆
     * @param user
     * @return
     */
   User login(User user);
}
