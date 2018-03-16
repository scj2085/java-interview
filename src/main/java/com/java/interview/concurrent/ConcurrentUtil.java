package com.java.interview.concurrent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author shichangjian
 *
 */
public class ConcurrentUtil {

	private ThreadPoolExecutor pool = null;  
    
    
    /** 
     * 线程池初始化方法 
     *  
     * corePoolSize 核心线程池大小---- 
     * maximumPoolSize 最大线程池大小---- 
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0； 
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES 
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(10)====10容量的阻塞队列 
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂 
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时, 
     *                          即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)), 
     *                                任务会交给RejectedExecutionHandler来处理 
     *                                
     *                                
     *    运行原理                            
     *  1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。 
	 *	2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行 
	 *	3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务 
	 *	4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理 
	 *	5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程 
	 *	6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭
	 *	
	 *  一下都是在阻塞情况下测试
	 *	第1-2个线程放入核心池中执行任务（依据条件核心线程池corePoolSize），现在核心池中2个线程再跑
	 *	继续阻塞第1-16个线程进入阻塞队列中等待（依据条件workQueue），现在阻塞队列中16个线程再跑，核心池中还是2个
	 *  核心池和阻塞队列都满，核心池中线程活跃数会从2加到9
	 *  核心池和阻塞队列都满，核心池中线程活跃数9，也就是最大线程池9，此时会报警
	 *	报警处理RejectedExecutionHandler抛给线程池再执行
     */  
    public void init() {  
        pool = new ThreadPoolExecutor(  
                2,  
                9,  
                30,  
                TimeUnit.MINUTES,  
                new ArrayBlockingQueue<Runnable>(16),  
                new CustomThreadFactory(),  
                (RejectedExecutionHandler) new CustomRejectedExecutionHandler());  
    }
    public void init2(ThreadFactory nameThreadFactory) {  
    	pool = new ThreadPoolExecutor(  
    			2,  
    			9,  
    			30,  
    			TimeUnit.MINUTES,  
    			new ArrayBlockingQueue<Runnable>(16),  
    			nameThreadFactory,  
    			(RejectedExecutionHandler) new CustomRejectedExecutionHandler());  
    }
    
