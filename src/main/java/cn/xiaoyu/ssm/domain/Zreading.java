package cn.xiaoyu.ssm.domain;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/7.
 * 左岸读书 实体对象
 */
public class Zreading {
    private int id;
    private String title;
    private Date publishDate;
    private String author;
    private String url;
    private String acticle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActicle() {
        return acticle;
    }

    public void setActicle(String acticle) {
        this.acticle = acticle;
    }
}
