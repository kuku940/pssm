package cn.xiaoyu.ssm.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by roin_zhang on 2016/9/21.
 * Spring AOP的分析
 *
 * 1、可以先从配置文件的beans-autoproxy.xml的<aop:aspectj-autoproxy>进行解析
 *   如果不是bean标签，则会用不同的类解析。解析AOP的是：AopNamespaceHandler类
 *
 * 2、
 */
public class Test {
    public static void main(String[] arge){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-autoproxy.xml");
        TestBean bean = context.getBean("test", TestBean.class);

        bean.printTest();
    }
}
