package cn.xiaoyu.ssm.processor;

import cn.xiaoyu.ssm.domain.Music163;
import cn.xiaoyu.ssm.domain.Zreading;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/7.
 * 这儿的信息可以使用注释来替代，详见现在的Zreading类
 * @see Zreading -- 可以将所有的信息都放在注释和后置处理器中
 */
public class Music163Processor implements PageProcessor{
    private Site site = Site.me()
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .setCharset("UTF-8");

    public void process(Page page) {
        String url = page.getUrl().toString();

        // 定义被保存的一些信息
        Music163 music163 = new Music163();
        music163.setUrl(url);
        music163.setName(page.getHtml().xpath("//a[@class='u-btni-share']/@data-res-name").toString());
        music163.setSinger(page.getHtml().xpath("//a[@class='u-btni-share']/@data-res-author").toString());

        music163.setCommentCount(1);

        page.putField("cn.xiaoyu.ssm.domain.Music163",music163);

        //配置后续需要抓取的url地址
        page.addTargetRequests(page.getHtml().links().regex("(http://www.zreading.cn/archives/\\d+.html)").all());
    }

    public Site getSite() {
        return site;
    }
}
