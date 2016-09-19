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
        // 获取bean.xml以及classLoader加载器
        ClassPathResource resource = new ClassPathResource("beans.xml");

        // 创建一个BeanFactory
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 创建一个载入BeanDefinition的读取器，这里使用XmlBeanDefinitionReader来载入XML文件形式的BeanDefinition,通过一个回调配置给BeanFactory
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        // 从定义好的资源位置读入配置信息，具体的解析过程由XmlBeanDefinitionReader来完成。
        // 完成整个载入和注册Bean定义之后，需要的Ioc容器就建立起来了。这个时候我们就可以直接使用Ioc容器了。
        /**
         * 这儿关联几个比较关键的点：
         *  1、XmlBeanDefinitionReader.registerBeanDefinitions()中documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
         *
         *
         *  2、DefaultBeanDefinitionDocumentReader.doRegisterBeanDefinitions()中documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
         *      这儿进行beans.xml文件的解析，这儿跟踪解析到context:component-scan节点的时候，进入delegate.parseCustomElement(ele);代码，
         *      接着将根据namespaceUri获取NamespaceHandler处理器，调用parse()方法
         *
         *  3、进行解析ComponentScanBeanDefinitionParser.parse()方法
         *      ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
         *          获取scanner扫描器，然后将exclude-filter的bean排除掉
         *      Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
         *          进行扫描Bean，获取Bean集合
         *
         *  4、ClassPathBeanDefinitionScanner.doScan()中的Set<BeanDefinition> candidates = findCandidateComponents(basePackage);代码
         *      这儿会将所有的符合条件的包都过滤出来然后对class类进行遍历找到对应的符合条件的类，返回一个集合给上面3使用
         */
        reader.loadBeanDefinitions(resource);

        //经过上述处理后，facatory中就有了BeanDefinitionMap和BeanDefinitionNames两个属性，这儿包含了有的所有的bean节点
        UserService bean = factory.getBean("userService", UserService.class);
        System.out.println(bean);
    }
}
