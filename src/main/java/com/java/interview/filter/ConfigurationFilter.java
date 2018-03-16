package com.java.interview.filter;

import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * springboot过滤器老方法
 * @author shichangjian
 *
 */
@Configuration
public class ConfigurationFilter {
	
	@Bean
	public FilterRegistrationBean testFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new MyFilter());// 添加过滤器
//		registration.addUrlPatterns("/*");// 设置过滤路径，/*所有路径
		registration.addUrlPatterns("/a/*");// 设置过滤路径，/*所有路径
		registration.addInitParameter("name", "alue");// 添加默认参数
		registration.setName("MyFilter");// 设置
		registration.setOrder(1);// 设置优先级
		return registration;
	}

	public class MyFilter implements Filter {
		@Override
		public void destroy() {
		}

		//在controller前执行，在servlet被调用之前截获;
		@Override
		public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain filterChain)
				throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) srequest;
			// 打印请求Url
			System.err.println("this is MyFilter,url :" + request.getRequestURI());
			filterChain.doFilter(srequest, sresponse);
		}

		@Override
		public void init(FilterConfig arg0) throws ServletException {
		}
	}
}
