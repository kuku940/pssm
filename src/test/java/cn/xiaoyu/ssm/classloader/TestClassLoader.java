package cn.xiaoyu.ssm.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * Created by roin_zhang on 2016/9/29.
 */
public class TestClassLoader {
    /**
     * 测试自己的类加载器
     * @param args
     */
    public static void main(String[] args) throws Exception{
        /**
         * 因为jar类加载器加载是从上往下加载的，使用是从下往上使用的，所以这儿优先使用父类的类
         * 故这儿如果sp下面有的优先加载sp下面的，如果没有则加载子类的
         *
         * 这儿没有双亲
         * BootStrapClassLoader - null,启动类加载器       jre/lib/rt.jar
         * ExtClassLoader - 扩展类加载器            jre/lib/ext/*.jar
         * AppClassLoader - 系统类加载器          classpath指定的所有的jar和目录
         * 自定义类加载器                                 自己指定的特殊目录
         *
         * 这儿需要在D:\jar\ext\cn\xiaoyu\ssm\classloader文件夹下面有Student类
         *
         */
        Class clazz = getClazz("cn.xiaoyu.ssm.classloader.Student");

        Object obj = clazz.newInstance();
        System.out.println(obj);

        try{
            //通过反射调用print方法
            Method method = clazz.getMethod("print");
            method.invoke(obj);
        }catch (NoSuchMethodException e){
            System.out.println("Ex类未找到！");
        }
    }

    public static Class getClazz(String name) throws ClassNotFoundException {
        /**
         * 使用覆写父类的loadClass方法，打破了双亲委托机制，实现了自己优先加载，然后再加载父类的class
         *
         * 这种类加载的顺序：优先加载Ext下面的扩展类，然后在加载Ext下面的类，然后加载patch下面的类
         * 最后，按照委托机制，从上向下加载！
         *
         * 如果不想打破双亲委托机制的话，可以直接从上向下加载这样就可以将优先加载父类，然后在加载子类，这样也可以
         * 实现金蝶的补丁和私包的类似效果
         */
        ClassLoader classLoader = new MyClassLoader("D:/jar/patch");
        ClassLoader extensionClassLoader = new MyExtensionClassLoader(classLoader,"D:/jar/ext");
        try{
            //优先加载扩展类，如果没有则加载自己
           return extensionClassLoader.loadClass(name+"Ex");
        }catch (Exception e){

        }
        return extensionClassLoader.loadClass(name);
    }

    /**
     * 加密文件算法
     * @param ips 输入流
     * @param ops 输出流
     * @throws IOException
     */
    public static void cypher(InputStream ips, OutputStream ops) throws IOException {
        int b = -1;
        /*
            将读出来的字节码加密后，写入ops中，加密算法是，对这个文件进行“异或”运算
            异或运算 既可以是加密算法，也可以是解密算法
         */
        while((b=ips.read()) != -1){
            ops.write(b^0xff);
        }
    }
}

