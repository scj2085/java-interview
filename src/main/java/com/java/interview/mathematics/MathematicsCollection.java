package com.java.interview.mathematics;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MathematicsCollection {

	public static void main(String[] args) {
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		
		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(1);
		list2.add(2);
		list2.add(3);
		list2.add(4);
		list2.add(5);
		
		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(1);
		
		System.err.println("contains:集合包含单个元素" + list2.contains(list1));
		System.err.println("contains:集合包含单个元素" + list2.contains(list1));
		System.err.println("contains:集合包含单个元素" + list1.contains(list3));
		System.err.println("contains:集合包含单个元素" + list1.contains(1));
		System.err.println("contains:集合包含单个元素" + list2.contains(5));
		System.err.println("containsAll:集合包含集合" + list2.containsAll(list1));
		
		
		Set set = new HashSet();
		set.add(new Date()); 
		set.add("apple"); 
		set.add(new Socket()); 
		set.add("TV"); 

		List list4 = new ArrayList();
		list4.add("apple"); 
		list4.add("TV"); 

		System.err.println("containsAll:集合包含集合" + set.containsAll(list4));
		
	}
}
