package cn.xiaoyu.ssm.spring;

import cn.xiaoyu.ssm.dao.UserDao;
import cn.xiaoyu.ssm.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Roin_zhang
 * @date 2018/4/2 11:46
 */
public class TestApplicationContext {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:beans*.xml");
        UserDao userDao = ac.getBean(UserDao.class);
        System.out.println(userDao);
    }
}
