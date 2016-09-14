package cn.xiaoyu.ssm.service;

import cn.xiaoyu.ssm.domain.User;

import java.util.List;

/**
 * @author 章小雨
 * @date 2016年3月15日
 * @email roingeek@qq.com
 */
public interface UserService {

	User getUserById(int id);

	List<User> getUserByName(String username);

	User getUserByEmail(String email);

	void saveUser(User user);

	void updateUser(User user);

	void deleteUser(int id);
	
	List<User> getAllUsers(int index,int size);
}
