package com.kymjs.model.osc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;

/**
 * 收藏实体类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月14日 下午2:27:39
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class FavoriteList {

    @XStreamAlias("id")
    protected int id;
    @XStreamAlias("pagesize")
    private int pageSize;
    @XStreamAlias("favorites")
    private ArrayList<Favorite> favoritelist = new ArrayList<Favorite>();

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pagesize) {
        this.pageSize = pagesize;
    }

    public ArrayList<Favorite> getFavoritelist() {
        return favoritelist;
    }

    public void setFavoritelist(ArrayList<Favorite> favoritelist) {
        this.favoritelist = favoritelist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
