package cn.xiaoyu.ssm.crawler;

import cn.xiaoyu.ssm.domain.Music163;
import cn.xiaoyu.ssm.domain.Zhihu;
import cn.xiaoyu.ssm.domain.Zreading;
import cn.xiaoyu.ssm.pipline.ZhihuPipline;
import cn.xiaoyu.ssm.processor.ZhihuProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.example.ZhihuPageProcessor;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@Service("spiderCrawler")
public class SpiderCrawler {
    @Resource(name="zreadingPipline")
    private PageModelPipeline<Zreading> zreadingPageModelPipeline;
    @Resource(name="zhihuPipline")
    private ZhihuPipline zhihuPipline;
    @Resource(name="music163Pipline")
    private PageModelPipeline<Music163> music163PageModelPipeline;

    /**
     * 抓取 左岸读书文章
     */
    public void crawlZreading(){
        Site site = Site.me()
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");
        OOSpider.create(site,zreadingPageModelPipeline, Zreading.class).addUrl("http://www.zreading.cn/").thread(10).run();
    }

    /**
     * 抓取 知乎答案
     */
    public void crawlZhihu(){
        OOSpider.create(new ZhihuProcessor()).addPipeline(zhihuPipline)
                .addUrl("https://www.zhihu.com/collection/37406996")
                .addUrl("https://www.zhihu.com/collection/34173061")
                .thread(3).run();
    }

    /**
     * 抓取 网易云音乐
     */
    public void crawlMusic163(){
        Site site = Site.me()
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .setCharset("UTF-8");
//        OOSpider.create(site,music163PageModelPipeline, Music163.class).addUrl("http://music.163.com/song?id=202373").thread(1).run();
    }
}