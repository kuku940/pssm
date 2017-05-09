package cn.xiaoyu.ssm.test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer> {
    public static final int threshold = 2;
    private int start;
    private int end;

    public CountTask(int start,int end){
        this.start = start;
        this.end = end;
    }

    protected Integer compute(){
        int sum = 0;
        boolean canCompute = (end - start) <= threshold;
        if(canCompute){
            for(int i=start;i<=end;i++){
                sum += i;
            }
        }else{
            int middle = (start + end) / 2;
            CountTask leftTask = new CountTask(start,middle);
            CountTask rightTask = new CountTask(middle+1,end);

            leftTask.fork();
            rightTask.fork();

            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            sum = leftResult + rightResult;

        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkjoinPool = new ForkJoinPool();

        //生成一个计算任务，计算1+2+3+4
        CountTask task = new CountTask(1,100);

        //执行一个任务
        Future<Integer> result = forkjoinPool.submit(task);

        try {
            System.out.println(result.get());
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
