package cn.xiaoyu.ssm.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by roin_zhang on 2016/10/26.
 */
public class TestRedis {
    Jedis jedis = null;
    @Before
    public void init(){
        jedis = new Jedis("localhost");
        jedis.auth("xiaode");
    }

    @Test
    public void testConn(){
        String result = jedis.ping();
        System.out.println(result);
    }


    @Test
    public void testMulti(){

    }
}
