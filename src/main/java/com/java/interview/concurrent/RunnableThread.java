package com.java.interview.concurrent;

/**
 * 多线程中Runnable和Thread的区别
 * 在实际开发中一个多线程的操作很少使用Thread类，而是通过Runnable接口完成
 * @author shichangjian
 *
 */
public class RunnableThread {

	
	/**
	 * thread类实现多线程
	 * @author shichangjian
	 *
	 */
	public static class MyThread extends Thread{
		
		private String name;
		private int ticket=10;
		
		public MyThread() {  
			super();  
		}
		public MyThread(String name) {  
			super();  
			this.name = name;  
		}
		
		public void run(){  
			for(int i=0;i<10;i++){
				System.err.println("线程开始："+this.name+",i="+i);
			}
		}
		
		
	}
	
	/**
	 * Runnable接口实现多线程
	 * @author shichangjian
	 *
	 */
	public static class MyRunNable implements Runnable {

		private int ticket = 10;

		public void run() {
			for (int i = 0; i < 20; i++) {
				if (this.ticket > 0) {
					System.out.println("卖票：ticket" + this.ticket--);
				}
			}
		}

	}
	
//	public static void main(String[] args) {  
//		MyThread mt1 = new MyThread("线程a");  
//		MyThread mt2 = new MyThread("线程b");  
//		此时结果很有规律，先第一个对象执行，然后第二个对象执行，并没有相互运行
//		mt1.run();  
//		mt2.run();
		
//		多线程交互运行
//		一旦调用start()方法，则会通过JVM找到run()方法。下面启动start()方法启动线程：
//		mt1.start();  
//		mt2.start(); 
		
//		----------------------------Runnable
//		Runnable mt1 = new MyRunNable("线程a");
//		MyRunNable mt2 = new MyRunNable("线程b");
//		但是在使用Runnable定义的子类中没有start()方法，只有Thread类中才有。
//		此时观察Thread类，有一个构造方法：
//		public Thread(Runnable targer)此构造方法接受Runnable的子类实例，
//		也就是说可以通过Thread类来启动Runnable实现的多线程。（start()可以协调系统的资源
//		new Thread(mt1).start(); 
//		new Thread(mt2).start();
		
//		-----------------卖票
//		MyThread mt1 = new MyThread();  
//		MyThread mt2 = new MyThread();  
//		MyThread mt3 = new MyThread();  
		
//		Thread卖票
//		mt1.start();//每个线程都各卖了10张，共卖了30张票  
//		mt2.start();//但实际只有10张票，每个线程都卖自己的票  
//		mt3.start();//没有达到资源共享
		
//		Runnable卖票
//		MyThread mt = new MyThread();  
//		new Thread(mt).start();//同一个mt，但是在Thread中就不可以，如果用同一 个实例化对象mt，就会出现异常
//		new Thread(mt).start(); 
//		new Thread(mt).start();//虽然现在程序中有三个线程，但是一共卖了10张票，也就是说使用Runnable实现多线程可以达到资源共享目的
//		MyRunNable mt=new MyRunNable();  
//		new Thread(mt).start();//同一个mt，但是在Thread中就不可以，如果用同一  
//		new Thread(mt).start();//个实例化对象mt，就会出现异常  
//		new Thread(mt).start();  
//	}  
}
