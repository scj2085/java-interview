//package com.java.interview.concurrent;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
///**
// * 使用ExecutorService.invokeAll()方法计算
// *
// * @author Administrator
// *
// */
//public class CountSumWithCallable {
//    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        int threadCounts = 18;
//        long sum = 0;
//        ExecutorService exec = Executors.newFixedThreadPool(threadCounts);
//        List<Callable<Long>> callList = new ArrayList<Callable<Long>>();
//        List<Integer> list = new ArrayList<Integer>();
//        for (int i = 0; i < 1000000; i++) {
//            list.add(i);
//        }
//
//        int len = list.size() / threadCounts;// 分割段数
//        if (len == 0) {
//            threadCounts = list.size();
//            len = list.size() / threadCounts;
//
//        }
//        for (int i = 0; i < threadCounts; i++) {
//            final List<Integer> subList;
//            if (i == threadCounts - 1) {
//                subList = list.subList(i * len, list.size());
//            } else {
//                subList = list.subList(i * len, len * (i + 1) > list.size() ? list.size() : len * (i + 1));
//            }
//            // 采用匿名内部类实现
//            callList.add(new Callable<Long>() {
//                public Long call() throws Exception {
//                    long subSum = 0L;
//                    for (Integer i : subList) {
//                        subSum += i;
//                    }
//                    System.out.println("分配给线程：" + Thread.currentThread().getName() + "那一部分的list的整数和为:" + subSum);
//                    return subSum;
//                }
//            });
//        }
//
//        List<Future<Long>> futureList = exec.invokeAll(callList);
//        for (Future<Long> future : futureList) {
//            sum += future.get();
//        }
//        exec.shutdown();
//        System.out.println("sum =====:" + sum);
//
//    }
//}