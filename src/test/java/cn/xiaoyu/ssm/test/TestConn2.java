package cn.xiaoyu.ssm.test;

import cn.xiaoyu.ssm.service.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2016/9/17.
 */
public class TestConn2 {
    ApplicationContext ctx;
    @Before
    public void init(){
        ctx = new ClassPathXmlApplicationContext("beans.xml");
//        ctx = new FileSystemXmlApplicationContext("classpath*:beans.xml");
    }

    @Test
    @Ignore
    public void openSession(){
        System.out.println(ctx.getBean("sqlSessionFactory"));
        UserService bean = (UserService) ctx.getBean("userService");
    }

}
