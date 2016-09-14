package cn.xiaoyu.ssm.processor;

import cn.xiaoyu.ssm.domain.Zreading;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ZreadingProcessor implements PageProcessor{
    private Site site;
    public void process(Page page) {
        // 定义被保存的一些信息
        Zreading zreading = new Zreading();
        zreading.setTitle(page.getHtml().xpath("//title/text()").toString().trim());
        zreading.setAuthor(page.getHtml().xpath("//span[@itemprop='author']/a/text()").toString());
        zreading.setUrl(page.getUrl().toString());
        zreading.setActicle(page.getHtml().xpath("//div[@itemprop='description']").toString());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date publishDate = sdf.parse(page.getHtml().xpath("//span[@itemprop='datePublished']/text()").toString());
            zreading.setPublishDate(publishDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        page.putField("cn.xiaoyu.ssm.domain.Zreading",zreading);

        //配置后续需要抓取的url地址
        page.addTargetRequests(page.getHtml().links().regex("(http://www.zreading.cn/archives/\\d+.html)").all());
    }

    public Site getSite() {
        return site;
    }
}
