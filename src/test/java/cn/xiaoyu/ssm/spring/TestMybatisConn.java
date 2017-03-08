package cn.xiaoyu.ssm.spring;

import cn.xiaoyu.ssm.dao.UserDao;
import cn.xiaoyu.ssm.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/9/28.
 * 两个核心的类：SqlSessionFactoryBean和MapperFactoryBean
 *
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

    @Test
    public void getUserDaoWithoutSpring() throws IOException {
        String resource = "mapper/UserMapper.xml";
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            User user = session.selectOne("cn.xiaoyu.ssm.dao.UserDao.getUserById",1);
            System.out.println(user);

            UserDao userDao = session.getMapper(UserDao.class);
            user = userDao.getUserById(1);
            System.out.println(user);
        }finally {
            session.close();
        }
    }
}
