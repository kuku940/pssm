package cn.xiaoyu.ssm.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 发布/订阅消息模型生产者
 *
 * @author xiaoyu
 * @date 2017/3/30
 */
@Component("topicSender")
public class TopicSender {
    @Resource(name = "jmsTopicTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 发送一条消息到指定的队列（目标）
     *
     * @param topicName 队列名称
     * @param message   消息内容
     */
    public void publish(String topicName, final Object message) {
        jmsTemplate.send(topicName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return jmsTemplate.getMessageConverter().toMessage(message, session);
            }
        });
    }
}