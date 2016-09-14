package cn.xiaoyu.ssm.controller;

import cn.xiaoyu.ssm.util.CaptchaUtil;
import cn.xiaoyu.ssm.util.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
@RequestMapping
public class RandomCodeImageController {
	
	@RequestMapping(value="/captcha.gif",method=RequestMethod.GET)
	public void regist(HttpServletRequest request,HttpServletResponse response){
		// 不设置图片缓存
		response.reset();
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setDateHeader("expires", 0);
        response.setContentType("image/gif");
        
        String code = CaptchaUtil.generateCaptcha(4);
        int width = 150;
        int height = 60;
        int fontSize = 48;
        
        // 设置到session中
        request.getSession().setAttribute(Constant.CAPTCHA, code);
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        Random random = new Random(); 
        
        g2.setPaint(Color.WHITE);   				// 设置图片背景为白色 
        g2.fillRect(1, 1, width-1, height-1);    	// 画出一个矩形
//        g2.setColor(new Color(102, 102, 102));      // 设置颜色
//        g2.drawRect(0, 0, width-1, height-1);		// 画图片的边框
        
        Font font = new Font("微软雅黑", Font.BOLD, fontSize);
        g2.setFont(font);
        
        g2.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));  
        g2.drawString(code,1,height-5);
        g2.dispose();
        
        try {
			ImageIO.write(image, "gif", response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
