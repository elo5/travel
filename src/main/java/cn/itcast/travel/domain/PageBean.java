package cn.itcast.travel.domain;

import java.util.List;

/**
 * 分页对象
 */
public class PageBean<T> {

    /**
     * 总记录数
     */
    public int totalCount;

    /**
     * 总页数
     */
    public int totalPage;

    /**
     * 当前页码
     */
    public int currentPage;

    /**
     * 每页显示的条数
     */
    public int pageSize;

    /**
     * 每页显示的数据集合
     */
    public List<T> list;

}
