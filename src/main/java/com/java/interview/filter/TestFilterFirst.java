package com.java.interview.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.Order;

/**
 * springboot过滤器新方法，在启动类添加@ServletComponentScan
 * 
 * @author shichangjian
 *
 */
@Order(2)//过滤顺序，值越小，越先执行
// 重点
@WebFilter(filterName = "testFilter1", urlPatterns = "/*")
public class TestFilterFirst implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		System.err.println("TestFilter1");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
	}

}
