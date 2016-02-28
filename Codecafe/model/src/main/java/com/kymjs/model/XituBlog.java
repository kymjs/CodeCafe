package com.kymjs.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author kymjs (http://www.kymjs.com/) on 12/3/15.
 */
@XStreamAlias("item")
public class XituBlog implements Parcelable {
    public static final String XITU_GUANGGAO = "下载掘金应用，挖掘最优质的互联网技术";

    @XStreamAlias("title")
    private String title;
    @XStreamAlias("description")
    private String description;
    @XStreamAlias("pubDate")
    private String pubDate;
    @XStreamAlias("link")
    private String link;
    @XStreamAlias("dc:creator")
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static String parserHtml(String html) {
        return html.replaceAll("\\<.*?>", "");
    }

    public void format() {
        //time
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        try {
            Date date = sdf.parse(pubDate);
            sdf.applyPattern("yyyy-MM-dd");
            pubDate = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //description
        this.description = parserHtml(description.replace(XITU_GUANGGAO, " "));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.pubDate);
        dest.writeString(this.link);
        dest.writeString(this.author);
    }

    public XituBlog() {
    }

    protected XituBlog(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.pubDate = in.readString();
        this.link = in.readString();
        this.author = in.readString();
    }

    public static final Parcelable.Creator<XituBlog> CREATOR = new Parcelable.Creator<XituBlog>() {
        public XituBlog createFromParcel(Parcel source) {
            return new XituBlog(source);
        }

        public XituBlog[] newArray(int size) {
            return new XituBlog[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o instanceof XituBlog) {
            XituBlog data = (XituBlog) o;
            if (data.link != null) return data.link.equals(link);
            else return link == null;
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }
}
