package cn.xiaoyu.ssm.test.webmagic;

import cn.xiaoyu.ssm.crawler.SpiderCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Roin_zhang
 * @date 2018/4/22 15:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans*.xml")
public class TestSprider {
    @Resource(name = "spiderCrawler")
    private SpiderCrawler spiderCrawler;

    @Test
    public void spiderZhihu() {
        spiderCrawler.crawlZhihu();
    }
}
