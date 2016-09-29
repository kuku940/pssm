package cn.xiaoyu.ssm.classloader;

import cn.xiaoyu.ssm.util.CommonUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * Created by roin_zhang on 2016/9/29.
 * 实现自定义的类加载器
 * <p>
 * -要实现用户自己的类加载器，只需要扩展java.lang.ClassLoader类，然后覆盖它的fingClass(String name)方法即可，
 * 该方法根据参数指定类的名字，返回对应的class对象的引用
 *
 * @see ClassLoader#loadClass(String)
 * @see ClassLoader#findClass(String)
 * 用指定的二进制名称，查找类，也就是class文件
 * @see ClassLoader#defineClass(String, byte[], int, int)
 * 把字节码文件转换成class类实例
 */
public class MyClassLoader extends ClassLoader {

    private String pathDir;
    private final String fileType = ".class";

    public MyClassLoader(String pathDir) {
        //让系统类加载器成为该类加载器的父加载器
        super();
        this.pathDir = pathDir;
    }

    public MyClassLoader(ClassLoader parent, String pathDir) {
        //指定该类加载器该父加载器
        super(parent);
        this.pathDir = pathDir;
    }

    /**
     * @param name 类的二进制名称 cn.xiaoyu.test.Student
     * @return Class对象
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String filename = pathDir + "/" + name.replace('.','/') + fileType;
        System.out.println(filename);
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(filename);
            bos = new ByteArrayOutputStream();

            int b = -1;
            byte[] buffer = new byte[256];
            while ((b = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, b);
            }

            byte[] bytes = bos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CommonUtil.close(fis);
            CommonUtil.close(bos);
        }
        return super.findClass(name);
    }
}
