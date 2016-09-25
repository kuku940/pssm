package cn.xiaoyu.ssm.test;

import cn.xiaoyu.ssm.domain.Zreading;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beans*.xml"})
public class ZreadingTest {
    @Resource(name="zreadingPipline")
    private PageModelPipeline<Zreading> zreadingPipline;

    @Test
    public void testSave() {
        OOSpider.create(Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")
                ,zreadingPipline, Zreading.class).addUrl("http://www.zreading.cn/").thread(1).run();
    }
}
