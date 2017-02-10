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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @ExtractBy("//span[@itemprop='datePublished']/text()")
    private String publishDateStr;
    private Date publishDate;
    @ExtractBy("//span[@itemprop='author']/a/text()")
    private String author;
    @ExtractByUrl
    private String url;
    @ExtractBy("//div[@itemprop='description']")
    private String acticle;
    @ExtractBy("//span[@class='show-view']/text()")
    private String viewsStr;
    private int views;

    /**
     * 进行一些后续处理
     * */
    @Override
    public void afterProcess(Page page) {
        try {
            // 时间解析有些问题，所有在手动进行解析
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date publishDate = sdf.parse(publishDateStr);
            this.setPublishDate(publishDate);

            // 阅读量进行正则匹配
            String pattern = "\\d+";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(this.getViewsStr().replace(",",""));
            if (m.find()) {
                this.setViews(Integer.parseInt(m.group(0)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getPublishDateStr() {
        return publishDateStr;
    }

    public void setPublishDateStr(String publishDateStr) {
        this.publishDateStr = publishDateStr;
    }

    public String getViewsStr() {
        return viewsStr;
    }

    public void setViewsStr(String viewsStr) {
        this.viewsStr = viewsStr;
    }
}
