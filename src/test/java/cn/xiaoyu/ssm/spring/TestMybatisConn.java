package cn.xiaoyu.ssm.spring;

import cn.xiaoyu.ssm.dao.UserDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2016/9/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beans*.xml"})
public class TestMybatisConn {
    @Autowired
    @Qualifier("sqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void getUserDao() throws Exception {
        SqlSession session = sqlSessionFactory.openSession();

        UserDao userDao = session.getMapper(UserDao.class);
        System.out.println(userDao.getUserById(1));
    }
}
