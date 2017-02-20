package cn.xiaoyu.ssm.service.impl;

import cn.xiaoyu.ssm.dao.ZhihuDao;
import cn.xiaoyu.ssm.domain.Zhihu;
import cn.xiaoyu.ssm.service.ZhihuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 章小雨
 * @date 2016年3月15日
 * @email roingeek@qq.com
 */
@Service(value = "zhihuService")
public class ZhihuServiceImpl implements ZhihuService{
	
	@Resource
	private ZhihuDao zhihuDao;

	public PageInfo<Zhihu> getAllForList(int pageIndex,int pageSize) {
		PageHelper.startPage(pageIndex,pageSize);
		PageInfo<Zhihu> info = new PageInfo<Zhihu>(zhihuDao.getAllForList());
		return info;
	}
	
}
