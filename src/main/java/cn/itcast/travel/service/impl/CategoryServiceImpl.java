package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    /**
     * 查询所有分类
     *
     * @return
     */
    @Override
    public List<Category> findAll() {

        //1. 从redis中查询
        //1.1 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2 使用sortedset排序查询
//        Set<String> rdCategories = jedis.zrange("category", 0, -1); // 查询所有

        //1.3 查询sortedset中的分数cid 和 值cname
        Set<Tuple> rdCategories = jedis.zrangeWithScores("category",0,-1);


        //2. 判断查询集合是否为空
        List<Category> dbCategories = null;
        if (rdCategories == null || rdCategories.size() == 0) {

            System.out.println("redis 为空 需从数据库查询，并将数据存入redis");

            //3. 为空 需从数据库查询，并将数据存入redis
            dbCategories = categoryDao.findAll();
            //将集合数据存储到redis中的category的key
            for (int i = 0; i< dbCategories.size(); i++){
                jedis.zadd("category", dbCategories.get(i).getCid(), dbCategories.get(i).getCname());
            }
        }else {
            System.out.println("redis 不为空");
            dbCategories = new ArrayList<Category>();
            for (Tuple tuple : rdCategories) {
                dbCategories.add(new Category((int)tuple.getScore(), tuple.getElement()));
            }
        }

        //4. 不为空，直接返回
        return dbCategories;
    }
}
