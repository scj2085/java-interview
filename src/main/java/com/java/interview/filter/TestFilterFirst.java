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
	
	//	它在第一次创建 Servlet 时被调用，在后续每次用户请求时不再调用
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		System.err.println("TestFilter1");
		// 把请求传回过滤链
		filterChain.doFilter(servletRequest, servletResponse);
	}

	//destroy() 方法只会被调用一次，在 Servlet 生命周期结束时被调用。destroy() 方法可以让您的 Servlet 关闭数据库连接、停止后台线程、把 Cookie 列表或点击计数器写入到磁盘，并执行其他类似的清理活动
	@Override
	public void destroy() {
	}

}
