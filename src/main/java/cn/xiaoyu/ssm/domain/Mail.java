package cn.xiaoyu.ssm.domain;

import java.io.Serializable;

/**
 * @author 章小雨
 * @date 2016年3月24日
 * @email roingeek@qq.com
 */
public class Mail implements Serializable{
	
	public static final String ENCODEING = "UTF-8";
	
	private String host = "smtp.163.com"; 		// 服务器地址
	private String sender = "zxy9312@163.com"; 		// 发件人邮箱
	private String receiver;	// 收件人邮箱
	private String nickname = "Roin";	// 昵称
	private String username = "zxy9312@163.com";	// 账号
	private String password = "5GVO73ZS4NJD6M8"; 	// 密码
	private String subject;		// 主题
	private String message;		// 信息
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
