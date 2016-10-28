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
        long millis = System.currentTimeMillis();
        long timeout = 1000;
        while((System.currentTimeMillis() - millis) < timeout){
            //观察这个key是否变动
            jedis.watch("invertory:1");
            if(!jedis.sismember("invertory:1","itemB")){
                jedis.unwatch();
                return ;
            }

            Transaction tx = jedis.multi();
            tx.zadd("market",90,"itemB_user:1");
            tx.srem("invertory:1","itemB");
            list = tx.exec();
            if(list == null || list.isEmpty()){
                continue;
            }
            System.out.println("上架商品B："+list);
            tx.close();
            break;
        }
        /**
         * 用户B购买商品并且支付现金，使用管道中调用事务来处理
         */
        millis = System.currentTimeMillis();
        while((System.currentTimeMillis() - millis) < timeout){
            jedis.watch("market");
            /**
             * 使用jedis来获取价格是因为使用pipeline必须要p.sync()来获取response才有数据
             * 否则是Please close pipeline or multi block before calling this method.错误
             */
            double price = jedis.zscore("market","itemB_user:1");
            double cash = Double.parseDouble(jedis.hget("user:2","cash"));
            if(price > cash){
                jedis.unwatch();
                return;
            }
            Pipeline p = jedis.pipelined();

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

    /**
     * 将新数据添加到list的开始
     */
    @Test
    public void testContact(){
        String ac_list = "recent:1"; //userId = 1

        Pipeline pipe = jedis.pipelined();
        pipe.multi();
        pipe.lpush(ac_list,"Pony","Jack","Roin","Robin");
        pipe.lrem(ac_list,1,"mary");
        pipe.ltrim(ac_list,0,99);
        pipe.exec();
    }

    @After
    public void tearDown(){
        if(jedis != null){
            pool.returnResource(jedis);
        }
    }
}
