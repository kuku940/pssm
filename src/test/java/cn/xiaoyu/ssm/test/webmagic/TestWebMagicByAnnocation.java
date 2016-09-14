package cn.xiaoyu.ssm.test.webmagic;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@HelpUrl("https://github.com/\\w*")
@TargetUrl("https://github.com/\\w*/\\w+")
public class TestWebMagicByAnnocation {

    @ExtractByUrl("https://github.com/code4craft")
    private String author;
    @ExtractBy("//title/text()")
    private String title;

    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000)
                , new ConsolePageModelPipeline(), TestWebMagicByAnnocation.class)
                .addUrl("https://github.com/code4craft").thread(5).run();
    }

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
}
