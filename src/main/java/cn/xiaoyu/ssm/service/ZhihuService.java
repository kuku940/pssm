package cn.xiaoyu.ssm.service;

import cn.xiaoyu.ssm.domain.Zhihu;
import com.github.pagehelper.PageInfo;

/**
 * @author 章小雨
 * @date 2016年3月15日
 * @email roingeek@qq.com
 */
public interface ZhihuService {

	PageInfo<Zhihu> getAllForList(int pageIndex, int pageSize);

}
