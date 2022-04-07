package com.kymjs.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

/**
 * 开源实验室博客
 *
 * @author kymjs (https://kymjs.com/) on 12/2/15.
 */
public class Blog implements Parcelable {
    private String title;
    private String description;
    private String image;
    private String blogid;
    private String pubDate;
    private String type;
    private String typeColor;
    private String author;
    private String link;
    private List<String> tags;
    private List<String> category;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBlogid() {
        return blogid;
    }

    public void setBlogid(String blogid) {
        this.blogid = blogid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blog)) return false;
        Blog blog = (Blog) o;
        return Objects.equals(getBlogid(), blog.getBlogid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getImage(), getBlogid(), getPubDate(), getType(), getTypeColor(), getAuthor(), getLink(), getTags(), getCategory());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.blogid);
        dest.writeString(this.pubDate);
        dest.writeString(this.type);
        dest.writeString(this.typeColor);
        dest.writeString(this.author);
        dest.writeString(this.link);
        dest.writeStringList(this.tags);
        dest.writeStringList(this.category);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.description = source.readString();
        this.image = source.readString();
        this.blogid = source.readString();
        this.pubDate = source.readString();
        this.type = source.readString();
        this.typeColor = source.readString();
        this.author = source.readString();
        this.link = source.readString();
        this.tags = source.createStringArrayList();
        this.category = source.createStringArrayList();
    }

    public Blog() {
    }

    protected Blog(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.blogid = in.readString();
        this.pubDate = in.readString();
        this.type = in.readString();
        this.typeColor = in.readString();
        this.author = in.readString();
        this.link = in.readString();
        this.tags = in.createStringArrayList();
        this.category = in.createStringArrayList();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel source) {
            return new Blog(source);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };
}
