package cn.xiaoyu.ssm.classloader;

import cn.xiaoyu.ssm.util.CommonUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by roin_zhang on 2016/9/29.
 * 实现自定义的类加载器
 * 这是扩展类自定义的类
 */
public class MyExtensionClassLoader extends ClassLoader {

    private String pathDir;
    private final String fileType = ".class";

    public MyExtensionClassLoader(String pathDir) {
        //让系统类加载器AppClassLoader 成为该类加载器的父加载器
        super();
        this.pathDir = pathDir;
    }

    public MyExtensionClassLoader(ClassLoader parent, String pathDir) {
        //指定该类加载器该父加载器
        super(parent);
        this.pathDir = pathDir;
    }

    /**
     * 打破双亲模式
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try{
            Class<?> c = findClass(name);
            return c;
        } catch (ClassNotFoundException e) {
            //获取该路径类失败
        }

        //这个使用这种方式调用，而不是采用super方式，防止直接调用ClassLoader的loadClass()方法
        return this.getParent().loadClass(name);
    }

    /**
     * @param name 类的二进制名称 cn.xiaoyu.test.Student
     * @return Class对象
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String filename = pathDir + "/" + name.replace('.', '/') + fileType;
            System.out.println("MyExtensionClassLoader:"+filename);

            byte[] bytes = getClassData(filename);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
        }
        return super.findClass(name);
    }

    private byte[] getClassData(String filename) throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        byte[] bytes = null;

        try {
            fis = new FileInputStream(filename);
            bos = new ByteArrayOutputStream();

            int b;
            byte[] buffer = new byte[256];
            while ((b = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, b);
            }
            bytes = bos.toByteArray();

        } finally {
            CommonUtil.close(fis);
            CommonUtil.close(bos);
        }
        return bytes;
    }
}
