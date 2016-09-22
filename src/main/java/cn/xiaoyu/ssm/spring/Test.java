package cn.xiaoyu.ssm.spring;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Element;

/**
 * Created by roin_zhang on 2016/9/21.
 * Spring AOP的分析
 *
 * 1、解析bean，从配置文件的beans-autoproxy.xml的<aop:aspectj-autoproxy>进行解析
 *   如果不是bean标签，则会用不同的类解析。解析AOP的是：AopNamespaceHandler类
 *   @see DefaultBeanDefinitionDocumentReader#parseBeanDefinitions(Element, BeanDefinitionParserDelegate)
 *   先进行配置文件的解析 这儿会有两个方法：
 *      parseDefaultElement(ele, delegate);     -- 解析bean节点信息
 *      delegate.parseCustomElement(ele);       -- 解析非bean节点的信息
 *
 * 2、初始化bean，在refresh()方法finishBeanFactoryInitialization(beanFactory);
 *      对剩余的所有的非懒加载的单实例进行初始化
 *    @see AbstractBeanFactory#doGetBean(String, Class, Object[], boolean)
 *          L306 - return createBean(beanName, mbd, args); -- 这儿进行了bean的初始化操作
 *              @see AbstractAutowireCapableBeanFactory#doCreateBean(String, RootBeanDefinition, Object[])
 *                  L541 - populateBean(beanName, mbd, instanceWrapper); -- 初始化属性
 *                  L545 - exposedObject = initializeBean(beanName, exposedObject, mbd);
 *                      @see AbstractAutoProxyCreator#postProcessAfterInitialization    -- AOP构建代理的核心逻辑
 *
 *  
 * 2、aop如何工作
 *  AnnotationAwareAspectAutoProxyCreator 的 postProcessAfterInitialization
 *      具体实现是在其父类 AbstractAutoProxyCreator 中完成的
 *
 */
public class Test {
    public static void main(String[] arge){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-autoproxy.xml");
        TestBean bean = context.getBean("test", TestBean.class);

        bean.printTest();
    }
}
