package cn.xiaoyu.ssm.serialzable;

import java.io.Serializable;

/**
 * Created by roin_zhang on 2016/10/10.
 */
public class UserInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
