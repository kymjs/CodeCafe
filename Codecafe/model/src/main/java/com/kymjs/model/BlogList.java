package com.kymjs.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

/**
 * @author kymjs (http://www.kymjs.com/) on 12/2/15.
 */
@XStreamAlias("rss")
public class BlogList {
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
        @XStreamAlias("pubDate")
        private String pubDate;
        @XStreamAlias("lastBuildDate")
        private String lastBuildDate;
        @XStreamAlias("generator")
        private String generator;
        @XStreamImplicit
        private ArrayList<Blog> itemArray = new ArrayList<>();

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

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public String getLastBuildDate() {
            return lastBuildDate;
        }

        public void setLastBuildDate(String lastBuildDate) {
            this.lastBuildDate = lastBuildDate;
        }

        public String getGenerator() {
            return generator;
        }

        public void setGenerator(String generator) {
            this.generator = generator;
        }

        public ArrayList<Blog> getItemArray() {
            return itemArray;
        }

        public void setItemArray(ArrayList<Blog> item) {
            this.itemArray = item;
        }
    }
}
