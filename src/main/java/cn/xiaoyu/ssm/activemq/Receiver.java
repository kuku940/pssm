package cn.xiaoyu.ssm.activemq;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * 点对点消息模型消费者
 *
 * @author xiaoyu
 * @date 2017/3/30
 */
public class Receiver {

}

@Repository("queueReceiver1")
class QueueReceiver1 implements MessageListener {
    @Resource(name="mqBeanMessageConverter")
    private MessageConverter messageConverter;

    @Override
    public void onMessage(Message message) {
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage = (ObjectMessage)message;
            try{
                MqBean bean = (MqBean) messageConverter.fromMessage(objectMessage);
                System.out.println("QueueReceiver1接收到消息：" + bean.toString());
            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }
}

@Repository("queueReceiver2")
class QueueReceiver2 implements MessageListener {
    private static int i = 0;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            MqBean bean = (MqBean) objectMessage.getObject();
            System.out.println("QueueReceiver2接收到消息：" + bean.getName());
//            if((i++%2)==0){
//                throw new RuntimeException("接受异常！");
//            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

@Repository("topicReceiver1")
class TopicReceiver1 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("TopicReceiver1接收到消息:" + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

@Repository("topicReceiver2")
class TopicReceiver2 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("TopicReceiver2接收到消息:" + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}