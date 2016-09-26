package cn.xiaoyu.ssm.domain;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by roin_zhang on 2016/9/26.
 * 网易云音乐实体类
 */
@TargetUrl("http://music.163.com/song?id=\\d+")
@HelpUrl("http://music.163.com/playlist?id=\\d+")
public class Song163 implements AfterExtractor{
    private int id;
    @ExtractByUrl
    private String url;
    @ExtractBy("//div[@class='tit']/em/text()")
    private String name;
    @ExtractBy("//p[@class='des s-fc4']//a[0]/text()")
    private String singer; //歌手
    @ExtractBy("//span[@id='cnt_comment_count']/text()")
    private int comment_count; //评论数量

    @Override
    public void afterProcess(Page page) {

    }

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
    public int getComment_count() {
        return comment_count;
    }
    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

}
