package com.java.interview.thread;

import java.util.ArrayList;
import java.util.List;

public class ThreadUsage {

//	---------------------------不用join()方法---------------------
//	主线程在子线程之前结束
//	public static class Thread1 extends Thread{  
//	    private String name;  
//	    public Thread1(String name) {  
//	        super(name);  
//	       this.name=name;  
//	    }  
//	    public void run() {  
//	        System.out.println(Thread.currentThread().getName() + " 线程运行开始!");  
//	        for (int i = 0; i < 5; i++) {  
//	            System.out.println("子线程"+name + "运行 : " + i);  
//	            try {  
//	                sleep((int) Math.random() * 10);  
//	            } catch (InterruptedException e) {  
//	                e.printStackTrace();  
//	            }  
//	        }  
//	        System.out.println(Thread.currentThread().getName() + " 线程运行结束!");  
//	    }  
//	}  
	  
	  
//    public static void main(String[] args) {  
//        System.out.println(Thread.currentThread().getName()+"主线程运行开始!");  
//        Thread1 mTh1 = new Thread1("A");  
//        Thread1 mTh2 = new Thread1("B");  
//        mTh1.start();  
//        mTh2.start();  
//        System.out.println(Thread.currentThread().getName()+ "主线程运行结束!");  
//  
//    }  
	  
//	-------------使用join方法--------------------------
//    主线程等待子线程结束之后结束
//	public static class Thread1 extends Thread{  
//	    private String name;  
//	    public Thread1(String name) {  
//	        super(name);  
//	       this.name=name;  
//	    }  
//	    public void run() {  
//	        System.out.println(Thread.currentThread().getName() + " 线程运行开始!");  
//	        for (int i = 0; i < 5; i++) {  
//	            System.out.println("子线程"+name + "运行 : " + i);  
//	            try {  
//	                sleep((int) Math.random() * 10);  
//	            } catch (InterruptedException e) {  
//	                e.printStackTrace();  
//	            }  
//	        }  
//	        System.out.println(Thread.currentThread().getName() + " 线程运行结束!");  
//	    }  
//	}
//    	  
//    public static void main(String[] args) {  
//        System.out.println(Thread.currentThread().getName()+"主线程运行开始!");  
//        Thread1 mTh1=new Thread1("A");  
//        Thread1 mTh2=new Thread1("B");  
//        mTh1.start();  
//        mTh2.start();  
//        try {  
//            mTh1.join();  
//        } catch (InterruptedException e) {  
//            e.printStackTrace();  
//        }  
//        try {  
//            mTh2.join();  
//        } catch (InterruptedException e) {  
//            e.printStackTrace();  
//        }  
//        System.out.println(Thread.currentThread().getName()+ "主线程运行结束!");  
//  
//    }  
      
    
//    -------------------yield():暂停当前正在执行的线程对象，并执行其他线程------------
//	yield()应该做的是让当前运行线程回到可运行状态，以允许具有相同优先级的其他线程获得运行机会。
//	因此，使用yield()的目的是让相同优先级的线程之间能适当的轮转执行。
//	但是，实际中无法保证yield()达到让步目的，因为让步的线程还有可能被线程调度程序再次选中
//	yield()从未导致线程转到等待/睡眠/阻塞状态。在大多数情况下，yield()将导致线程从运行状态转到可运行状态，但有可能没有效果
    
//	sleep()和yield()的区别):sleep()使当前线程进入停滞状态，所以执行sleep()的线程在指定的时间内肯定不会被执行；
//	yield()只是使当前线程重新回到可执行状态，所以执行yield()的线程有可能在进入到可执行状态后马上又被执行
	
//	static class ThreadYield extends Thread{  
//        public ThreadYield(String name) {  
//            super(name);  
//        }  
//       
//        @Override  
//        public void run() {  
//            for (int i = 1; i <= 50; i++) {  
//                System.out.println("" + this.getName() + "-----" + i);  
//                // 当i为30时，该线程就会把CPU时间让掉，让其他或者自己的线程执行（也就是谁先抢到谁执行）  
//                if (i ==30) {  
//                    this.yield();  
//                }  
//            }  
//          
//    }  
//    }  
//      
//      
//    public static void main(String[] args) {  
//          
//        ThreadYield yt1 = new ThreadYield("张三");  
//        ThreadYield yt2 = new ThreadYield("李四");  
//        yt1.start();  
//        yt2.start();  
//    }  
      
//	----------------------------setPriority(): 更改线程的优先级-------------
    
//    MIN_PRIORITY = 1
//    NORM_PRIORITY = 5
//    MAX_PRIORITY = 10
	
//    用法：
//    Thread4 t1 = new Thread4("t1");
//    Thread4 t2 = new Thread4("t2");
//    t1.setPriority(Thread.MAX_PRIORITY);
//    t2.setPriority(Thread.MIN_PRIORITY);
    
    
//    --------------------wait(释放对象锁，并线程休眠)和notify(释放对象锁，jvm从wait()唤醒休眠线程)---
//    Obj.wait()，与Obj.notify()必须要与synchronized(Obj)一起使用，
//    也就是wait,与notify是针对已经获取了Obj锁进行操作，
//    从语法角度来说就是Obj.wait(),Obj.notify必须在synchronized(Obj){...}语句块内。
//    从功能上来说wait就是说线程在获取对象锁后，主动释放对象锁，同时本线程休眠。直到有其它线程调用对象的notify()唤醒该线程，
//    才能继续获取对象锁，并继续执行。相应的notify()就是对对象锁的唤醒操作。
//    但有一点需要注意的是notify()调用后，并不是马上就释放对象锁的，而是在相应的synchronized(){}语句块执行结束，自动释放锁后，
//    JVM会在wait()对象锁的线程中随机选取一线程，赋予其对象锁，唤醒线程，继续执行。这样就提供了在线程间同步、唤醒的操作。
//    Thread.sleep()与Object.wait()二者都可以暂停当前线程，释放CPU控制权，主要的区别在于Object.wait()在释放CPU同时，
//    释放了对象锁的控制
    
    
//    public static class MyThreadPrinter2 implements Runnable {     
//        
//        private String name;     
//        private Object prev;     
//        private Object self;     
//        
//        private MyThreadPrinter2(String name, Object prev, Object self) {     
//            this.name = name;     
//            this.prev = prev;     
//            this.self = self;     
//        }     
//        
//        @Override    
//        public void run() {     
//            int count = 10;     
//            while (count > 0) {     
//                synchronized (prev) {     
//                    synchronized (self) {     
//                        System.out.print(name);     
//                        count--;    
//                          
////                      相应的synchronized(){}语句块执行结束，自动释放锁后 ,
////                      JVM会在wait()对象锁的线程中随机选取一线程，赋予其对象锁，唤醒线程，继续执行
////                      顺序：线程1——》锁对象c，a,在此处释放a对象锁，同时在wait()中还没有要唤醒的线程(c对象锁)，执行prev.wait()时，释放对象c锁，并且线程1休眠，等待其它线程调用c对象的c.notify()方法，唤醒线程1
////                        线程2——》锁对象a,b,在此处释放b对象锁，同时在wait()中还没有要唤醒的线程(截止现在只有线程1的c对象锁)，执行prev.wait()时，释放对象a锁，并且线程2休眠，等待其它线程调用a对象的a.notify()方法，唤醒线程2
////                        线程3——》锁对象b,c,在此处释放c对象锁，同时在wait()中(截止现在有线程1的c对象锁,线程2的a对象锁)唤醒c对象锁的线程1，执行prev.wait()时，释放对象b锁，并且线程3休眠，等待其它线程调用b对象的b.notify()方法，唤醒线程3
////                        线程1——》此线程被线程3的c对象唤醒，锁对象c，a,在此处释放a对象锁，同时在wait()中(截止现在有线程3的b对象锁,线程2的a对象锁)唤醒a对象锁的线程2，执行prev.wait()时，释放对象c锁，并且线程1休眠，等待其它线程调用c对象的c.notify()方法，唤醒线程1
////                        以此类推，循环打印abc
//                        self.notify();    
//                    }     
//                    try {     
//                        prev.wait();   //主动释放对象锁，同时本线程休眠  
//                    } catch (InterruptedException e) {     
//                        e.printStackTrace();     
//                    }     
//                }     
//        
//            }     
//        }     
//        
//    } 
//
//	public static void main(String[] args) throws Exception {
//		Object a = new Object();
//		Object b = new Object();
//		Object c = new Object();
//		MyThreadPrinter2 pa1 = new MyThreadPrinter2("A", c, a);
//		MyThreadPrinter2 pb2 = new MyThreadPrinter2("B", a, b);
//		MyThreadPrinter2 pc3 = new MyThreadPrinter2("C", b, c);
//
//		new Thread(pa1).start();
//		Thread.sleep(100); // 确保按顺序A、B、C执行
//		new Thread(pb2).start();
//		Thread.sleep(100);
//		new Thread(pc3).start();
//		Thread.sleep(100);
//	}
	
	
//	----------------------通过构造方法传递数据-------------------
	
//	public static class MyThread1 extends Thread {
//		private String name;
//
//		public MyThread1(String name) {
//			this.name = name;
//		}
//
//		public void run() {
//			System.out.println("hello " + name);
//		}
//
//	}
//	public static void main(String[] args) {
//		Thread thread = new MyThread1("world");
//		thread.start();
//	}
	
	
//	----------------------通过变量传递数据-----------------------------
//	public static class MyThread2 implements Runnable {
//		private String name;
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public void run() {
//			System.out.println("hello " + name);
//		}
//
//	}
//
//	public static void main(String[] args) {
//		MyThread2 myThread = new MyThread2();
//		myThread.setName("world");
//		Thread thread = new Thread(myThread);
//		thread.start();
//	}
	
	
//	----------------------------通过回调函数传递数据-------------------------
	
	static class Data {
		public int value = 0;
	}

	static class Work {
		public void process(Data data, List<Integer> numbers) {
			for (int n : numbers) {
				data.value += n;
			}
		}
	}

	public static class MyThread3 extends Thread {
		private Work work;

		public MyThread3(Work work) {
			this.work = work;
		}

		public void run() {
			java.util.Random random = new java.util.Random();
			Data data = new Data();
			int n1 = random.nextInt(1000);
			int n2 = random.nextInt(2000);
			int n3 = random.nextInt(3000);
			List<Integer> n = new ArrayList<Integer>();
			n.add(n1);
			n.add(n2);
			n.add(n3);
			work.process(data, n); // 使用回调函数
			System.out.println(
					String.valueOf(n1) + "+" + String.valueOf(n2) + "+" + String.valueOf(n3) + "=" + data.value);
		}

	}
	
//	public static void main(String[] args) {
//		Thread thread = new MyThread3(new Work());
//		thread.start();
//	}
    
}
