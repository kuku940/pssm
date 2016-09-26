package cn.xiaoyu.ssm.spring;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.weaver.tools.JoinPointMatch;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.annotation.*;
import org.springframework.aop.config.AopNamespaceHandler;
import org.springframework.aop.framework.*;
import org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by roin_zhang on 2016/9/21.
 *
 * 《Spring AOP的分析》
 *
 * 1、解析bean，从配置文件的beans-autoproxy.xml的<aop:aspectj-autoproxy>进行解析
 *   如果不是bean标签，则会用不同的类解析。解析AOP的是：AopNamespaceHandler类
 *   @see DefaultBeanDefinitionDocumentReader#parseBeanDefinitions(Element, BeanDefinitionParserDelegate)
 *   先进行配置文件的解析 这儿会有两个方法：
 *      parseDefaultElement(ele, delegate);     -- 解析bean节点信息
 *      delegate.parseCustomElement(ele);       -- 解析非bean节点的信息
 *
 *      这儿在解析非bean节点的时候，会调用aop的handler:AopNamespaceHandler
 *      @see AopNamespaceHandler#init() 注册对应的解析器，找到对应的解析器
 *
 * 2、初始化bean，在refresh()方法finishBeanFactoryInitialization(beanFactory);
 *      对剩余的所有的非懒加载的单实例进行初始化
 *    @see AbstractBeanFactory#doGetBean(String, Class, Object[], boolean)
 *          L306 - return createBean(beanName, mbd, args); -- 这儿进行了bean的初始化操作
 *              @see AbstractAutowireCapableBeanFactory#doCreateBean(String, RootBeanDefinition, Object[])
 *                  L541 - populateBean(beanName, mbd, instanceWrapper); -- 初始化属性
 *                  L545 - exposedObject = initializeBean(beanName, exposedObject, mbd);
 *                      @see AbstractAutoProxyCreator#postProcessAfterInitialization    -- AOP构建代理的核心逻辑
 *                      @see AbstractAutoProxyCreator#wrapIfNecessary(Object, String, Object)
 *
 * 3、设计的基石
 *  Advice - 通知 定义连接点做什么，为切面增强提供了织入的接口
 *      在Spring AOP围绕方法调用而注入的切面的行为
 *  @see Advice 可以查看这个接口的继承关系
 *  @see MethodInterceptor#invoke(MethodInvocation)
 *  @see MethodBeforeAdviceInterceptor#invoke(MethodInvocation) 调用子类的before()方法
 *  @see AspectJMethodBeforeAdvice#before(Method, Object[], Object)
 *      @see AbstractAspectJAdvice#invokeAdviceMethod(JoinPointMatch, Object, Throwable)
 *      @see AbstractAspectJAdvice#invokeAdviceMethodWithGivenArgs(Object[]) -- aspectJAdviceMethod.invoke()对于前置增强的方法，在这里实行了调用
 *
 *  其中其他的后置增强逻辑一样
 *  @see AspectJAfterReturningAdvice#afterReturning(Object, Method, Object[], Object)
 *
 *  Pointcut - 切点 决定 Advice 增强作用于哪个连接点，
 *      也就是说通过 Pointcut 来定义需要增强的方法集合，而这些集合的选取可以通过一定的规则来完成
 *  @see Pointcut 需要返回一个MethodMatcher,由这个 MethodMatcher 来判断是否需要对当前方法调用进行增强或者配置应用
 *  @see MethodMatcher#matches(Method, Class)
 *      @see NameMatchMethodPointcut#matches(Method, Class) -- 方法名相同或者方法名匹配
 *
 *  Advisor 通知器 用一个对象将对目标方法的切面增强设计（Advice）和关注点的设计（Pointcut）结合起来
 *  @see Advisor
 *  @see DefaultPointcutAdvisor
 *      这个类主要职责是 Pointcut 的设置或者获取，在 DefaultPointcutAdvisor 中，Pointcut 默认被设置为 Pointcut.True，
 *      这个 Pointcut.True 接口被定义为 Pointcut True = TruePointcut.INSTANCE。而关于 Advice 部分的设置与获取是由
 *      其父类 AbstractGenericPointcutAdvisor 来完成的。
 *
 * 4、指定bean的增强方法
 *      而对于指定 bean 的增强方法的获取，一般包含获取所有增强以及寻找所有增强中适合于 bean 的增强并应用这两个步骤。
 *  @see AnnotationAwareAspectJAutoProxyCreator#getAdvicesAndAdvisorsForBean(Class, String, TargetSource)
 *      @see AnnotationAwareAspectJAutoProxyCreator#findEligibleAdvisors(Class, String)
 *          -- findCandidateAdvisors() - 获取所有的增强，通知器的解析
 *          -- findAdvisorsThatCanApply() - 获取所有可以应用的增强
 *
 *          通知器的解析： -- findCandidateAdvisors()
 *              @see AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors()
 *                  - 继承父类规则                    super.findCandidateAdvisors();
 *                  - 构建beanFactory所有bean         this.aspectJAdvisorsBuilder.buildAspectJAdvisors()
 *
 *                  @see BeanFactoryAspectJAdvisorsBuilder#buildAspectJAdvisors()
 *                      首先，获取所有在 beanFacotry 中注册的 Bean 都会被提取出来。
 *                      然后遍历所有 beanName，并找出声明 AspectJ 注释的类，进一步处理。
 *                      最后，将将结果加入缓存。
 *
 *                  L127 advisors.addAll(this.advisorFactory.getAdvisors(factory));
 *                  获取通知器：
 *                      @see ReflectiveAspectJAdvisorFactory#getAdvisors(MetadataAwareAspectInstanceFactory)
 *                      函数中首先完成了对增强器的获取，包括获取注解以及根据注解生成增强的步骤，
 *                      然后考虑到在配置中可能会将增强配置成延迟初始化，那么需要在首位加入同步实例化增强以保证增强使用之前的实例化，
 *                      最后对 DeclareParents 注解的获取
 *
 *                      1.切点信息的获取。所谓获取切点信息就是指定注解的表达式信息的获取，如@Before（"test()"）。
 *                          @see ReflectiveAspectJAdvisorFactory#getPointcut(Method, Class)
 *                          @see AbstractAspectJAdvisorFactory#findAspectJAnnotationOnMethod(Method)
 *                      2.根据切点信息生成增强。所有的增强都由 Advisor 的实现类 InstantiationModelAwarePointcutAdvisorImpl 统一封装的。
 *                          @see InstantiationModelAwarePointcutAdvisorImpl
 *                          @see InstantiationModelAwarePointcutAdvisorImpl#instantiateAdvice(AspectJExpressionPointcut)
 *                          @see ReflectiveAspectJAdvisorFactory#getAdvice(Method, AspectJExpressionPointcut, MetadataAwareAspectInstanceFactory, int, String)
 *                              Spring 会根据不同的注解生成不同的增强器
 *
 *              寻找匹配的通知 -- findAdvisorsThatCanApply()
 *                  @see AbstractAdvisorAutoProxyCreator#findAdvisorsThatCanApply(List, Class, String)
 *                      @see AopUtils#findAdvisorsThatCanApply(List, Class)
 *                          --  函数的主要功能是寻找所有增强器中适合于当前 class 的增强器
 *  5、创建代理
 *      获取了所有对应bean的增强器后，便可以进行代理的创建
 *      @see AbstractAutoProxyCreator#createProxy(Class, String, Object[], TargetSource)
 *          @see AbstractAutoProxyCreator#buildAdvisors(String, Object[])  -- 封装逻辑
 *              @see DefaultAdvisorAdapterRegistry#wrap(Object)
 *
 *  6、代理生成
 *      创建代理
 *      @see ProxyCreatorSupport#createAopProxy()
 *          @see DefaultAopProxyFactory#createAopProxy(AdvisedSupport) -- 这儿进行了代理的创建
 *
 *          包含JDK动态代理和CGlib字节码生成代理
 *
 *          JDK动态代理
 *          @see JdkDynamicAopProxy#getProxy(ClassLoader) - 获取生成的代理对象
 *          @see JdkDynamicAopProxy#invoke(Object, Method, Object[])
 *          @see ReflectiveMethodInvocation#proceed() - 创建一个拦截器链
 *
 *          CGlib字节码生成代理
 *          @see CglibAopProxy#getProxy(ClassLoader) - 创建 Spring 的 Enhancer 过程
 *          @see CglibAopProxy#getCallbacks(Class) - 设置拦截器链
 *
 */
public class Test {
    public static void main(String[] arge){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-autoproxy.xml");
        TestBean bean = context.getBean("test", TestBean.class);

        bean.printTest();
    }
}
