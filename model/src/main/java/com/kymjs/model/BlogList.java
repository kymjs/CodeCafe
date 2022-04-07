package com.kymjs.model;


import java.io.Serializable;
import java.util.List;

/**
 * @author kymjs (https://kymjs.com/) on 12/2/15.
 */
public class BlogList implements Serializable {
    private String title;
    private String description;
    private String link;
    private String lastBuildDate;
    private List<Blog> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public List<Blog> getItems() {
        return items;
    }

    public void setItems(List<Blog> items) {
        this.items = items;
    }
}
