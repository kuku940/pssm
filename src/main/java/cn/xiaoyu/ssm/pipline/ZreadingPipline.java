package cn.xiaoyu.ssm.pipline;

import cn.xiaoyu.ssm.dao.ZreadingDao;
import cn.xiaoyu.ssm.domain.Zreading;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@Component("zreadingPipline")
public class ZreadingPipline implements PageModelPipeline<Zreading> {

    @Resource
    private ZreadingDao zreadingDao;

    @Override
    public void process(Zreading zreading, Task task) {
        Zreading z1 = zreadingDao.getByUrl(zreading.getUrl());
        if(z1 == null){
            // 调用Mybatis Dao保存结果
            zreadingDao.save(zreading);
        }else{

        }
    }
}
