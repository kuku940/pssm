package cn.xiaoyu.ssm.crawler;

import cn.xiaoyu.ssm.domain.Zreading;
import org.springframework.stereotype.Controller;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@Controller("zreadingCrawler")
public class ZreadingCrawler {
    @Resource(name="zreadingPipline")
    private PageModelPipeline<Zreading> zreadingPageModelPipeline;

    public void crawl(){
        OOSpider.create(Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")
                ,zreadingPageModelPipeline, Zreading.class).addUrl("http://www.zreading.cn/").thread(5).run();
    }
}
