package cn.xiaoyu.ssm.domain;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by roin_zhang on 2016/9/26.
 * 网易云音乐实体类
 */
@TargetUrl("/song?id=\\d+")
@HelpUrl("/playlist/\\d+/")
public class Music163 implements AfterExtractor{
    private int id;
    @ExtractByUrl
    private String url;
    private String name;
    private String singer; //歌手
    private int comments; //评论数量

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSinger() {
        return singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
    public int getComments() {
        return comments;
    }
    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public void afterProcess(Page page) {
        System.out.println("Music163.afterProcess");
    }
}
