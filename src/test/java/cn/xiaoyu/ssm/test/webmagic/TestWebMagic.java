package cn.xiaoyu.ssm.test.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestWebMagic implements PageProcessor {
    // 抓取网站的相关配置，包括编码、抓取间隔，重试次数
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static void main(String[] args) {
//        Spider.create(new TestWebMagic()).addUrl("http://www.zreading.cn/")
//                .addPipeline(new JsonFilePipeline("D:/webmagic/")).thread(1).run();
        String pattern = "\\d+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher("阅读量：2,990 views".replace(",",""));
        if(m.find()){
            System.out.println(m.group(0));
        }
        System.out.println(1213);
    }

    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     * @param page
     */
    public void process(Page page) {
        // 定义被保存的一些信息
        page.putField("title",page.getHtml().xpath("//title/text()").toString().trim());
        page.putField("publishDate",page.getHtml().xpath("//span[@itemprop='datePublished']/text()").toString().trim());
        page.putField("url",page.getUrl().toString());
        page.putField("author",page.getHtml().xpath("//span[@itemprop='author']/a/text()").toString());
        page.putField("article",page.getHtml().xpath("//div[@itemprop='description']").toString());

        //配置后续需要抓取的url地址
        page.addTargetRequests(page.getHtml().links().regex("(http://www.zreading.cn/archives/\\d+.html)").all());
    }

    public Site getSite() {
        return site;
    }
}
