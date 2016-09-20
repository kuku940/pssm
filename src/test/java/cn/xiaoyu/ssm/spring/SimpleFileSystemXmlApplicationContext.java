package cn.xiaoyu.ssm.spring;

import cn.xiaoyu.ssm.dao.ZreadingDao;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.w3c.dom.Element;

/**
 * Created by roin_zhang on 2016/9/19.
 *
 * Ioc容器初始化
 * ------------------------------------
 * @see FileSystemXmlApplicationContext#FileSystemXmlApplicationContext(String[], boolean, ApplicationContext)
 * 1、继承父类的super(parent),以及设置setConfigLocations方法的实现，用于载入xml
 *  -> refresh(); //Ioc容器的refresh()过程
 *
 * @see AbstractApplicationContext#refresh()
 * 2、详细描述了整个ApplicationContext的初始化过程，比如BeanFactory的更新，MessageSource和PostProcessor的注册等
 *   对ApplicationContext进行初始化的模版或执行提纲，这个执行过程为Bean的生命周期管理提供了条件。
 **
 *  // 启动子类的refreshBeanFactory方法
 *  -> obtainFreshBeanFactory();
 *
 * @see DefaultBeanDefinitionDocumentReader#doRegisterBeanDefinitions(Element)
 * 3、解析配置文件 完成向IoC容器注册bean
 * -> processBeanDefinition()方法
 *
 * @see BeanDefinitionParserDelegate#parseCustomElement(Element, BeanDefinition)
 * 4、获取Bean的节点component-scan，获取对应的handler，然后解析所有的class文件，然后得到符合条件的Bean
 *
 * @see ComponentScanBeanDefinitionParser#parse(Element, ParserContext)
 * 5、解析包名，然后执行doScan()反法
 *  -> doScan()方法
 *
 * @see ClassPathBeanDefinitionScanner#doScan(String...)
 * 6、扫描包下面的符合条件的类
 *
 *  @see ClassPathScanningCandidateComponentProvider#findCandidateComponents(String)
 *      找出符合路径下的所有的class文件，然后遍历使用isCandidateComponent(metadataReader)这个排除exclude-filter下面的文件，并找出其他的所有的组件@Component
 *  然后将上面步骤的bean进行scope范围设置一些相关操作
 *  @see BeanDefinitionReaderUtils#registerBeanDefinition(BeanDefinitionHolder, BeanDefinitionRegistry); //在Ioc容器中建立BeanDefinition数据映射
 *
 *
 * 依赖注入
 * ---------------------------------
 * @see AbstractApplicationContext#refresh()
 *
 * -> finishBeanFactoryInitialization(beanFactory); //初始化一些单例模式的Bean
 *
 * @see AbstractBeanFactory#doGetBean(String, Class, Object[], boolean)
 * 1、实际触发依赖注入的地方
 *  这儿会调用的方法来创建一个Bean方法 - sharedInstance = getSingleton(beanName, new ObjectFactory<Object>(){}
 *      回调createBean(beanName, mbd, args); -> doCreateBean(beanName, mbdToUse, args);
 *
 *      初始化Bean对象
 *      @see SimpleInstantiationStrategy#instantiate(RootBeanDefinition, String, BeanFactory)
 *          2、BeanUtils.instantiateClass(constructorToUse); 获取构造方法，然后将bean初始化
 *
 *      populateBean(beanName, mbd, instanceWrapper); //给指定的Bean填充属性
 *      @see AbstractAutowireCapableBeanFactory#populateBean(String, RootBeanDefinition, BeanWrapper)
 *          -> ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName) 注入属性
 *              ->
 *
 */
public class SimpleFileSystemXmlApplicationContext {
    public static void main(String[] args) {

        ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:beans.xml");
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        ZreadingDao bean = ctx.getBean("zreadingDao",ZreadingDao.class);
        System.out.println(bean);
    }
}
