package cn.xiaoyu.ssm.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 点对点消息模型生产者
 *
 * @author xiaoyu
 * @date 2017/3/30
 */
@Component("queueSender")
public class QueueSender {

    @Resource(name = "jmsQueueTemplate")
    private JmsTemplate jmsQueueTemplate;

    /**
     * 发送消息到指定的队列
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */

    public void send(String queueName, final Object message) {
        jmsQueueTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return jmsQueueTemplate.getMessageConverter().toMessage(message, session);
//                return session.createObjectMessage(message);
            }
        });
    }


    /**
     * 发送消息到指定的队列
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */

    public void send2(String queueName, final Object message) {
        //使用MessageConverter情况
        jmsQueueTemplate.convertAndSend(queueName,message);
    }
}
