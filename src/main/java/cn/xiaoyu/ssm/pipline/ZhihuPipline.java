package cn.xiaoyu.ssm.pipline;

import cn.xiaoyu.ssm.dao.ZhihuDao;
import cn.xiaoyu.ssm.domain.Zhihu;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@Component("zhihuPipline")
public class ZhihuPipline implements Pipeline {

    @Resource
    private ZhihuDao zhihuDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Zhihu zhihu = resultItems.get("cn.xiaoyu.ssm.domain.Zhihu");
        if(zhihu != null && zhihu.getVote()>5000){
            // 只做增量更新，如果该对应存在的话，就不需要将这条数据更新
            Zhihu z1 = zhihuDao.getByUrl(zhihu.getUrl());
            if(z1 == null){
                // 调用Mybatis Dao保存结果
                zhihuDao.save(zhihu);
            }else{
                zhihuDao.update(zhihu);
            }
        }
    }
}
