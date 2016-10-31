package cn.xiaoyu.ssm.redis;

import cn.xiaoyu.ssm.util.CommonUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by roin_zhang on 2016/10/31.
 */
public class SocialNetwork{
    private static int HOME_TIMELINE_SIZE = 1000;
    private static int POSTS_PER_PASS = 1000;
    private static int REFILL_USERS_STEP = 50;

    public static void main(String[] args) throws Exception {
        new SocialNetwork().run();
    }

    private void run() throws Exception {
        Jedis conn = new Jedis("localhost");
        conn.auth("xiaode");
        conn.select(15);
        conn.flushDB();

        testCreateUserAndStatus(conn);
    }

    private void testCreateUserAndStatus(Jedis conn) throws Exception {
        System.out.println("\n----- testCreateUserAndStatus -----");

        CommonUtil.assertTrue(createUser(conn,"TestUser","Test User") == 1);
        CommonUtil.assertTrue(createUser(conn,"TestUser","Test User2") == -1);

        CommonUtil.assertTrue(createStatus(conn,1,"This is a new status message") == 1);
        System.out.println(conn.hget("user:1","posts"));
    }

    public long createUser(Jedis conn,String login,String name){
        String llogin = login.toLowerCase();
        String lock = acquireLockWithTimeout(conn,"user:"+llogin,10*1000,9999);

        if(lock == null){
            return -1;
        }

        if(conn.hget("users:",llogin) != null){
            return -1;
        }

        long id = conn.incr("user:id:");
        Transaction trans = conn.multi();
        trans.hset("users:",llogin,String.valueOf(id));
        Map<String,String> values = new HashMap<String,String>();
        values.put("login",llogin);             //小写的用户名
        values.put("id",String.valueOf(id));    //用户id
        values.put("name",name);        //昵称
        values.put("followers","0");    //拥有的关注者
        values.put("following","0");    //关注的人
        values.put("posts","0");        //已发布的状态数量
        values.put("signip",String.valueOf(System.currentTimeMillis()));    //注册日期
        trans.hmset("user:"+id,values);
        trans.exec();
        releaseLock(conn,"user:"+llogin,lock);

        return id;
    }

    public long createStatus(Jedis conn,long uid,String message){
        return createStatus(conn,uid,message,null);
    }
    public long createStatus(Jedis conn,long uid,String message,Map<String,String> data){
        Transaction trans = conn.multi();
        trans.hget("user:"+uid,"login");
        trans.incr("status:id:");

        List<Object> resp = trans.exec();
        String login = (String) resp.get(0);
        long id = (long) resp.get(1);

        if(login == null){
            return -1;
        }

        if(data == null){
            data = new HashMap<String,String>();
        }

        data.put("message",message);                //消息内容
        data.put("posted",String.valueOf(System.currentTimeMillis())); //发布时间
        data.put("id",String.valueOf(id));          //消息id
        data.put("uid",String.valueOf(uid));        //用户id
        data.put("login",login);                    //用户名

        trans = conn.multi();
        trans.hmset("status:"+id,data);
        trans.hincrBy("user:"+id,"posts",1);
        trans.exec();
        return id;
    }

    /**
     * 分布式锁
     * @param conn
     * @param lockname
     * @param acquireTimeout
     * @param lockTimeout
     * @return
     */
    private String acquireLockWithTimeout(Jedis conn, String lockname, long acquireTimeout, int lockTimeout) {
        String id = UUID.randomUUID().toString();
        lockname = "lock:"+lockname;

        long mills = System.currentTimeMillis();
        while((mills + acquireTimeout) > System.currentTimeMillis()){
            if(conn.setnx(lockname,id) >= 1){
                conn.expire(lockname,lockTimeout);
                return id;
            }else if(conn.ttl(lockname) <= 0){
                conn.expire(lockname,lockTimeout);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        return null;
    }

    /**
     * 释放锁
     * @param conn
     * @param lockname
     * @param identifier
     */
    private boolean releaseLock(Jedis conn, String lockname, String identifier) {
        lockname = "lock:" + lockname;
        while(true){
            conn.watch(lockname);
            if(identifier.equals(conn.get(lockname))){
                Transaction trans = conn.multi();
                trans.del(lockname);
                List list = trans.exec();
                if(list == null){
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
