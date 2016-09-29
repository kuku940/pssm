package cn.xiaoyu.ssm.classloader;

import org.omg.CORBA.portable.OutputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by roin_zhang on 2016/9/29.
 */
public class TestClassLoader {
    /**
     * 测试自己的类加载器
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new MyClassLoader("D:/sp");
        Class clazz = new MyClassLoader(classLoader,"D:/patch").loadClass("cn.xiaoyu.ssm.classloader.Student");

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

