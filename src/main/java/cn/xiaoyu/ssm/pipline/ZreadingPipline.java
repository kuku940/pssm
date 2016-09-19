package cn.xiaoyu.ssm.pipline;

import cn.xiaoyu.ssm.dao.ZreadingDao;
import cn.xiaoyu.ssm.domain.Zreading;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@Service("zreadingPipline")
public class ZreadingPipline implements PageModelPipeline<Zreading> {

    @Resource
    private ZreadingDao zreadingDao;

    public void process(Zreading zreading, Task task) {
        // 调用Mybatis Dao保存结果
        zreadingDao.save(zreading);
    }
}
