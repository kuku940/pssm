package cn.xiaoyu.ssm.test;

import cn.xiaoyu.ssm.service.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

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

    @Test
    @Ignore
    public void testFactoryBean(){
        // 创建ioc配置文件的抽象资源，这个抽象资源包含了BeanDefinition的定义信息
        ClassPathResource resource = new ClassPathResource("beans.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
        UserService bean = factory.getBean("message", UserService.class);	//Message是自己写的测试类
        bean.getUserById(1);
    }
}
