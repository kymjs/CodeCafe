package com.kymjs.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

/**
 * @author kymjs (http://www.kymjs.com/) on 12/3/15.
 */
@XStreamAlias("rss")
public class XituBlogList {

    @XStreamAlias("channel")
    private Channel channel = new Channel();

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @XStreamAlias("channel")
    public class Channel {
        @XStreamAlias("title")
        private String title;
        @XStreamAlias("description")
        private String description;
        @XStreamAlias("link")
        private String link;
        @XStreamImplicit
        private ArrayList<XituBlog> itemArray = new ArrayList<>();

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

        public ArrayList<XituBlog> getItemArray() {
            return itemArray;
        }

        public void setItemArray(ArrayList<XituBlog> itemArray) {
            this.itemArray = itemArray;
        }
    }
}
