package cn.xiaoyu.ssm.test;

import cn.xiaoyu.ssm.crawler.ZreadingCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beans*.xml"})
public class ZreadingTest {
    @Resource
    private ZreadingCrawler crawler;

    @Test
    public void testSave(){
        crawler.crawl();
    }
}