    /**
     * 构造一个固定线程数目的线程池，配置的corePoolSize与maximumPoolSize大小相同，
     * 同时使用了一个无界LinkedBlockingQueue存放阻塞任务，因此多余的任务将存在再阻塞队列，不会由RejectedExecutionHandler处理
     * @param nThreads
     * @return
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {  
        return new ThreadPoolExecutor(nThreads, nThreads,  
                                      0L, TimeUnit.MILLISECONDS,  
                                      new LinkedBlockingQueue<Runnable>());  
    }  
      
    /**
     * 构造一个缓冲功能的线程池，配置corePoolSize=0，maximumPoolSize=Integer.MAX_VALUE，keepAliveTime=60s,
     * 以及一个无容量的阻塞队列 SynchronousQueue，因此任务提交之后，将会创建新的线程执行；线程空闲超过60s将会销毁
     * @return
     */
    public static ExecutorService newCachedThreadPool() {  
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,  
                                      60L, TimeUnit.SECONDS,  
                                      new SynchronousQueue<Runnable>());  
    } 
    
    /**
     * 一个线程执行，它能保证线程的先后顺序执行，并且能保证一条线程执行完成后才开启另一条新的线程
     * @return
     */
    public static ExecutorService newSingleThreadExecutor() { 
    	
    	return new FinalizableDelegatedExecutorService  
            (new ThreadPoolExecutor(1, 1,  
                                    0L, TimeUnit.MILLISECONDS,  
                                    new LinkedBlockingQueue<Runnable>()));  
    }
    
    /** 
     * 初始化延迟0ms开始执行，每隔2秒执行一次，注意，如果上次的线程还没有执行完成，那么会阻塞下一个线程的执行。即使线程池设置得足够大
     * 间隔指的是连续两次任务开始执行的间隔。对于scheduleAtFixedRate方法，当执行任务的时间大于我们指定的间隔时间时，它并不会在指定间隔时开辟一个新的线程并发执行这个任务。而是等待该线程执行完毕
     */  
    public static void executeFixedRate() {    
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);    
        executor.scheduleAtFixedRate(    
                new MyHandle(),    
                0,    
                2000,    
                TimeUnit.MILLISECONDS);    
    } 
    
    /**  
     * 以固定延迟时间进行执行  
     * 间隔指的是连续上次执行完成和下次开始执行之间的间隔
     * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务  
     */    
    public static void executeFixedDelay() {    
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);    
        executor.scheduleWithFixedDelay(    
                new MyHandle(),    
                0,    
                2000,    
                TimeUnit.MILLISECONDS);    
    }
    
    /**  
     * 获取指定时间对应的毫秒数  
     * 周期性的执行一个任务，可以使用下面方法设定每天在固定时间执行一次任务
     * @param time "HH:mm:ss"  
     * @return  
     */    
    private static long getTimeMillis(String time) {    
        try {    
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");    
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");    
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);    
            return curDate.getTime();    
        } catch (ParseException e) {    
            e.printStackTrace();    
        }    
        return 0;    
    } 
    
    
    
    
    
    public void destory() {  
        if(pool != null) {  
            pool.shutdownNow();  
        }  
    }  
      
      
    /**  
	 * 每天晚上9点执行一次  
	 * 每天定时安排任务进行执行  
	 */    
	public static void executeEightAtNightPerDay() {    
	    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);    
	    long oneDay = 24 * 60 * 60 * 1000;    
	    long initDelay  = getTimeMillis("21:00:00") - System.currentTimeMillis();    
	    initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;    
	    
	    executor.scheduleAtFixedRate(    
	            new MyHandle(),    
	            initDelay,    
	            oneDay,    
	            TimeUnit.MILLISECONDS);    
	}
	public ExecutorService getCustomThreadPoolExecutor() {  
        return this.pool;  
    }  
      
    private class CustomThreadFactory implements ThreadFactory {  
  
//    	AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减,十分适合高并发情况下的使用
//    	AtomicInteger(0)这里0 value使用了volatile关键字，volatile在这里可以做到的作用是使得多个线程可以共享变量，
//    	但是问题在于使用volatile将使得VM优化失去作用，导致效率较低，所以要在必要的时候使用，因此AtomicInteger类不要随意使用
        private AtomicInteger count = new AtomicInteger(0);  
          
        @Override  
        public Thread newThread(Runnable r) {  
            Thread t = new Thread(r);  
//          public final int addandget(int delta) delta 要加上的值
//          以原子方式将给定值与当前值相加
//            System.err.println(count.addAndGet(1));
//          得到类的简写名称
//          ConcurrentUtil.class.getSimpleName()
            String threadName = ConcurrentUtil.class.getSimpleName() + count.addAndGet(1);  
            System.err.println(threadName);  
            t.setName(threadName);  
            return t;  
        }  
    }  
      
      
    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {  
  
//        @Override  
//        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {  
//            // 记录异常  
//            // 报警处理等  
//            System.out.println("error.............");  
//        }  
        @Override    
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {    
            try {  
                // 核心改造点，由blockingqueue的offer改成put阻塞方法  
//            	当提交任务被拒绝时，进入拒绝机制，我们实现拒绝方法，把任务重新用阻塞提交方法put提交，实现阻塞提交任务功能，防止队列过大，OOM（内存泄漏和溢出）
            	System.err.println(executor.toString());
                executor.getQueue().put(r);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        } 
    }  
      
      
      
    // 测试构造的线程池  
    public static void main(String[] args) {  
    	ConcurrentUtil exec = new ConcurrentUtil();  
        // 1.初始化  
        exec.init();  
          
        ExecutorService pool = exec.getCustomThreadPoolExecutor();
        
        for(int i = 1; i<100; i++) {  
            System.out.println("提交第" + i + "个任务!");  
            pool.execute(new Runnable() {  
                @Override  
                public void run() {  
                    try {  
                        Thread.sleep(30000);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                    exec.sys();
                }  
            });  
        }  
          
          
          
//         2.销毁----此处不能销毁,因为任务没有提交执行完,如果销毁线程池,任务也就无法执行了  
//         exec.destory();  
          
//        try {  
//            Thread.sleep(10000);  
//        } catch (InterruptedException e) {  
//            e.printStackTrace();  
//        }  
    } 
    public void sys(){
    	a++;
    	System.err.println("running=====执行任务==" + a);
    }
    int a = 0;
    
    
    
    
    
    
    
   
    
	static class DelegatedExecutorService extends AbstractExecutorService {
		private final ExecutorService e;

		DelegatedExecutorService(ExecutorService executor) {
			e = executor;
		}

		public void execute(Runnable command) {
			e.execute(command);
		}

		public void shutdown() {
			e.shutdown();
		}

		public List<Runnable> shutdownNow() {
			return e.shutdownNow();
		}

		public boolean isShutdown() {
			return e.isShutdown();
		}

		public boolean isTerminated() {
			return e.isTerminated();
		}

		public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
			return e.awaitTermination(timeout, unit);
		}

		public Future<?> submit(Runnable task) {
			return e.submit(task);
		}

		public <T> Future<T> submit(Callable<T> task) {
			return e.submit(task);
		}

		public <T> Future<T> submit(Runnable task, T result) {
			return e.submit(task, result);
		}

		public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
			return e.invokeAll(tasks);
		}

		public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
				throws InterruptedException {
			return e.invokeAll(tasks, timeout, unit);
		}

		public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
				throws InterruptedException, ExecutionException {
			return e.invokeAny(tasks);
		}

		public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException, TimeoutException {
			return e.invokeAny(tasks, timeout, unit);
		}
	}

	static class FinalizableDelegatedExecutorService extends DelegatedExecutorService {
		FinalizableDelegatedExecutorService(ExecutorService executor) {
			super(executor);
		}

		protected void finalize() {
			super.shutdown();
		}

	}
	
	static class MyHandle implements Runnable {  
		  
	    @Override  
	    public void run() {  
	        System.out.println(System.currentTimeMillis());  
	        try {  
	            Thread.sleep(1 * 1000);  
	        } catch (InterruptedException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	    }  
	      
	}
	
}
