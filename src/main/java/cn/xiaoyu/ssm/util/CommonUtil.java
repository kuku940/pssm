package cn.xiaoyu.ssm.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by roin_zhang on 2016/9/29.
 */
public class CommonUtil {
    /**
     * 关闭文件资源
     * @param closeable
     */
    public static void close(Closeable closeable){
        if(closeable != null){
            try{
                closeable.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void assertTrue(boolean bool) throws Exception{
        if(!bool){
            throw new Exception();
        }
    }
}
