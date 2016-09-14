package cn.xiaoyu.ssm.util;

import cn.xiaoyu.ssm.domain.Mail;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Calendar;
import java.util.Properties;

/**
 * @author 章小雨
 * @date 2016年3月24日
 * @email roingeek@qq.com
 */
public class MailUtil { 
	/**
	 * 使用JavaMail发送一封简单的邮件
	 * 1.创建包含邮件服务器的网络连接信息的Session对象
	 * 2.创建代表邮件内容的Message对象
	 * 3.创建Transport对象、连接服务器、发送Message、关闭连接
	 * @param mail
	 * @return
	 */	
	public static boolean send(final Mail mail){
		try {
			Properties props = new Properties();
			props.put("mail.host", mail.getHost());
			props.put("mail.transport.protocol", "stmp");
			props.put("mail.smtp.auth", "true");
			
			// 创建一个Authenticator对象
			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mail.getUsername(),mail.getPassword());
				}
			};
			
			// 创建Session对象
			Session session = Session.getInstance(props,auth);
			session.setDebug(true); //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
						
			// 创建邮件
			MimeMessage message = new MimeMessage(session);
			String nickname = MimeUtility.encodeText(mail.getNickname());
			message.setFrom(new InternetAddress(nickname+"<"+mail.getUsername()+">"));
			/** 
			 * 	Message.RecipientType.TO	消息接受者
			 *	Message.RecipientType.CC	消息抄送者
			 *	Message.RecipientType.BCC	匿名抄送接收者 	
			 **/
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(mail.getReceiver()));
			
			message.setSubject(mail.getSubject());
			message.setSentDate(Calendar.getInstance().getTime());
			message.setContent(mail.getMessage(),"text/html;charset=UTF-8");
			
			Transport.send(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
    public static boolean sendSingleMail(Mail mail) {  
        // 发送email  
        HtmlEmail email = new HtmlEmail();  
        try {  
            // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"  
            email.setHostName(mail.getHost());  
            // 字符编码集的设置  
            email.setCharset(Mail.ENCODEING);  
            // 收件人的邮箱  
            email.addTo(mail.getReceiver());  
            // 发送人的邮箱  
            email.setFrom(mail.getSender(), mail.getNickname());  
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码  
            email.setAuthentication(mail.getUsername(), mail.getPassword());  
            // 要发送的邮件主题  
            email.setSubject(mail.getSubject());  
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签  
            email.setMsg(mail.getMessage());  
            // 发送  
            email.send();    
            return true;  
        } catch (EmailException e) {   
            return false;  
        }  
    } 
}