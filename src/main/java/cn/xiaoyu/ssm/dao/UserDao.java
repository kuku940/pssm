package cn.xiaoyu.ssm.dao;

import cn.xiaoyu.ssm.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 章小雨
 * @date 2016年3月15日
 * @email roingeek@qq.com
 */
public interface UserDao {
	/**
	 * 在以下的两种情况下，只能够使用xml文件进行配置而不能够使用注释完成
	 * 1、条件不确定的时候 - getList
	 * 2、增加对象时，需要返回自增id - saveUserReturnID
	 * 3、在一个Mapper接口中，出现多个select查询（>=3个），且每个查询都需要写相同的返回@Results内容，这儿为了整洁可以将方法教给xml文件处理
	 */

	// 根据ID来查询用户
	User getUserById(int id);

	// 根据名称来模糊查询
	List<User> getUserByName(String username);

	// 根据邮箱来查找用户
	User getUserByEmail(String email);

	// 保存用户
//    @Insert("insert into user(username,password,email,birth) values (#{username},#{password},#{email},#{birth})")
	void saveUser(User user);

	// 保存用户 并且返回id值，这个是注释做不到的,必须要使用xml文件配置
	int saveUserReturnID(User user);

	@Update("update user set username=#{username},email=#{email},password=#{password},birth=#{birth} where id=#{id}")
	void updateUser(User user);

	@Delete("delete from user where id=#{id}")
	void deleteUser(int id);
	
	@Select("select * from user limit #{offset},#{pageSize}")
	List<User> getListForPage(@Param("offset") int offset,@Param("pageSize") int pageSize);

	/**
	 * 进行模糊查询，条件可能为空
	 */
	List<User> getList(@Param("username") String username,@Param("email")String email);
}
