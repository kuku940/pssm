package cn.xiaoyu.ssm.util;

import java.util.Random;

/**
 * 图片验证码生成类
 * @author 章小雨
 * @time 2016年3月25日 上午10:05:30
 * @email zhangxy@hktv.tv
 * @qq 1349274643
 */
public class CaptchaUtil {
	public static final String VERIFY_CODES = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/** 
     * 生成验证码 
     * @param verifySize	验证码长度 
     * @return 
     */  
    public static String generateCaptcha(int verifySize){  
        int length = VERIFY_CODES.length();  
        Random rand = new Random(System.currentTimeMillis());  
        StringBuilder verifyCode = new StringBuilder(verifySize);  
        for(int i = 0; i < verifySize; i++){  
            verifyCode.append(VERIFY_CODES.charAt(rand.nextInt(length-1)));  
        }  
        return verifyCode.toString();  
    }
    
}
