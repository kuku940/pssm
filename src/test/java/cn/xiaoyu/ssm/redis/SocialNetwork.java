package cn.xiaoyu.ssm.redis;

import cn.xiaoyu.ssm.util.CommonUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import java.lang.reflect.Method;
import java.util.*;

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

    /**
     * 创建用户
     * @param conn
     * @param login
     * @param name
     * @return
     */
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

    /**
     * 关注某人
     * @param jedis
     * @param uid
     * @param otherUid
     * @return
     */
    public boolean followUser(Jedis jedis,long uid,long otherUid){
        String following = "following:"+uid;
        String followers = "followers:"+otherUid;

        //判断是否已经关注过该对象
        if(jedis.zscore(following,String.valueOf(otherUid)) != null){
            return false;
        }

        long mills = System.currentTimeMillis();
        Transaction trans = jedis.multi();
        trans.zadd(following,mills,String.valueOf(otherUid));
        trans.zadd(followers,mills,String.valueOf(uid));
        trans.zcard(following); //查看关注者的个数
        trans.zcard(followers);
        trans.zrevrangeWithScores("profile:"+otherUid,0,HOME_TIMELINE_SIZE - 1);
        List<Object> results = trans.exec();

        long followingCount = (long) results.get(results.size() - 3);
        long followersCount = (long) results.get(results.size() - 2);
        Set<Tuple> statuses = (Set<Tuple>) results.get(results.size() - 1);

        trans = jedis.multi();
        trans.hset("user:"+uid,"following",String.valueOf(followingCount));
        trans.hset("user:"+otherUid,"followers",String.valueOf(followersCount));
        if(statuses.size() > 0){
            for (Tuple status:statuses) {
                trans.zadd("home:"+uid,status.getScore(),status.getElement());
            }
        }
        trans.zremrangeByRank("home:"+uid,0,0 - HOME_TIMELINE_SIZE - 1);
        trans.exec();
        return true;
    }

    /**
     * 取消关注某人
     * @param jedis
     * @param uid
     * @param otherUid
     * @return
     */
    public boolean unfollowUser(Jedis jedis,long uid,long otherUid){
        String following = "following:"+uid;
        String followers = "followers:"+otherUid;

        //判断是否已经关注过该对象
        if(jedis.zscore(following,String.valueOf(otherUid)) == null){
            return false;
        }

        Transaction trans = jedis.multi();
        trans.zrem(followers,String.valueOf(uid));
        trans.zrem(following,String.valueOf(otherUid));
        trans.zcard(followers);
        trans.zcard(following);
        trans.zrevrange("profile:" + otherUid, 0, HOME_TIMELINE_SIZE - 1);
        List<Object> results = trans.exec();

        long followingCount = (long) results.get(results.size() - 3);
        long followersCount = (long) results.get(results.size() - 2);
        Set<String> statuses = (Set<String>) results.get(results.size() - 1);


        trans = jedis.multi();
        trans.hset("user:"+uid,"following",String.valueOf(followingCount));
        trans.hset("user:"+otherUid,"followers",String.valueOf(followersCount));
        if(statuses.size() > 0){
            for (String status:statuses) {
                trans.zrem("home:"+uid,status);
            }
        }
        trans.exec();
        return true;
    }

    /**
     * 创建状态
     * @param conn
     * @param uid
     * @param message
     * @return
     */
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
     * 发布状态，并更新到个人主页以及其他关注者主页
     * @param jedis
     * @param uid
     * @param message
     * @return
     */
    public long postStatus(Jedis jedis,long uid,String message){
        return postStatus(jedis,uid,message,null);
    }
    private long postStatus(Jedis jedis, long uid, String message, Map<String,String> data) {
        long statusId = createStatus(jedis,uid,message,data);
        if(statusId == -1){
            return -1;
        }

        String postedString = jedis.hget("status:"+statusId,"posted");
        if(postedString == null){
            return -1;
        }

        long postTime = Long.parseLong(postedString);
        // 添加到自己的发布状态及主页时间线
        jedis.zadd("profile:"+uid,postTime,String.valueOf(statusId));
        jedis.zadd("home:"+uid,postTime,String.valueOf(statusId));
        //同步到其他的关注者
        syndicateStatus(jedis,uid,statusId,postTime,0,true);
        return statusId;
    }

    /**
     * 同步状态信息到其他的关注者
     * @param jedis
     * @param uid
     * @param statusId
     * @param postTime
     * @param start 关注者位置
     * @param bool 是表示发布状态 否表示删除状态
     */
    public void syndicateStatus(Jedis jedis,long uid,long statusId,long postTime,double start,boolean bool){
        Set<Tuple> followers = jedis.zrangeByScoreWithScores("followers:"+uid,String.valueOf(start),"inf",0,POSTS_PER_PASS);

        Transaction trans = jedis.multi();
        for (Tuple user: followers) {
            String follower = user.getElement();
            start = user.getScore();
            trans.zadd("home:"+follower,postTime,String.valueOf(statusId));
            trans.zrange("home:"+follower,0,-1);
            trans.zremrangeByRank("home:"+follower,0,0-HOME_TIMELINE_SIZE-1);
        }
        trans.exec();

        //关注者大于1000人，起个线程私下跑
        if(followers.size() >= POSTS_PER_PASS){
            try{
                Method method = getClass().getDeclaredMethod("syndicateStatus",Jedis.class,Long.TYPE,Long.TYPE,Long.TYPE,Double.TYPE,Boolean.TYPE);
                executeLater("default",method,uid,statusId,postTime,start,bool);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public void synchronizeStatus(Jedis jedis,long uid,long statusId,long postTime,double start,boolean bool){
        Set<Tuple> followers = jedis.zrangeByScoreWithScores("followers:"+uid,String.valueOf(start),"inf",0,POSTS_PER_PASS);

        Transaction trans = jedis.multi();
        for (Tuple user: followers) {
            String follower = user.getElement();
            start = user.getScore();
            if(bool){
                trans.zadd("home:"+follower,postTime,String.valueOf(statusId));
                trans.zrange("home:"+follower,0,-1);
                trans.zremrangeByRank("home:"+follower,0,0-HOME_TIMELINE_SIZE-1);
            }else{
                trans.zrem("home"+follower,String.valueOf(statusId));
            }
        }
        trans.exec();

        //关注者大于1000人，起个线程私下跑
        if(followers.size() >= POSTS_PER_PASS){
            try{
                Method method = getClass().getDeclaredMethod("synchronizeStatus",Jedis.class,Long.TYPE,Long.TYPE,Long.TYPE,Double.TYPE,Boolean.TYPE);
                executeLater("default",method,uid,statusId,postTime,start,bool);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public boolean deleteStatus(Jedis jedis,long uid,long statusId){
        String key = "status:"+statusId;
        String lock = acquireLockWithTimeout(jedis,key,1,10);

        if(lock == null){
            return false;
        }

        try{
            if(!String.valueOf(uid).equals(jedis.hget(key,"uid"))){
                return false;
            }

            Transaction trans = jedis.multi();
            trans.del(key);
            trans.zrem("profile:"+uid,String.valueOf(statusId));
            trans.zrem("home:"+uid,String.valueOf(statusId));
            trans.hincrBy("user:"+uid,"posts",-1);

            //同步到其他的关注者
            synchronizeStatus(jedis,uid,statusId,0,0,false);
            trans.exec();

            return true;
        }finally {
            releaseLock(jedis,key,lock);
        }
    }

    public void refillTimeLine(Jedis jedis,String incoming,String timeline){
        refillTimeLine(jedis,incoming,timeline,0);
    }
    public void refillTimeLine(Jedis jedis,String incoming,String timeline,double start){
        if(start == 0 && jedis.zcard(timeline)>=750){
            return;
        }

        Set<Tuple> users = jedis.zrangeByScoreWithScores(incoming,String.valueOf(start),"inf",0,REFILL_USERS_STEP);
        Pipeline pipeline = jedis.pipelined();
        for(Tuple tuple:users){
            String uid = tuple.getElement();
            start = tuple.getScore();
            pipeline.zrevrangeWithScores("profile:"+uid,0,HOME_TIMELINE_SIZE-1);
        }

        List<Object> response = pipeline.syncAndReturnAll();
        List<Tuple> messages = new ArrayList<Tuple>();
        for(Object result:response){
            messages.addAll((Set<Tuple>)result);
        }
        Collections.sort(messages);
        messages = messages.subList(0,HOME_TIMELINE_SIZE);

        Transaction trans = jedis.multi();
        if (messages.size() > 0) {
            for (Tuple tuple : messages) {
                trans.zadd(timeline, tuple.getScore(), tuple.getElement());
            }
        }
        trans.zremrangeByRank(timeline, 0, 0 - HOME_TIMELINE_SIZE - 1);
        trans.exec();

        if (users.size() >= REFILL_USERS_STEP) {
            try{
                Method method = getClass().getDeclaredMethod(
                        "refillTimeline", Jedis.class, String.class, String.class, Double.TYPE);
                executeLater("default", method, incoming, timeline, start);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public void cleanTimelines(Jedis conn, long uid, long statusId) {
        cleanTimelines(conn, uid, statusId, 0, false);
    }
    public void cleanTimelines(
            Jedis conn, long uid, long statusId, double start, boolean onLists)
    {
        String key = "followers:" + uid;
        String base = "home:";
        if (onLists) {
            key = "list:out:" + uid;
            base = "list:statuses:";
        }
        Set<Tuple> followers = conn.zrangeByScoreWithScores(
                key, String.valueOf(start), "inf", 0, POSTS_PER_PASS);

        Transaction trans = conn.multi();
        for (Tuple tuple : followers) {
            start = tuple.getScore();
            String follower = tuple.getElement();
            trans.zrem(base + follower, String.valueOf(statusId));
        }
        trans.exec();

        Method method = null;
        try{
            method = getClass().getDeclaredMethod(
                    "cleanTimelines", Jedis.class,
                    Long.TYPE, Long.TYPE, Double.TYPE, Boolean.TYPE);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        if (followers.size() >= POSTS_PER_PASS) {
            executeLater("default", method, uid, statusId, start, onLists);

        }else if (!onLists) {
            executeLater("default", method, uid, statusId, 0, true);
        }
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

    private void executeLater(String queue, Method method, Object... args) {
        MethodThread thread = new MethodThread(this,method,args);
        new Thread(thread).start();
    }

    public class MethodThread implements Runnable{
        private Object instance;
        private Method method;
        private Object[] args;

        public MethodThread(Object instance,Method method,Object...args){
            this.instance = instance;
            this.method = method;
            this.args = args;
        }
        @Override
        public void run() {
            Jedis conn = new Jedis("localhost");
            conn.select(15);
            conn.auth("xiaode");

            Object[] args = new Object[this.args.length + 1];
            System.arraycopy(this.args,0,args,1,this.args.length);
            args[0] = conn;

            try{
                method.invoke(instance,args);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
