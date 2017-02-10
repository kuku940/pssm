package cn.xiaoyu.ssm.dao;

import cn.xiaoyu.ssm.domain.Zreading;

/**
 * Created by xiaoyu on 2016/9/7.
 */
public interface ZreadingDao {
    /**
     * 保存对象到数据库中，然后返回数据库生成的id
     * @param zreading
     * @return
     */
    int save(Zreading zreading);

    Zreading getByUrl(String url);

    void update(Zreading zreading);
}
