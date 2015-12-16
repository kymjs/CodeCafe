package com.kymjs.model.osc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 收藏实体类
 *
 * @author hww
 */
@SuppressWarnings("serial")
@XStreamAlias("favorite")
public class Favorite {

    @XStreamAlias("objid")
    public int id;
    @XStreamAlias("type")
    public int type;
    @XStreamAlias("title")
    public String title;
    @XStreamAlias("url")
    public String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Favorite) {
            Favorite data = (Favorite) o;
            return data.id == id;
        } else {
            return super.equals(o);
        }
    }

}
