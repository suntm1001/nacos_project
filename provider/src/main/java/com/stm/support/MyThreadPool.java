package com.stm.support;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建线程池
 * @author huhailong
 *
 */
public class MyThreadPool {
    private static final int CORE_POOL_SIZE = 10;	//核型线程数，最小可以同时运行的线程数量
    private static final int MAX_POOL_SIZE = 10;	//最大线程数，当队列中存放的任务到达队列容量时，当前可以同时运行的线程数量变为最大线程数
    private static final int QUEUE_CAPACITY = 100;	//当新任务来的时候会先判断当前运行的线程数是否到达量核心线程数，如果达到量，就将新任务存放到队列中
    private static final long KEEP_ALIVE_TIME = 1L;	//当线程池中的线程数量大于核心线程池数量的时候，如果这时候没有新的任务提交，核心线程外的线程不会立即销毁，而是会等待，知道等待的时间超了设定的时间才会销毁

    /*
     * 线程池中的饱和策略
     * 1. ThreadPoolExecutor.AbortPolicy: 抛出RejectedExecutoionExpection来拒绝新任务的处理
     * 2. ThreadPoolExecutor.CallerRunsPolicy: 调用执行自己的线程运行任务，也就是直接调用execute方法线程中运行被拒绝的任务，如果执行程序已关闭，则会丢弃该任务。因此这种策略会降低对于新任务提交的速度，
     * 影响程序的整体性能。
     * 3. ThreadPoolExecutor.DiscardPolicy: 不处理新任务，直接丢弃
     * 4. ThreadPoolExecutor.DiscardOldestPolicy: 此策略将丢弃最早的未处理的任务请求
     * 5.
     */

    public static ThreadPoolExecutor createThreadPool() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        ThreadPoolExecutor executor = createThreadPool();
        List<Future<String>> list = new LinkedList<>();
        List<Integer> tempList = new ArrayList<>();
        //创建测试用例
        for(int i=0; i<100; i++) {
            tempList.add(100*(i+1));
        }
        for(int i=0; i<tempList.size(); i++) {
            MyTask worker = new MyTask(tempList.get(i));	//执行任务
            //下面两行代码是将执行的返回结果进行汇总
            Future<String> submit = executor.submit(worker);
            list.add(submit);
        }
        List<String> result = new LinkedList<>();
        for(Future<String> f : list) {
            //将汇总好的结果进行轮询，判断任务是否执行完成，确保每个任务执行完成后将结果添加到结果集中
            while(true) {
                if(f.isDone() && !f.isCancelled()) {
                    String object = f.get();
                    result.add(object);
                    break;
                }
            }
        }
//		List<Integer> result = new LinkedList<>();
//		for(int i=0; i<tempList.size(); i++) {
//			Thread.sleep(200);
//			Integer integer = tempList.get(i);
//			result.add(integer+1);
//		}
        System.out.println("最终汇总结果：");
        System.out.println(result);
        long end = System.currentTimeMillis();
        System.out.println("work time:"+(end - start)+"ms");
        //终止线程
        executor.shutdown();
        while(!executor.isTerminated()) {}
        System.out.println("Finished all threads");
    }
}
