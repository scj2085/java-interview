package com.java.interview.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class Test {
	
	
	
//	public static void main(String[] args) {
//		ConcurrentUtil exec = new ConcurrentUtil();
//		ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d").build();
//		exec.init2(nameThreadFactory);
//		
//		ExecutorService pool = exec.getCustomThreadPoolExecutor();
//		pool.execute(new MyRunNable());
//		
//		
////		for(int i = 1; i<100; i++) {  
////            System.out.println("提交第" + i + "个任务!"); 
////            pool.execute(new MyRunNable());
////            pool.execute(new Runnable() {  
////                @Override  
////                public void run() {  
////                    try {  
////                        Thread.sleep(3000);  
////                    } catch (InterruptedException e) {  
////                        e.printStackTrace();  
////                    }  
////                    System.err.println("running=====");  
////                }  
////            });  
////        }
//	}
	
	/**
	 * Runnable接口实现多线程
	 * @author shichangjian
	 *
	 */
	public static class MyRunNable implements Runnable {

		private int ticket = 1000000;

		public void run() {
			for (int i = 0; i < 2000000; i++) {
				if (this.ticket > 0) {
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					System.out.println("卖票：ticket" + this.ticket--);
					System.err.println("当前线程" + Thread.currentThread().getName());
				}
			}
		}

	}
	
	
}
