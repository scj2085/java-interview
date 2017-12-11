package com.java.interview.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.java.interview.thread.LockUsage.ReadWriteLockTest.Account;
import com.java.interview.thread.LockUsage.ReadWriteLockTest.Operation;

/**
 * 
 * @author shichangjian
 *
 */
public class LockUsage {
	
//	----------------------------非公平锁-------------------------------------
//	可重入锁ReentrantLock，多人读、多人写、或同时有人读和写时。只能有一个人能拿到锁，执行代码
	
//	static class NumberWrapper {
//		public int value = 1;
//	}
//
//	public static void main(String[] args) {
//		// 初始化可重入锁
//		//默认非公平锁
////		其里的公平锁的意思是哪个线程先来等待，谁就先获得这个锁。而非公平锁则是看操作系统的调度，有不确定性。一般设置成非公平锁的性能会好很多
//		final Lock lock = new ReentrantLock(); 
//
//		// 第一个条件当屏幕上输出到3
//		final Condition reachThreeCondition = lock.newCondition();
//		// 第二个条件当屏幕上输出到6
//		final Condition reachSixCondition = lock.newCondition();
//
//		// NumberWrapper只是为了封装一个数字，一边可以将数字对象共享，并可以设置为final
//		// 注意这里不要用Integer, Integer 是不可变对象
//		final NumberWrapper num = new NumberWrapper();
//		// 初始化A线程
//		Thread threadA = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// 2，线程A拿到锁1
//				lock.lock();
//				try {
//					System.out.println("threadA start write");
//					// A线程先输出前3个数
//					while (num.value <= 3) {
//						System.out.println(num.value);
//						num.value++;
//					}
//					//4，唤醒一个等待线程B， 告诉B线程可以开始了（唤醒reachThreeCondition此条件的线程）
//					reachThreeCondition.signal();
//				} finally {
//					//5,线程A释放锁1
//					lock.unlock();
//				}
//				//6，线程A拿到锁2
//				lock.lock();
//				try {
//					// 11线程A等待，等待输出6的条件
//					reachSixCondition.await();//使当前线程等待，直到发出信号或中断
//					System.out.println("threadA start write");
//					// 输出剩余数字
//					while (num.value <= 9) {
//						System.out.println(num.value);
//						num.value++;
//					}
//
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} finally {
//					//12线程A释放锁2
//					lock.unlock();
//				}
//			}
//
//		});
//
//		Thread threadB = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					//1,线程B拿到锁1
//					lock.lock();
//
//					while (num.value <= 3) {
//						// 3，线程B等待,(线程B上的reachThreeCondition条件等待，唤醒时也要使用reachThreeCondition此条件唤醒)
//						reachThreeCondition.await();
//					}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} finally {
////					7，线程B释放锁1
//					lock.unlock();
//				}
//				try {
////					8,线程B拿到锁2
//					lock.lock();
//					// 已经收到信号，开始输出4，5，6
//					System.out.println("threadB start write");
//					while (num.value <= 6) {
//						System.out.println(num.value);
//						num.value++;
//					}
//					// 9，唤醒线程A，4，5，6输出完毕，告诉A线程6输出完了
//					reachSixCondition.signal();
//				} finally {
//					//10，线程B释放锁2
//					lock.unlock();
//				}
//			}
//
//		});
//
//		// 启动两个线程
//		threadB.start();
//		threadA.start();
//	}
	
	
//	----------------------------------读写锁----------------
//	能多人同时读，但是又不让它多人写或同时读和写时
	
	
	public static class ReadWriteLockTest {  
		  
		static class Operation implements Runnable{  
		      
		    private Account account;//账号  
		    private String type;  
		      
		    Operation(Account account,String type){  
		        this.account = account;  
		        this.type = type;  
		    }  
		      
		  
		    public void run() {  
		        if ("take".equals(type)) { //每次取100元  
		             //获取写锁    
		            account.getLock().writeLock().lock();  
		            account.setMoney(account.getMoney() -100);  
		            System.out.println( "取走100元,账号"+ account.getAccoutNo()+" 还有"+account.getMoney()+"元");  
		            account.getLock().writeLock().unlock();  
		              
		        }  
		        else if ("query".equals(type)) {  
		             //获取读锁    
		            account.getLock().readLock().lock();  
		            System.out.println( "查询账号"+ account.getAccoutNo()+" 还有"+account.getMoney()+"元");  
		            account.getLock().readLock().unlock();  
		              
		        }  
		        else if ("save".equals(type)) {  
		             //获取写锁    
		            account.getLock().writeLock().lock();  
		            account.setMoney(account.getMoney() + 100);  
		            System.out.println( "存入100元,账号"+ account.getAccoutNo()+" 还有"+account.getMoney()+"元");  
		            account.getLock().writeLock().unlock();  
		        }  
		    }  
		      
		}  
		  
		static class Account  {  
		      
		    private int money;//账号上的钱  
		    private ReadWriteLock lock;//读写锁
		    private String accoutNo;//账号  
		      
		    Account(ReadWriteLock lock,String accoutNo,int money) {  
		        this.lock = lock;  
		        this.accoutNo = accoutNo;  
		        this.money = money;  
		    }  
		      
		    public int getMoney() {  
		        return money;  
		    }  
		  
		    public void setMoney(int money) {  
		        this.money = money;  
		    }  
		  
		    public ReadWriteLock getLock() {  
		        return lock;  
		    }  
		  
		    public void setLock(ReadWriteLock lock) {  
		        this.lock = lock;  
		    }  
		  
		    public String getAccoutNo() {  
		        return accoutNo;  
		    }  
		  
		    public void setAccoutNo(String accoutNo) {  
		        this.accoutNo = accoutNo;  
		    }     
		          
		} 
	} 
	public  static  void main(String[] args) {  
	       //创建一个锁对象 ,非公平锁  ，读写锁
	       ReadWriteLock lock = new ReentrantReadWriteLock(false);    
	       //创建一个线程池    
	       ExecutorService pool = Executors.newCachedThreadPool();   
	       //设置一个账号，设置初始金额为10000  
	       Account account = new Account(lock,"123456",10000);  
	          
	       //账号取钱10次，存钱10次，查询20次  
	       for(int i=1;i<=10;i++) {  
	           Operation operation1 = new Operation(account,"take");  
	           Operation operation2 = new Operation(account,"query");  
	           Operation operation3 = new Operation(account,"save");  
	           Operation operation4 = new Operation(account,"query");  
	           pool.execute(operation1);  
	           pool.execute(operation2);  
	           pool.execute(operation3);  
	           pool.execute(operation4);  
	       }  
	       
//	       	可以关闭 ExecutorService，这将导致其拒绝新任务。
//	      	 线程池运行中可以通过shutdown()和shutdownNow()来改变运行状态。
//	       shutdown()是一个平缓的关闭过程，线程池停止接受新的任务，同时等待已经提交的任务执行完毕，包括那些进入队列还没有开始的任务，这时候线程池处于SHUTDOWN状态；
//	       shutdownNow()是一个立即关闭过程，线程池停止接受新的任务，同时线程池取消所有执行的任务和已经进入队列但是还没有执行的任务，这时候线程池处于STOP状态
	       pool.shutdown();  
	       
//	      	 一旦shutdown()或者shutdownNow()执行完毕，线程池就进入TERMINATED状态，此时线程池就结束了。
//	       isTerminating()描述的是SHUTDOWN和STOP两种状态。
//	       isShutdown()描述的是非RUNNING状态，也就是SHUTDOWN/STOP/TERMINATED三种状态
	       while(!pool.isTerminated()){    
	           //wait for all tasks to finish    
	       }    
	       System.out.println("账号"+ account.getAccoutNo() +",最后金额为："+account.getMoney());    
	   }
}
