package cn.xiaoyu.ssm.controller;

import cn.xiaoyu.ssm.domain.Mail;
import cn.xiaoyu.ssm.domain.User;
import cn.xiaoyu.ssm.service.UserService;
import cn.xiaoyu.ssm.util.Constant;
import cn.xiaoyu.ssm.util.MD5Util;
import cn.xiaoyu.ssm.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author 章小雨
 * @date 2016年3月15日
 * @email roingeek@qq.com
 */

@Controller
@SessionAttributes("loginUser")
@RequestMapping("/user")
public class UserController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource(name="userService")
	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value="/regist",method=RequestMethod.GET)
	public String regist(Model model){
		model.addAttribute("user", new User());
		return "user/regist";
	}
	
	@RequestMapping(value="/regist",method=RequestMethod.POST)
	public String regist(User user,String confirmpass,Model model){
		if(userService.getUserByEmail(user.getEmail()) != null){
			model.addAttribute("error","邮箱已经被注册！");
			model.addAttribute("user", user);
			return "user/regist";
		}			
		
		// 判断两次输入的密码是否相等
		if(user.getPassword().equals(confirmpass)){
			user.setPassword(MD5Util.encrypt(user.getPassword()));
			userService.saveUser(user);
			
			// 发送一封邮件 来确定注册信息
			final Mail mail = new Mail();
			mail.setReceiver(user.getEmail());
			mail.setSubject("邮箱消息验证");
			String message = user.getUsername()+"您好：<br/>&nbsp;&nbsp;欢迎你注册我们的网站，点击<a href='http://localhost/ssm/user/reg?email="+user.getEmail()+"&flag="+MD5Util.encrypt(user.getEmail()+"salt2016")+"'>这儿</a>验证邮箱!";
			mail.setMessage(message);
			MailUtil.send(mail);
			logger.info("向新注册用户{}发送验证邮件",user.getEmail());
			logger.info("发送给注册用户{}验证邮件加入队列成功！",user.getEmail());
			
			model.addAttribute("user", user);
			return "user/login";
		}else{
			model.addAttribute("error", "两次输入密码不一致");
			model.addAttribute("user", user);
			return "user/regist";
		}
	}
	/**
	 * 邮箱验证
	 * @param model
	 * @return
	 * 
	 * produces="text/html;charset=UTF-8" 防止返回的json中文乱码
	 */
	@ResponseBody
	@RequestMapping(value="/reg",method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	public String verifyMail(HttpServletRequest request){
		String email = request.getParameter("email");
		String flag = request.getParameter("flag");
		
		if(flag.equals(MD5Util.encrypt(email+"salt2016"))){
			// 更改用户验证状态
			
			return "恭喜你验证邮箱成功！";
		}else{
			return "验证失败，请重试！";
		}
	}
	
	@RequestMapping(value={"/","/login"},method=RequestMethod.GET)
	public String login(Model model){
		model.addAttribute("user",new User());
		return "user/login";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(User user,String captcha,HttpServletRequest request,Model model){
		String sessionCaptcha = (String) request.getSession().getAttribute(Constant.CAPTCHA);
		if(captcha == null || !(captcha.equalsIgnoreCase(sessionCaptcha))){
			model.addAttribute("user",user);
			model.addAttribute("error","验证码不正确！");
			return "user/login";
		}
		User u = userService.getUserByEmail(user.getEmail());
		if(u != null && u.getPassword().equals(user.getPassword())){
			logger.info("用户{}于{}登陆成功",user.getEmail(),new Date());
			return "redirect:/user/list";
		}else{
			model.addAttribute("user",user);
			model.addAttribute("error","用户名或密码不正确！");
			return "user/login";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Object list(@RequestParam(defaultValue="1")int pageIndex,@RequestParam(defaultValue="10")int pageSize){		
		List<User> users = userService.getAllUsers(pageIndex,pageSize);
		return users;
	}
}