package cn.xiaoyu.ssm.domain;

/**
 * Created by roin_zhang on 2016/9/26.
 * 网易云音乐实体类
 */
public class Music163{
    private int id;
    private String url;
    private String name;
    private String singer; //歌手
    private int commentCount; //评论数量

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
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
