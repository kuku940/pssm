package cn.xiaoyu.ssm.domain;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/7.
 * 左岸读书 实体对象
 */
@TargetUrl("http://www.zreading.cn/archives/\\d+.html")
@HelpUrl("http://www.zreading.cn/page/\\d+")
public class Zreading implements AfterExtractor {
    private int id;
    @ExtractBy("//title/text()")
    private String title;
    @ExtractBy("//span[@itemprop='datePublished']/text()")
    private String publishDateStr;
    private Date publishDate;
    @ExtractBy("//span[@itemprop='author']/a/text()")
    private String author;
    @ExtractByUrl
    private String url;
    @ExtractBy("//div[@itemprop='description']")
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

    public String getPublishDateStr() {
        return publishDateStr;
    }

    public void setPublishDateStr(String publishDateStr) {
        this.publishDateStr = publishDateStr;
    }

    @Override
    public void afterProcess(Page page) {
        try {
            /**
             * 后续处理发布日期转换成为时间格式
             */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.setPublishDate(sdf.parse(this.publishDateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
