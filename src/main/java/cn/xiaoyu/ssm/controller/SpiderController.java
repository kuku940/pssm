package cn.xiaoyu.ssm.controller;

import cn.xiaoyu.ssm.crawler.SpiderCrawler;
import cn.xiaoyu.ssm.domain.Mail;
import cn.xiaoyu.ssm.domain.User;
import cn.xiaoyu.ssm.service.UserService;
import cn.xiaoyu.ssm.util.Constant;
import cn.xiaoyu.ssm.util.MD5Util;
import cn.xiaoyu.ssm.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author 章小雨
 * @date 2017年2月18日
 * @email roingeek@qq.com
 */

@Controller
@RequestMapping("/spider")
public class SpiderController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name="spiderCrawler")
	private SpiderCrawler spiderCrawler;

	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "spider/list";
	}

	@ResponseBody
	@RequestMapping(value="/touch/{target}",method=RequestMethod.GET)
	public Object touch(@PathVariable(value="target") String target){
		if("zhihu".equals(target)){
			logger.info("爬虫正在爬取知乎页面");
			spiderCrawler.crawlZhihu();
		}else if("zreading".equals(target)){
			logger.info("爬虫正在爬取左岸读书页面");
			spiderCrawler.crawlZreading();
		}else if("music163".equals(target)){
			logger.info("爬虫正在爬取网易云音乐页面");
			spiderCrawler.crawlMusic163();
		}
		return "Spider is Running!";
	}
}