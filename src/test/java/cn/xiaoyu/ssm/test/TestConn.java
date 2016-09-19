package cn.xiaoyu.ssm.test;

import cn.xiaoyu.ssm.dao.UserDao;
import cn.xiaoyu.ssm.dao.ZreadingDao;
import cn.xiaoyu.ssm.domain.User;
import cn.xiaoyu.ssm.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beans*.xml"})
public class TestConn {
    @Resource(name = "userService")
    private UserService userService;
    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private UserDao userDao;
    @Resource
    private ZreadingDao zreadingDao;

    @Test
    public void testSelect() throws SQLException {
        User user = userService.getUserById(1);
        System.out.println(user);

        Connection conn = sqlSessionFactory.openSession().getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from user";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String str = rs.getString(1);
            System.out.println(str);
        }
    }

    @Test
    public void testSelectAll(){
        List<User> list = userService.getAllUsers(1,2);
        for (User user : list) {
            System.out.println(user);
        }
    }

    @Test
    public void testGetUserDao(){
        User user = userDao.getUserById(1);
        System.out.println(user);
        System.out.println(zreadingDao);
    }
}
