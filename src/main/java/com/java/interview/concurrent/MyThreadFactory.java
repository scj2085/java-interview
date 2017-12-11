package com.java.interview.concurrent;

import java.util.Date;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadFactory implements ThreadFactory {

	private int counter;
	private String prefix;
	private AtomicInteger count = new AtomicInteger(0);

	public MyThreadFactory() {
		
	}
	
	public MyThreadFactory(String prefix) {
		this.prefix = prefix;
		counter = 1;
	}


	
	@Override
	public Thread newThread(Runnable r) {
//		MyThread myThread = new MyThread(r, prefix + "-" + counter);
//		counter++;
//		return myThread;
		
		Thread t = new Thread(r);  
//      public final int addandget(int delta) delta 要加上的值
//      以原子方式将给定值与当前值相加
//        System.err.println(count.addAndGet(1));
//      得到类的简写名称
//      ConcurrentUtil.class.getSimpleName()
        String threadName = "MyThreadFactory" + count.addAndGet(1);  
        System.err.println(threadName);  
        t.setName(threadName);  
        return t;
		
	}

	
	public class MyThread extends Thread{

		private Date creationDate;
		private Date startDate;
		private Date finishDate;
		
		/**
		 * 实现这个类的构造器。它接收名称和要执行的Runnable对象参数。存储线程的创建日期
		 */
		public MyThread(Runnable target, String name) {
			super(target, name);
			setCreationDate();
		}

		/**
		 * 实现run()方法。存储线程的开始时间，调用父类的run()方法，存储执行的结束时间
		 */
		public void run() {
			setStartDate();
			super.run();
			setFinishDate();
		}
		
		/**
		 * 实现一个方法用来设置creationDate属性值。
		 */
		public void setCreationDate() {
			creationDate = new Date();
		}

		
		/**
		 * 实现一个方法用来设置startDate属性值 
		 */
		public void setStartDate() {
			startDate = new Date();
		}
		
		
		/**
		 * 实现一个方法用来设置finishDate属性值 
		 */
		public void setFinishDate() {
			finishDate = new Date();
		}

		
		/**
		 * 实现getExecutionTime()方法，用来计算线程的执行时间（结束日期与开始日期之差）
		 * @return
		 */
		public long getExecutionTime() {
			return finishDate.getTime() - startDate.getTime();
		}

		/* 
		 * 覆盖toString()方法，返回线程的创建日期和执行日期
		 */
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append(getName());
			buffer.append(": ");
			buffer.append(" Creation Date: ");
			buffer.append(creationDate);
			buffer.append(" : Running time: ");
			buffer.append(getExecutionTime());
			buffer.append(" Milliseconds.");
			return buffer.toString();
		}

	}
	
	public static class MyTask implements Runnable {
		
		@Override
		public void run() {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		MyThreadFactory myFactory = new MyThreadFactory("MyThreadFactory");
		MyTask task = new MyTask();
		Thread thread = myFactory.newThread(task);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf("Main: Thread information.\n");
		System.err.printf("%s\n", thread);
		System.out.printf("Main: End of the example.\n");
	}
}
