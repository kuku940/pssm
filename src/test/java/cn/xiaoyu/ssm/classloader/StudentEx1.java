package cn.xiaoyu.ssm.classloader;

/**
 * Created by Administrator on 2016/9/29.
 */
public class StudentEx1 extends Student1{
    public String toString(){
        return "This is StudentEx class!";
    }
    public void print(){
        System.out.println("this is a extension class!");
    }
}
