package cn.xiaoyu.ssm.spring;

import cn.xiaoyu.ssm.service.UserService;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by roin_zhang on 2016/9/18.
 * 最原始的Ioc容器的使用
 */
public class SimpleBeanFactory {
    public static void main(String[] args) {
        // 创建ioc配置文件的抽象资源，这个抽象资源包含了BeanDefinition的定义信息
        ClassPathResource resource = new ClassPathResource("beans.xml");

        // 创建一个BeanFactory
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 创建一个载入BeanDefinition的读取器，这里使用XmlBeanDefinitionReader来载入XML文件形式的BeanDefinition,通过一个回调配置给BeanFactory
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        // 从定义好的资源位置读入配置信息，具体的解析过程由XmlBeanDefinitionReader来完成。
        // 完成整个载入和注册Bean定义之后，需要的Ioc容器就建立起来了。
        // 这个时候我们就可以直接使用Ioc容器了。
        reader.loadBeanDefinitions(resource);

        UserService bean = factory.getBean("userService", UserService.class);
        System.out.println(bean);
    }
}
