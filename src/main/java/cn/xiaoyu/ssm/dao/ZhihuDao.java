package cn.xiaoyu.ssm.dao;

import cn.xiaoyu.ssm.domain.Zhihu;

/**
 * Created by xiaoyu on 2016/9/7.
 */
public interface ZhihuDao {
    /**
     * 保存对象到数据库中，然后返回数据库生成的id
     * @param zhihu
     * @return
     */
    int save(Zhihu zhihu);

    Zhihu getByUrl(String url);

    void update(Zhihu zhihu);
}
