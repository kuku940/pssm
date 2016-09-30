package cn.xiaoyu.ssm.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/10/8.
 * 伪异步IO服务器
 */
public class FakeServer {
    public static void main(String[] args) throws IOException {
        int port = 8899;
        ServerSocket server = new ServerSocket(port);
        System.out.println("伪异步IO服务器启动成功...");
        HandlerExecutePool pool = new HandlerExecutePool(50,1000);

        while(true){
            Socket socket = server.accept();
            // 启动新线程处理这次请求
            pool.execute(new Task(socket));
        }
    }
}

/**
 * 处理服务器请求的线程池
 */
class HandlerExecutePool {
    private ExecutorService executor;

    /**
     * @param maxPoolSize 线程池中最大线程数量
     * @param queueSize 保持任务队列的数量
     */
    public HandlerExecutePool(int maxPoolSize,int queueSize){
        /**
         * corePoolSize     - 池中所保存的线程数，包括空闲线程。
         * maximumPoolSize  - 池中允许的最大线程数。
         * keepAliveTime    - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
         * unit             - keepAliveTime 参数的时间单位。
         * workQueue        - 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。
         */
        this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120l, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task){
        executor.execute(task);
    }
}
