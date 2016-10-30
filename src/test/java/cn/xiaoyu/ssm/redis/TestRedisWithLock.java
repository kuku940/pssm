package cn.xiaoyu.ssm.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * Redis实现分布式锁
 * Redis中watch,multi,exec组成的事务并不具有可扩展性【乐观锁】，
 * 随着负载不断加大，乐观锁的时间就不断提高，所以这儿我们需要自己
 * 实现Redis的分布式锁
 * @author xiaoyu
 * @date 2016/10/31
 */
public class TestRedisWithLock {
    private Jedis jedis;

    public TestRedisWithLock(){
        jedis = new Jedis("localhost");
        jedis.auth("xiaode");
    }

    public static void main(String[] args) {
        new TestRedisWithLock();
    }

    /**
     * 获取分布式锁
     * setnx命令来获取分布式锁
     * 但是2.6以后可以使用SET key value [EX seconds] [PX milliseconds] [NX|XX]来替代该setnx命令
     * @param jedis - redis链接
     * @param lockname - 锁名称
     * @param acquireTimeout 请求超时时间-毫秒
     * @param lockTimeout 锁超时时间-秒
     */
    public String aquireLockWithTimeout(Jedis jedis,String lockname,long acquireTimeout,int lockTimeout){
        String identifier = UUID.randomUUID().toString();
        long mills = System.currentTimeMillis();
        lockname = "lock:" + lockname;

        while((mills + acquireTimeout) > System.currentTimeMillis()){
            if(jedis.setnx(lockname,identifier) == 1){
                jedis.expire(lockname,lockTimeout);
                return identifier;
            }

            //该值存在但是没有设置超时
            if (jedis.ttl(lockname) == -1){
                jedis.expire(lockname,lockTimeout);
            }

            try {
                Thread.sleep(1);
            }catch(InterruptedException ie){
                Thread.currentThread().interrupt();
            }
        }

        return null;
    }
    public String aquireLock(Jedis jedis,String lockname,int acquireTimeout){
        String identifier = UUID.randomUUID().toString();
        long mills = System.currentTimeMillis();
        lockname = "lock:" + lockname;

        while ((mills + acquireTimeout) > System.currentTimeMillis()) {
            if (jedis.setnx(lockname, identifier) == 1) {
                return identifier;
            }

            try {
                Thread.sleep(1);
            }catch(InterruptedException ie){
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    /**
     * 释放分布式锁
     */
    public boolean releaseLock(Jedis conn, String lockName, String identifier) {
        String lockKey = "lock:" + lockName;

        while (true){
            conn.watch(lockKey);
            if (identifier.equals(conn.get(lockKey))){
                Transaction trans = conn.multi();
                trans.del(lockKey);
                List<Object> results = trans.exec();
                if (results == null){
                    continue;
                }
                return true;
            }

            conn.unwatch();
            break;
        }

        return false;
    }
}
