//package com.java.interview.httpclient;
//
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.RejectedExecutionHandler;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//
//import com.java.interview.concurrent.ConcurrentUtil;
//
//@ConfigurationProperties(prefix = "threadPool")
//public class ThreadPoolConfig {
//
//	private int corePoolSize;		//核心线程
//	private int maximumPoolSize;	//线程池最大线程
//	private long keepAliveTime; 	//条件是：超过核心线程数，空闲线程存活时间
//	private int queueCapacity;		//队列容量
//	
//	@Bean
//	public ExecutorService executorService(){
//		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueCapacity);
//		ExecutorService pool = new ThreadPoolExecutor(  
//				corePoolSize,  
//				maximumPoolSize,  
//				keepAliveTime,  
//				TimeUnit.SECONDS,  
//                queue,  
//                new CustomThreadFactory(),  
//                (RejectedExecutionHandler) new CustomRejectedExecutionHandler());  
//		
//		return pool;
//	}
//	
//	
//	private class CustomThreadFactory implements ThreadFactory {  
//  	  
////    	AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减,十分适合高并发情况下的使用
////    	AtomicInteger(0)这里0 value使用了volatile关键字，volatile在这里可以做到的作用是使得多个线程可以共享变量，
////    	但是问题在于使用volatile将使得VM优化失去作用，导致效率较低，所以要在必要的时候使用，因此AtomicInteger类不要随意使用
//        private AtomicInteger count = new AtomicInteger(0);  
//          
//        @Override  
//        public Thread newThread(Runnable r) {  
//            Thread t = new Thread(r);  
////          public final int addandget(int delta) delta 要加上的值
////          以原子方式将给定值与当前值相加
////            System.err.println(count.addAndGet(1));
////          得到类的简写名称
////          ConcurrentUtil.class.getSimpleName()
//            String threadName = ConcurrentUtil.class.getSimpleName() + count.addAndGet(1);  
//            System.err.println(threadName);  
//            t.setName(threadName);  
//            return t;  
//        }  
//    }
//    
//    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {  
//    	  
//      // 记录异常，报警处理等 
//      @Override    
//      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {    
//          try {  
//        	  // 核心改造点，由blockingqueue的offer改成put阻塞方法  
//        	  // 当提交任务被拒绝时，进入拒绝机制，我们实现拒绝方法，把任务重新用阻塞提交方法put提交，实现阻塞提交任务功能，防止队列过大，OOM（内存泄漏和溢出）
//        	  System.err.println(executor.toString());
//        	  executor.getQueue().put(r);  
//          } catch (InterruptedException e) {  
//              e.printStackTrace();  
//          }  
//      } 
//    }
//	
//	
//}
