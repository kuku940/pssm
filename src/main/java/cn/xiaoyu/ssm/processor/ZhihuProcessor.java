package cn.xiaoyu.ssm.processor;

import cn.xiaoyu.ssm.domain.Zhihu;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 知乎爬取工具
 *
 * @author xiaoyu
 * @date 2017/2/17
 */
public class ZhihuProcessor implements PageProcessor {

    private Site site = Site.me()
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .setCharset("UTF-8")
            .addCookie("_xsrf", "3517ac9e17387a11f57bf70df1c0b310")
            .addCookie("_zap", "bfbc0a11-f73a-4dd0-96c6-d11a1784815e")
            .addCookie("z_c0", "Mi4wQUFBQ0FXVHhVZ3NBQUlLSmI3eE9DeVlBQUFCZ0FsVk5JSFhPV0FENTloVEVHc0EtU3l1M2Noa0NCUlJjV2NHaWxB|1487333424|fc4eb05f00bad2ee9410ccb6bee55536b6699192")
            .setSleepTime(3000);


    @Override
    public void process(Page page) {
        //表示页面是符合条件的页面
        String url = page.getUrl().toString();
        if(page.getUrl().regex("https://www\\.zhihu\\.com/question/\\d+/answer/\\d+").match()){
            // 定义被保存的一些信息
            Zhihu zhihu = new Zhihu();
            zhihu.setUrl(url);
            zhihu.setTitle(page.getHtml().xpath("//div[@id='zh-question-title']//a/text()").toString().trim());
            zhihu.setAuthor(page.getHtml().xpath("//div[@id='zh-question-answer-wrap']//a[@class='author-link']/text()").toString());
            zhihu.setAnswer(page.getHtml().xpath("//div[@id='zh-question-answer-wrap']//div[@class='zm-item-rich-text']").toString());
            zhihu.setVote(Integer.parseInt(page.getHtml().xpath("//div[@id='zh-question-answer-wrap']//span[@class='js-voteCount']/text()").toString()));
            page.putField("cn.xiaoyu.ssm.domain.Zhihu",zhihu);
        }


        //配置后续需要抓取的url地址
        Set<String> set = new HashSet<String>();

        List<String> list = new ArrayList<>();
//        list.addAll(page.getHtml().links().regex("(/question/\\d+)").all());
        list.addAll(page.getHtml().links().regex("(\\?page=\\d+)").all());
        list.addAll(page.getHtml().links().regex("(/question/\\d+/answer/\\d+)").all());
//        list.addAll(page.getHtml().links().regex("(https://www\\.zhihu\\.com/question/\\d+/answer/\\d+)").all());
        String tmpurl = "";
        for(int i=0;i<list.size();i++){
            tmpurl = list.get(i);
            if(tmpurl != null && !("".equals(url))){
                if(tmpurl.contains("https://www.zhihu.com")){
                    set.add(tmpurl);
                }else if(tmpurl.contains("?page=")) {
                    if(url.contains("?page=")){
                        set.add(url.substring(0,url.indexOf("?")) + tmpurl);
                    }else{
                        set.add(url + tmpurl);
                    }
                }else{
                    set.add("https://www.zhihu.com"+tmpurl);
                }
            }
        }
        page.addTargetRequests(new ArrayList(set));
    }

    @Override
    public Site getSite() {
        return site;
    }
}