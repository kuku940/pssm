package cn.xiaoyu.ssm.pipline;

import cn.xiaoyu.ssm.dao.Music163Dao;
import cn.xiaoyu.ssm.domain.Music163;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/7.
 */
@Component("music163Pipline")
public class Music163Pipline implements PageModelPipeline<Music163> {

    @Resource
    private Music163Dao music163Dao;

    @Override
    public void process(Music163 music, Task task) {
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
