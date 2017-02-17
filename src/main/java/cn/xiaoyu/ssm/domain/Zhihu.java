package cn.xiaoyu.ssm.domain;

/**
 * 知乎答案信息类
 *
 * @author xiaoyu
 * @date 2017/2/13
 */
public class Zhihu {
    private int id;
    private String title; //问题标题
    private String url; //网址
    private String answer;
    private String author;
    private int vote; //赞同数

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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getVote() {
        return vote;
    }
    public void setVote(int vote) {
        this.vote = vote;
    }

}
