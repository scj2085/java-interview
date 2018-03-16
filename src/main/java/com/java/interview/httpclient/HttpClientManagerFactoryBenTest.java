package com.java.interview.httpclient;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.java.interview.concurrent.ConcurrentUtil;

@RestController
public class HttpClientManagerFactoryBenTest {
	
	// 注入HttpClient实例
	@Resource(name = "httpClientManagerFactoryBen")
	private CloseableHttpClient client;
	
	private ExecutorService executorService;
	int a = 0;
	String entity2 = null;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test() throws ClientProtocolException, IOException, InterruptedException, ExecutionException {
		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(16);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(  
					                2,  
					                9,  
					                30,  
					                TimeUnit.MINUTES,  
					                queue,  
					                new CustomThreadFactory(),  
					                (RejectedExecutionHandler) new CustomRejectedExecutionHandler());  
		
		ExecutorService service = Executors.newFixedThreadPool(9);
		for (int i = 0; i < 100; i++) {
			
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					System.err.println("the current thread is:" + Thread.currentThread().getName());
					try {
						test2();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					HttpEntity entity = null;
					try {
						HttpGet get = new HttpGet("http://127.0.0.1:2222/account/test");
						// 通过httpclient的execute提交 请求
						// 并用CloseableHttpResponse接受返回信息,try with resource语法response.close()会被自动调用
						CloseableHttpResponse response = client.execute(get);
						System.err.println("client object:" + client);
						entity = response.getEntity();
						System.out.println("============" + EntityUtils.toString(entity, Consts.UTF_8) + "=============");
						EntityUtils.consumeQuietly(entity);// 释放连接
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (null != entity) {// 释放连接
							EntityUtils.consumeQuietly(entity);
						}
					}
				}
			});
			
			
			System.out.println("线程池中活跃的线程数： " + pool.getPoolSize());
			if (queue.size() > 0) {
				System.out.println("----------------队列中阻塞的线程数" + queue.size());
			}
		}
		
	}
	
	public void test2() throws InterruptedException{
		a++;
		System.err.println("=====================" + a + "=============");
	} 
//	public void test3(Future future) throws InterruptedException, ExecutionException{
//		System.err.println(future.get()); // 打印各个线程（任务）执行的结果
//	} 
	
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
    	  
      // 记录异常，报警处理等 
      @Override    
      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {    
          try {  
              // 核心改造点，由blockingqueue的offer改成put阻塞方法  
        	  // 当提交任务被拒绝时，进入拒绝机制，我们实现拒绝方法，把任务重新用阻塞提交方法put提交，实现阻塞提交任务功能，防止队列过大，OOM（内存泄漏和溢出）
          	System.err.println(executor.toString());
              executor.getQueue().put(r);  
          } catch (InterruptedException e) {  
              e.printStackTrace();  
          }  
      } 
    }
    
    
    
    
    
    
    
    
    @RequestMapping(value = "/httpThreadPool")
    public String httpThreadPool() throws ParseException, IOException, InterruptedException, ExecutionException{
//    	ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(16);
//    	ExecutorService pool = new ThreadPoolExecutor(  
//					                2,  
//					                9,  
//					                1,  
//					                TimeUnit.MINUTES,  
//					                queue,  
//					                new CustomThreadFactory(),  
//					                (RejectedExecutionHandler) new CustomRejectedExecutionHandler());  
    	
    	Future<String> task = executorService.submit(new CallableImpl());
    	String httpEntity4 = task.get();
    	
		System.out.println("线程池中活跃的线程数： " + ((ThreadPoolExecutor)executorService).getPoolSize());
		
		// 当前阻塞排队线程数
		int queueSize = ((ThreadPoolExecutor)executorService).getQueue().size();
		System.out.println("----------------当前阻塞排队线程数:" + queueSize);
		// 当前活动线程数
		int activeCount = ((ThreadPoolExecutor)executorService).getActiveCount();
		System.out.println("----------------当前活动线程数:" + activeCount);
		// 执行完成线程数
		long completedTaskCount = ((ThreadPoolExecutor)executorService).getCompletedTaskCount();
		System.out.println("----------------执行完成线程数:" + completedTaskCount);
		// 总线程数（排队线程数 + 活动线程数 +  执行完成线程数）
		long taskCount = ((ThreadPoolExecutor)executorService).getTaskCount();
		System.out.println("----------------总线程数（排队线程数 + 活动线程数 +  执行完成线程数）:" + taskCount);
		
    	System.out.println("线程池中活跃的线程数： " + ((ThreadPoolExecutor)executorService).getPoolSize());
    	System.out.println(httpEntity4);
    	System.out.println(entity2);
    	return entity2;
    }
	 
    public void test3(String entity){
    	entity2 = entity;
    }
    
    
    
    
    
    
    
    
    public class CallableImpl implements Callable<String> {


        @Override
        public String call() throws Exception {
        	System.err.println("the current thread is:" + Thread.currentThread().getName());
        	HttpEntity entity3 = null;
        	String aa = null;
			try {
				test2();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				HttpGet get = new HttpGet("http://127.0.0.1:2222/account/test");
				// 通过httpclient的execute提交 请求
				// 并用CloseableHttpResponse接受返回信息,try with resource语法response.close()会被自动调用
				
				CloseableHttpResponse response = client.execute(get);
				System.err.println("client object:" + client);
				entity3 = response.getEntity();
				aa = EntityUtils.toString(entity3, Consts.UTF_8);
				test3(aa);
//				System.out.println("============" + EntityUtils.toString(entity3, Consts.UTF_8) + "=============");
				EntityUtils.consumeQuietly(entity3);// 释放连接
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != entity3) {// 释放连接
					EntityUtils.consumeQuietly(entity3);
				}
			}
			return aa;
			
        }
    }
    
    
    
    
    
    
//    public class MyRunnable implements Runnable{
//
//		@Override
//		public void run() {
//			System.err.println("the current thread is:" + Thread.currentThread().getName());
//			try {
//				test2();
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			HttpEntity entity = null;
//			try {
//				HttpGet get = new HttpGet("http://127.0.0.1:2222/account/test");
//				// 通过httpclient的execute提交 请求
//				// 并用CloseableHttpResponse接受返回信息,try with resource语法response.close()会被自动调用
//				
//				CloseableHttpResponse response = client.execute(get);
//				System.err.println("client object:" + client);
//				entity = response.getEntity();
//				test3(entity);
//				System.out.println("============" + EntityUtils.toString(entity, Consts.UTF_8) + "=============");
//				EntityUtils.consumeQuietly(entity);// 释放连接
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (ParseException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (null != entity) {// 释放连接
//					EntityUtils.consumeQuietly(entity);
//				}
//			}
//			
//		}
//    	
//    }
}
