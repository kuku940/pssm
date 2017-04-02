package cn.xiaoyu.ssm.activemq;

import java.io.Serializable;

/**
 * 消息队列的Bean对象
 *
 * @author xiaoyu
 * @date 2017/3/30
 */
public class MqBean implements Serializable {
    private Integer age;
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
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
        return "MqBean{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
