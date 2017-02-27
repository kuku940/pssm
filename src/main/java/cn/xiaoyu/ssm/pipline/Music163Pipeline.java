package cn.xiaoyu.ssm.pipline;

import cn.xiaoyu.ssm.dao.Music163Dao;
import cn.xiaoyu.ssm.domain.Music163;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@Component("music163Pipeline")
public class Music163Pipeline implements Pipeline {

    @Resource
    private Music163Dao music163Dao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Music163 music = resultItems.get("cn.xiaoyu.ssm.domain.Music163");
        // 只做增量更新，如果该对应存在的话，就不需要将这条数据更新
        Music163 m1 = music163Dao.getByUrl(music.getUrl());
        if(m1 == null){
            // 调用Mybatis Dao保存结果
            music163Dao.save(m1);
        }else{
            music163Dao.update(music);
        }
    }
}
