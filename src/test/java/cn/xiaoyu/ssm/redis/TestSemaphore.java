package cn.xiaoyu.ssm.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.ZParams;

import java.util.List;
import java.util.UUID;

/**
 * 测试信号量【限制一项资源最多能够同时被多少个进程访问】
 * Created by roin_zhang on 2016/10/31.
 */
public class TestSemaphore {
    private Jedis jedis;

    public TestSemaphore(){
        jedis = new Jedis("localhost");
        jedis.auth("xiaode");
    }

    /**
     * 获取信号量
     * @param jedis jedie连接
     * @param semname 信号量名称
     * @param limit 限制数
     * @param timeout 超时时间-毫秒
     * @return
     */
    public String acquireSemaphore(Jedis jedis,String semname,int limit,long timeout){
        String identifier = UUID.randomUUID().toString();
        String czset = semname + ":owner";
        String ctr = semname + ":counter";

        long mills = System.currentTimeMillis();

        // 删除超时的信号量
        Transaction trans = jedis.multi();
        trans.zremrangeByScore(
                semname.getBytes(),
                "-inf".getBytes(), //"-inf" 表示最小值
                String.valueOf(mills - timeout).getBytes());
        ZParams params = new ZParams();
        params.weightsByDouble(1,0);
        trans.zinterstore(czset,params,czset,semname); //添加权重

        trans.incr(ctr);
        List<Object> results = trans.exec();
        int counter = ((Long)results.get(results.size() - 1)).intValue();

        // 添加并获取排名 决定获取信号量是否成功
        trans = jedis.multi();
        trans.zadd(semname,mills,identifier);
        trans.zadd(czset,counter,identifier);
        trans.zrank(czset,identifier);
        results = trans.exec();

        int result = ((Long)results.get(results.size() - 1)).intValue();
        if (result < limit){
            return identifier;
        }

        //客户端未能取得信号量，清理无用的数据
        trans = jedis.multi();
        trans.zrem(semname,identifier);
        trans.zrem(czset,identifier);
        trans.exec();
        return null;
    }

    /**
     * 释放公平锁
     * @param jedis
     * @param semname
     * @param identifier
     * @return
     */
    public boolean releaseFairSemaphore(Jedis jedis,String semname,String identifier){
        Transaction trans = jedis.multi();
        trans.zrem(semname,identifier);
        trans.zrem(semname + ":counter",identifier);
        List<Object> results = trans.exec();
        return (Long)results.get(results.size() - 1) == 1;
    }

}
