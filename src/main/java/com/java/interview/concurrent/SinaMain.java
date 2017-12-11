package com.java.interview.concurrent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author mengxr
 * @Package com.sina.face
 * @Description:
 * @date 2017/11/29 下午2:03
 */
public class SinaMain {
	
	 private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {  
		  
       @Override    
       public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {    
           try {  
               // 核心改造点，由blockingqueue的offer改成put阻塞方法  
//           	当提交任务被拒绝时，进入拒绝机制，我们实现拒绝方法，把任务重新用阻塞提交方法put提交，实现阻塞提交任务功能，防止队列过大，OOM（内存泄漏和溢出）
               executor.getQueue().put(r);  
           } catch (InterruptedException e) {  
               e.printStackTrace();  
           }  
       }
	 }
	public static void main(String[] args) {
		
		ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d").build();
		ExecutorService pool = 
				new ThreadPoolExecutor(
						2, 
						9, 
						30, 
						TimeUnit.MILLISECONDS,
						new ArrayBlockingQueue<Runnable>(60),
						nameThreadFactory, 
						(RejectedExecutionHandler) new CustomRejectedExecutionHandler());
		//单个线程
//		ExecutorService pool = new ConcurrentUtil().newSingleThreadExecutor();
				
				
		long starttime = System.currentTimeMillis();
		System.out.println("开始时间：" + starttime);
		SinaUtils sinaUtils = new SinaUtils();
		sinaUtils.countJavaSource(pool, "E://");
		pool.shutdown();//让所有的入队任务都执行完毕
		while (true) {
			if (pool.isTerminated()) {
				long endtime = System.currentTimeMillis();
				System.out.println("结束时间：" + endtime);
				System.out.println("总用时间：" + (endtime - starttime) / 1000);
				System.out.println("文件个数：" + SinaUtils.filenum);
				System.out.println("代码行数" + SinaUtils.codeline + "行\t注释行数" + SinaUtils.commentline);
				break;
			}
		}
	}
	
	public static class SinaUtils {
	    public static AtomicLong codeline = new AtomicLong();
	    public static AtomicLong commentline = new AtomicLong();//注释行数
	    public static AtomicLong filenum = new AtomicLong();//注释行数
	    /**
	     * 统计Java源代码行数
	     *
	     * @param filename 文件名称
	     */
	    public  void countJavaSource( ExecutorService pool,String filename) {
	        File file = new File(filename);
	        if (!file.exists()) {
	            System.out.println("文件不存在！");
	            System.exit(0);
	        } else {
	            countJavaFile(pool, file);

	        }
	    }

	    /**
	     * 递归目录便利java文件
	     *
	     * @param file
	     */
	    private  void countJavaFile(ExecutorService pool, final File file) {
	    	if(null != file){
	    		if (file.isDirectory()) {
	                File[] files = file.listFiles();
	                if(null != files){
	                	for (int i = 0; i < files.length; i++) {
	                		countJavaFile(pool, files[i]);//递归
	                	}
	                }
	            }else if(file.isFile()){
	                if (file.getName().endsWith(".java")) {
	                    pool.execute(new Runnable() {
	                        @Override
	                        public void run() {
	                            filenum.incrementAndGet();
	                            
//	                            file不能编译：Cannot refer to the non-final local variable file defined in an enclosing scope
//	                            匿名内部类和局部内部类只能访问final变量。
//	                            解决方法：
//	                            在参数前加上final，即onScanResult(int callbackType, final ScanResult result)
	                               countJava(file); 
	                               System.out.println("执行线程名称路径：" + file.getAbsolutePath() + "    当前线程" + Thread.currentThread().getName());
	                        }
	                    });
	                }
	            }
	    	}
	    }

	    /**
	     * 统计代码行数个数
	     *
	     * @param file
	     */
	    private  void countJava(File file) {
	        BufferedReader br = null;
	        try {
	            br = new BufferedReader(new FileReader(file));
	            String temp = "";
	            boolean isComment = false;
	            while ((temp = br.readLine()) != null) {
	                temp = temp.trim();
	                if (temp.startsWith("/*") && !temp.endsWith("*/")) {
	                    isComment = true;
	                    // ++commentline;
	                    commentline.incrementAndGet();
	                } else if (isComment == true && !temp.endsWith("*/")) {  //没到注释多行注释结尾计算
	                    // ++commentline;
	                    commentline.incrementAndGet();
	                } else if (isComment == true && temp.endsWith("*/")) {  //到注释结尾计算
	                    isComment = false;
	                    //++commentline;
	                    commentline.incrementAndGet();
	                } else if (temp.startsWith("//")) {
	                    // ++commentline;
	                    commentline.incrementAndGet();
	                } else if (!"".equals(temp)) {
	                    //++codeline;
	                    codeline.incrementAndGet();
	                }
	            }
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        } finally {
	            try {
	                br.close();
	            } catch (IOException e) {
	                System.out.println(e.getMessage());
	            }
	        }
	    }
	}
}
