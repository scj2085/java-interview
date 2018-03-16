package com.java.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ServletComponentScan	//用来扫描@WebFilter 的让@WebFilter起作用,扫描自定义过滤器
public class JavaInterviewApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaInterviewApplication.class, args);
	}
}
