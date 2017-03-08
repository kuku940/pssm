package cn.xiaoyu.ssm.test;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试首字母查询
 * @author xiaoyu
 * @date 2017/3/2
 */
public class TestSeachByPinyin {
    public static void main(String[] args) {
        String cnStr = "重庆银行";
        System.out.println(getPinYin(cnStr));
        System.out.println(getPinYinHeadChar(cnStr));
    }

    /**
     * 获取中文的首字母
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str){
        String convert = "";
        for(int j=0;j<str.length();j++){
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if(pinyinArray != null){
                convert += pinyinArray[0].charAt(0);
            }else{
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 获取中文的全拼
     * @param str
     * @return
     */
    public static String getPinYin(String str){
        char[] t1 = str.toCharArray();
        String[] t2 = new String[t1.length];

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String t4 = "";
        int len = t1.length;

        try{
            for(int i=0;i<len;i++){
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], format);
                    t4 += t2[0];
                } else {
                    t4 += Character.toString(t1[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return t4;
    }

    /**
     * 将字符串转移为ASCII码
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // System.out.println(Integer.toHexString(bGBK[i]&0xff));
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }
}
