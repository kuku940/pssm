package cn.xiaoyu.ssm.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by roin_zhang on 2016/10/26.
 */
public class TestRedis {
    /**
     * 不使用连接池，直接使用jedis获取链接
     */
    @Test
    public void testConn(){
        Jedis conn = new Jedis();
        conn.auth("xiaode");
        String result = conn.ping();
        System.out.println(result);
        conn.disconnect();
    }


    private JedisPool pool = null;
    private Jedis jedis = null;
    @Before
    public void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        //最大空闲连接数，默认为8个
        config.setMaxIdle(8);
        //获取链接时的最大等待毫秒数，默认-1，阻塞
        config.setMaxWaitMillis(1000*10);
        //最小空闲连接数, 默认0
        config.setMinIdle(0);

        pool = new JedisPool(config,"localhost");
    }

    /**
     * 使用连接池来获取jedis的连接
     * @return jedis连接
     */
    public Jedis getConn(){
        jedis = pool.getResource();
        jedis.auth("xiaode");
        return jedis;
    }

    @Test
    public void testString(){
        jedis = this.getConn();
        jedis.set("user","xiaoyu");
        System.out.println(jedis.get("user"));
    }

    /**
     * 玩家进行交易，有三种实体
     * 1、玩家 user
     * 2、包裹 inventory
     * 3、市场 market
     */
    @Test
    public void testMulti() throws Exception {
        jedis = this.getConn();
        /**
         * 初始化如下数据：【使用管道】
         * ---hash---
         * user:1 name:xiaoyu cash 200
         * user:2 name:roin cash 99
         * ---zset---
         * market itemA
         * ---set---
         * invertory:1 itemB itemC
         * invertory:2 itemD
         *
         * 现在实现如下场景：
         * 1、用户1将B商品以90货币上架
         * 2、用户2买下B商品
         */
        Pipeline pipe = jedis.pipelined();
        pipe.del("user:1","user:2","market","invertory:1","invertory:2");

        Map user = new HashMap();
        user.put("name","xiaoyu");
        user.put("cash","200");
        pipe.hmset("user:1",user);

        user.put("name","roin");
        user.put("cash","99");
        pipe.hmset("user:2",user);

        pipe.zadd("market",100,"itemA_user:3");

        pipe.sadd("invertory:1","itemB","itemC");
        pipe.sadd("invertory:2","itemD");

//        pipe.sync();
        List<Object> list = pipe.syncAndReturnAll();
        pipe.close();
        System.out.println("初始化数据："+list);

        /**
         * 用户A将商品B上架，使用事务来处理
         */
        long nano = System.nanoTime();
        long timeout = 10000;
        while((System.nanoTime() - nano) < timeout){
            Transaction tx = jedis.multi();
            tx.zadd("market",90,"itemB_user:1");
            tx.srem("invertory:1","itemB");
            list = tx.exec();
            if(list == null || list.isEmpty()){
                throw new Exception("sale itemB fail");
            }
            System.out.println("上架商品B："+list);
            tx.close();
            break;
        }
        /**
         * 用户B购买商品并且支付现金，使用管道中调用事务来处理
         */
        nano = System.nanoTime();
        while((System.nanoTime() - nano) < timeout){
            Pipeline p = jedis.pipelined();
            /**
             * 使用jedis来获取价格是因为使用pipeline必须要p.sync()来获取response才有数据
             * 否则是Please close pipeline or multi block before calling this method.错误
             */
            double price = jedis.zscore("market","itemB_user:1");
            p.multi();
            p.hincrByFloat("user:2","cash",-1 * price);
            p.hincrByFloat("user:1","cash",price);
            p.zrem("market","itemB_user:1");
            p.sadd("invertory:2","itemB");
            p.exec();
            list = p.syncAndReturnAll();
            p.close();
            System.out.println("购买商品B:"+list);
            break;
        }
    }

    @After
    public void tearDown(){
        if(jedis != null){
            pool.returnResource(jedis);
        }
    }
}
