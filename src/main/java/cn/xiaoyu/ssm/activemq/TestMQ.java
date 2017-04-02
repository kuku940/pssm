package cn.xiaoyu.ssm.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 消息队列测试文件
 *
 * @author xiaoyu
 * @date 2017/3/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beans*.xml"})
public class TestMQ {
    @Resource(name="queueSender")
    private QueueSender queueSender;
    @Resource(name="topicSender")
    private TopicSender topicSender;

    @Test
    public void testSendQueue() {
        MqBean bean = new MqBean();
        for (int i = 0; i < 10; i++) {
            bean.setName("阿里云"+i);
            bean.setAge(i);
//            queueSender.send("test.queue",bean);
            queueSender.send2("test.queue",bean);
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPulishTopic(){
        String message = "发布订阅消息模型_";
        for (int i = 0; i < 5; i++) {
            topicSender.publish("test.topic",message+i);
        }
    }
}
