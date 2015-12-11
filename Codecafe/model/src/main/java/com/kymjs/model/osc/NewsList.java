package com.kymjs.model.osc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;

/**
 * 新闻列表实体类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月27日 下午5:55:58
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class NewsList {

    @XStreamAlias("id")
    protected int id;

    @XStreamAlias("catalog")
    private int catalog;

    @XStreamAlias("pagesize")
    private int pageSize;

    @XStreamAlias("newscount")
    private int newsCount;

    @XStreamAlias("newslist")
    private ArrayList<News> list = new ArrayList<News>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }

    public ArrayList<News> getList() {
        return list;
    }

    public void setList(ArrayList<News> list) {
        this.list = list;
    }
}
