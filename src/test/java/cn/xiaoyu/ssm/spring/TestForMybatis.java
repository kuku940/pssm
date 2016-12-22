package cn.xiaoyu.ssm.spring;

import cn.xiaoyu.ssm.dao.UserDao;
import cn.xiaoyu.ssm.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * Created by roin_zhang on 2016/12/21.
 */
public class TestForMybatis {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream(TestForMybatis.class.getClassLoader(),"mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            User user = null;
            UserDao userDao = session.getMapper(UserDao.class);
            user = userDao.getUserById(1);
            System.out.println(user);
        }finally {
            session.close();
        }

    }
}
