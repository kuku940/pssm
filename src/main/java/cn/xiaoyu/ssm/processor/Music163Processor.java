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
    private Site site;
    public void process(Page page) {
        // 定义被保存的一些信息
        Music163 music163 = new Music163();
        music163.setUrl(page.getUrl().toString());

        page.putField("cn.xiaoyu.ssm.domain.Music163",music163);

        //配置后续需要抓取的url地址
        page.addTargetRequests(page.getHtml().links().regex("(http://www.zreading.cn/archives/\\d+.html)").all());
    }

    public Site getSite() {
        return site;
    }
}
