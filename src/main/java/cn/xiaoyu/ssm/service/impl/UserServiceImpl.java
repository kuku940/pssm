package cn.xiaoyu.ssm.service.impl;

import cn.xiaoyu.ssm.dao.UserDao;
import cn.xiaoyu.ssm.domain.User;
import cn.xiaoyu.ssm.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 章小雨
 * @date 2016年3月15日
 * @email roingeek@qq.com
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService{
	
	@Resource
	private UserDao userDao;
	
	@Override
	public User getUserById(int id) {
		return this.userDao.getUserById(id);
	}

	@Override
	public List<User> getUserByName(String username) {
		return this.userDao.getUserByName(username);
	}

	@Override
	public User getUserByEmail(String email) {
		return this.userDao.getUserByEmail(email);
	}

	public void saveUser(User user) {
		this.userDao.saveUser(user);
	}

	public void updateUser(User user) {
		this.userDao.updateUser(user); 
	}

	public void deleteUser(int id) {
		this.userDao.deleteUser(id);
	}

	public List<User> getAllUsers(int pageIndex, int pageSize) {
		int offset = (pageIndex - 1) * pageSize;
		return this.userDao.getListForPage(offset, pageSize);
	}
	
}
