package cn.xiaoyu.ssm.dao;

import cn.xiaoyu.ssm.domain.Music163;

/**
 * Created by xiaoyu on 2016/9/7.
 */
public interface Music163Dao {
    /**
     * 保存对象到数据库中，然后返回数据库生成的id
     * @param music163
     * @return
     */
    int save(Music163 music163);

    Music163 getByUrl(String url);

    void update(Music163 music163);
}
