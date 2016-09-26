package cn.xiaoyu.ssm.domain;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.*;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/7.
 * 左岸读书 实体对象
 *
 * TargetUrl 需要爬取的页面
 * HelpUrl 需要爬取的列表页面
 */
@TargetUrl("http://www.zreading.cn/archives/\\d+.html")
@HelpUrl("http://www.zreading.cn/page/\\d+")
public class Zreading implements AfterExtractor {
    private int id;
    @ExtractBy("//title/text()")
    private String title;
    @Formatter("yyyy-MM-dd")
    @ExtractBy("//span[@itemprop='datePublished']/text()")
    private Date publishDate;
    @ExtractBy("//span[@itemprop='author']/a/text()")
    private String author;
    @ExtractByUrl
    private String url;
    @ExtractBy("//div[@itemprop='description']")
    private String acticle;

    @Override
    public void afterProcess(Page page) {
        //进行一些后续处理
    }

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
