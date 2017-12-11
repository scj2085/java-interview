package com.java.interview.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 同步线程
 * synchronized是Java中的关键字，是一种同步锁。它修饰的对象有以下几种： 
 *	1. 修饰一个代码块，被修饰的代码块称为同步语句块，其作用的范围是大括号{}括起来的代码，作用的对象是调用这个代码块的对象； 
 *	2. 修饰一个方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象； 
 *	3. 修改一个静态的方法，其作用的范围是整个静态方法，作用的对象是这个类的所有对象； 
 *	4. 修改一个类，其作用的范围是synchronized后面括号括起来的部分，作用主的对象是这个类的所有对象。
 * 
 * @author shichangjian
 *
 */
public class Synchronized implements Runnable{

	private static int count;

	public Synchronized() {
	      count = 0;
	   }

	public void run() {
		synchronized (this) {
			for (int i = 0; i < 5; i++) {
				try {
					System.out.println(Thread.currentThread().getName() + ":" + (count++));
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int getCount() {
		return count;
	}

	
//	public static void main(String[] args) {
	
	
//	------------------------两个并发线程访问同一个对象----------------------------------
	
	
	
////		当两个并发线程(thread1和thread2)访问同一个对象(syncThread)中的synchronized代码块时，
////		在同一时刻只能有一个线程得到执行，另一个线程受阻塞，必须等待当前线程执行完这个代码块以后才能执行该代码块。
////		Thread1和thread2是互斥的，因为在执行synchronized代码块时会锁定当前的对象，
////		只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象
//		Synchronized syncThread = new Synchronized();
//		Thread thread1 = new Thread(syncThread, "SyncThread1");
//		Thread thread2 = new Thread(syncThread, "SyncThread2");
//		thread1.start();
//		thread2.start();
		
		
//		---------------------------两个线程锁两个对象---------------------------
		
////		这时创建了两个SyncThread的对象syncThread1和syncThread2，线程thread1执行的是syncThread1对象中的synchronized代码(run)，
////		而线程thread2执行的是syncThread2对象中的synchronized代码(run)；我们知道synchronized锁定的是对象，这时会有两把锁分别锁定syncThread1对象和syncThread2对象，
////		而这两把锁是互不干扰的，不形成互斥，所以两个线程可以同时执行
//		Synchronized syncThread1 = new Synchronized();
//		Synchronized syncThread2 = new Synchronized();
//		Thread thread1 = new Thread(syncThread1, "SyncThread1");
//		Thread thread2 = new Thread(syncThread2, "SyncThread2");
//		thread1.start();
//		thread2.start();;
		
		
		
//	}

//		---------------------多个线程访问synchronized和非synchronized代码块----------------------------------
	
//   public void countAdd() {
//	      synchronized(this) {
//	         for (int i = 0; i < 5; i ++) {
//	            try {
//	               System.out.println(Thread.currentThread().getName() + ":" + (count++));
//	               Thread.sleep(100);
//	            } catch (InterruptedException e) {
//	               e.printStackTrace();
//	            }
//	         }
//	      }
//	   }
//	
//	// 非synchronized代码块，未对count进行读写操作，所以可以不用synchronized
//	public void printCount() {
//		for (int i = 0; i < 5; i++) {
//			try {
//				System.out.println(Thread.currentThread().getName() + " count:" + count);
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public void run() {
//		String threadName = Thread.currentThread().getName();
//		if (threadName.equals("A")) {
//			countAdd();
//		} else if (threadName.equals("B")) {
//			printCount();
//		}
//	}
//	
//	public static void main(String[] args) {
////		上面代码中countAdd是一个synchronized的，printCount是非synchronized的。
////		从上面的结果中可以看出一个线程访问一个对象的synchronized代码块时，别的线程可以访问该对象的非synchronized代码块而不受阻塞
//		Synchronized counter = new Synchronized();
//		Thread thread1 = new Thread(counter, "A");
//		Thread thread2 = new Thread(counter, "B");
//		thread1.start();
//		thread2.start();
//		
//	}
	
	
	
	
	
//	--------------------------指定要给某个对象加锁----------------------------
		
	
	
//	/**
//	 * 银行账户类
//	 */
//	static class Account {
//		
//	   String name;
//	   float amount;
//
//	   public Account(String name, float amount) {
//	      this.name = name;
//	      this.amount = amount;
//	   }
//	   //存钱
//	   public  void deposit(float amt) {
//	      amount += amt;
//	      try {
//	         Thread.sleep(100);
//	      } catch (InterruptedException e) {
//	         e.printStackTrace();
//	      }
//	   }
//	   
//	   //取钱
//	   public  void withdraw(float amt) {
//	      amount -= amt;
//	      try {
//	         Thread.sleep(100);
//	      } catch (InterruptedException e) {
//	         e.printStackTrace();
//	      }
//	   }
//
//	   public float getBalance() {
//	      return amount;
//	   }
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	private Account account;
//
//	public Synchronized(Account account) {
//		this.account = account;
//	}
//
//	public void run() {
//		synchronized (account) {
//			account.deposit(500);
//			account.withdraw(500);
//			System.out.println(Thread.currentThread().getName() + ":" + account.getBalance());
//		}
//	}
//	
//	public static void main(String[] args) {
////		在AccountOperator 类中的run方法里，我们用synchronized 给account对象加了锁。
////		这时，当一个线程访问account对象时，其他试图访问account对象的线程将会阻塞，直到该线程访问account对象结束。
////		也就是说谁拿到那个锁谁就可以运行它所控制的那段代码
//		Account account = new Account("zhang san", 10000.0f);
//		Synchronized accountOperator = new Synchronized(account);
//
//		final int THREAD_NUM = 5;
//		Thread threads[] = new Thread[THREAD_NUM];
//		for (int i = 0; i < THREAD_NUM; i++) {
//			threads[i] = new Thread(accountOperator, "Thread" + i);
//			threads[i].start();
//		}
//	}
	
	
//	--------------------当没有一个明确的对象作为锁，只是想让一段代码同步时，可以创建一个特殊的对象来充当锁----------

	
//	class Test implements Runnable {
//		// 零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：生成零长度的byte[]对象只需3条操作码，
//		// 而Object lock = new Object()则需要7行操作码
//		private byte[] lock = new byte[0]; // 特殊的instance变量
//
//		public void method() {
//			synchronized (lock) {
//				// todo 同步代码块
//			}
//		}
//
//		public void run() {
//
//		}
//	}
	
	
//	-------------------Synchronized修饰一个方法-------------------------
////	在定义接口方法时不能使用synchronized关键字。
////	构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步
//	
//	
//	public synchronized void run() {
//		for (int i = 0; i < 5; i++) {
//			try {
//				System.out.println(Thread.currentThread().getName() + ":" + (count++));
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
////	虽然可以使用synchronized来定义方法，但synchronized并不属于方法定义的一部分，
////	因此，synchronized关键字不能被继承。如果在父类中的某个方法使用了synchronized关键字，
////	而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，而必须显式地在子类的这个方法中加上synchronized关键字才可以。
////	当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，但子类调用了父类的同步方法，
////	因此，子类的方法也就相当于同步了
//	
//	class Parent {
//		public synchronized void method() { }
//	}
//	class Child extends Parent {
//		public synchronized void method() { }
//	}
//	
////	在子类方法中调用父类的同步方法
//	class Parent2 {
//		public synchronized void method() {
//		}
//	}
//
//	class Child2 extends Parent2 {
//		public void method() {
//			super.method();
//		}
//	} 
	
	
	
//	---------------------------修饰一个静态方法--------------------------
//	我们知道静态方法是属于类的而不属于对象的。同样的，synchronized修饰的静态方法锁定的是这个类的所有对象
	
//	/**
//	 * 同步线程
//	 */
//	class SyncThread implements Runnable {
//	   private static int count;
//
//	   public SyncThread() {
//	      count = 0;
//	   }
//
//	   public synchronized static void method() {
//	      for (int i = 0; i < 5; i ++) {
//	         try {
//	            System.out.println(Thread.currentThread().getName() + ":" + (count++));
//	            Thread.sleep(100);
//	         } catch (InterruptedException e) {
//	            e.printStackTrace();
//	         }
//	      }
//	   }
//
//	   public synchronized void run() {
//	      method();
//	   }
//	}
	
	
	
//	-----------------------修饰一个类-----------------------------
	
//	/**
//	 * 同步线程
//	 */
//	class SyncThread implements Runnable {
//	   private static int count;
//
//	   public SyncThread() {
//	      count = 0;
//	   }
//
//	   public static void method() {
//	      synchronized(SyncThread.class) {
//	         for (int i = 0; i < 5; i ++) {
//	            try {
//	               System.out.println(Thread.currentThread().getName() + ":" + (count++));
//	               Thread.sleep(100);
//	            } catch (InterruptedException e) {
//	               e.printStackTrace();
//	            }
//	         }
//	      }
//	   }
//
//	   public synchronized void run() {
//	      method();
//	   }
//	}
	
	
	
//	--------------------------总结--------------------------
	
//	A. 无论synchronized关键字加在方法上还是对象上，如果它作用的对象是非静态的，则它取得的锁是对象；如果synchronized作用的对象是一个静态方法或一个类，则它取得的锁是对类，该类所有的对象同一把锁。 
//	B. 每个对象只有一个锁（lock）与之相关联，谁拿到这个锁谁就可以运行它所控制的那段代码。 
//	C. 实现同步是要很大的系统开销作为代价的，甚至可能造成死锁，所以尽量避免无谓的同步控制
}
