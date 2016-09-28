# springmvc + spring + mybatis项目

## spring

### mvc
DisPatcherServlet核心类

### IOC和AOP
AbstractApplicationContext的refresh方法

### mybatis核心类
SqlSessionFactoryBean类，该类实现了三个接口：InitializingBean、FactoryBean、ApplicationListener

    InitializingBean接口：当bean初始化的时候，spring就会调用该接口的实现类的afterPropertiesSet方法，去实现当spring初始化该Bean 的时候所需要的逻辑
    FactoryBean接口：在调用getBean的时候会返回该工厂返回的实例对象，也就是再调一次getObject方法返回工厂的实例
    ApplicationListener接口：如果注册了该监听的话，那么就可以了监听到Spring的一些事件，然后做相应的处理
