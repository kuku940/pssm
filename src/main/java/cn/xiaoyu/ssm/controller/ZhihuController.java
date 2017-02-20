package cn.xiaoyu.ssm.controller;

import cn.xiaoyu.ssm.domain.User;
import cn.xiaoyu.ssm.domain.Zhihu;
import cn.xiaoyu.ssm.service.ZhihuService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 知乎爬虫
 *
 * @author xiaoyu
 * @date 2017/2/20
 */
@Controller
@RequestMapping("/zhihu")
public class ZhihuController {
    @Resource
    private ZhihuService zhihuService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String list(@RequestParam(defaultValue="1")int pageIndex, @RequestParam(defaultValue="10")int pageSize,Model model){
        PageInfo<Zhihu> pageInfo = zhihuService.getAllForList(pageIndex,pageSize);
        model.addAttribute("page", pageInfo);
        return "zhihu/list";
    }
}
