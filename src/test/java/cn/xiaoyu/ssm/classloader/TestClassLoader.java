package cn.xiaoyu.ssm.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by roin_zhang on 2016/9/29.
 */
public class TestClassLoader {
    /**
     * 测试自己的类加载器
     * @param args
     */
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        /**
         * 因为jar类加载器加载是从上往下加载的，使用是从下往上使用的，所以这儿优先使用父类的类
         * 故这儿如果sp下面有的优先加载sp下面的，如果没有则加载子类的
         *
         * 这儿没有双亲
         * BootStrapClassLoader - null,启动类加载器
         * ExtensionClassLoader - 扩展类加载器
         * ApplicationClassLoader - 系统类加载器
         * 自定义类加载器
         *
         * 这儿需要在D:\jar\ext\cn\xiaoyu\ssm\classloader文件夹下面有Student类
         */
        ClassLoader classLoader = new MyClassLoader("D:/jar/sp");
        Class clazz = new MyClassLoader(classLoader,"D:/jar/ext").loadClass("cn.xiaoyu.ssm.classloader.Student");

        Object obj = clazz.newInstance();
        System.out.println(obj);
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

