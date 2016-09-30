package cn.xiaoyu.ssm.multithreading;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/10/8.
 * 测试线程池的使用方法
 */
public class TestThreadPool {
    public static void main(String[] args) {
        int poolMaxSize = 5;
        int queueSize = 10;
        /**
         * corePoolSize     - 池中所保存的线程数，包括空闲线程。
         * maximumPoolSize  - 池中允许的最大线程数。
         * keepAliveTime    - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
         * unit             - keepAliveTime 参数的时间单位。
         * workQueue        - 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。
         *
         * 可以并发执行的任务为线程最大的数量
         * 同时存在的任务数量为：线程最大的数量+队列的大小，即正在执行的任务+正在排队的任务数
         */
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3,poolMaxSize,120, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(queueSize));
        long start = System.currentTimeMillis();
        try{
            pool.execute(new Task("001"));
            pool.execute(new Task("002"));
            pool.execute(new Task("003"));
            pool.execute(new Task("004"));
            pool.execute(new Task("005"));
            pool.execute(new Task("006"));
            pool.execute(new Task("007"));
            pool.execute(new Task("008"));
            pool.execute(new Task("009"));
            pool.execute(new Task("010"));
            pool.execute(new Task("011"));
            pool.execute(new Task("012"));
        }catch (RejectedExecutionException e){
            // 表示任务数已经满载
        }
        System.out.println("运行时间："+(System.currentTimeMillis() - start)+"ms");
    }
}
class Task implements Runnable{
    private String name;
    public Task(String name){
        this.name = name;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(500);
            System.out.println(name+"线程休眠结束..."+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
