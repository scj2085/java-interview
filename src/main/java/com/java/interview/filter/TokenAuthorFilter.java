package com.java.interview.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.java.interview.filter.ModifyParametersFilter.CheckMobile;
import com.java.interview.util.DeviceUtils;

@Order(3)
@WebFilter(filterName = "tokenAuthorFilter", urlPatterns = "/login/*")
public class TokenAuthorFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(TokenAuthorFilter.class);
	// 定义一个全局登录总点击量
	private long hitCount;
	// 定义一个全局微信点击量
	private long weChatCount;
	// 定义一个全局手机点击量
	private long mobileCount;
	// 定义一个全局ios点击量
	private long iosCount;
	// 定义一个全局pc点击量
	private long pcCount;

	// 它在第一次创建 Servlet 时被调用，在后续每次用户请求时不再调用
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 查询DB
		hitCount = 12;
		weChatCount = 1;
		mobileCount = 2;
		iosCount = 3;
		pcCount = 4;

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		 HttpServletRequest req = (HttpServletRequest)request;
		// response.setCharacterEncoding("UTF-8");
		// response.setContentType("application/json; charset=utf-8");
		// String token = req.getHeader("token");

		// 把计数器的值增加 1
		hitCount++;
		// 输出计数器
		System.err.println("登录访问统计：" + hitCount);
		
		if (DeviceUtils.isMobileDevice(req)) {
	        System.err.println("访问设备来自手机");
        }
        if (DeviceUtils.isIOSDevice(req)) {
        	System.err.println("访问设备来自IOS");
        }
        if (DeviceUtils.isWeChat(req)) {
        	System.err.println("访问设备来自微信");
        }
        if (DeviceUtils.ispcFlag(req)) {
        	System.err.println("访问设备来自PC");
        }
		
		
		
		
		

		// 把请求传回过滤链
		chain.doFilter(request, response);
	}

	// 在 Filter 实例被 Web 容器从服务移除之前调用，destroy() 方法只会被调用一次，在 Servlet
	// 生命周期结束时被调用。destroy() 方法可以让您的 Servlet 关闭数据库连接、停止后台线程、把 Cookie
	// 列表或点击计数器写入到磁盘，并执行其他类似的清理活动
	@Override
	public void destroy() {
		//点击量保存到数据库
		
	}

	/**
	 * 检测是否为移动端设备访问
	 *
	 * @author : Cuichenglong
	 * @group : tgb8
	 * @Version : 1.00
	 * @Date : 2014-7-7 下午01:34:31
	 */
	static class CheckMobile {
		// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
		// 字符串在编译时会被转码一次,所以是 "\\b"
		// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
		static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry"
				+ "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" + "|laystation portable)|nokia|fennec|htc[-_]"
				+ "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
		static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
		// 移动设备正则匹配：手机端、平板
		static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
		static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

		/**
		 * 检测是否是移动设备访问
		 *
		 * @Title: check
		 * @Date : 2014-7-7 下午01:29:07
		 * @param userAgent
		 *            浏览器标识
		 * @return true:移动设备接入，false:pc端接入
		 */
		static boolean check(String userAgent) {
			if (null == userAgent) {
				userAgent = "";
			}
			// 匹配
			Matcher matcherPhone = phonePat.matcher(userAgent);
			Matcher matcherTable = tablePat.matcher(userAgent);
			return matcherPhone.find() || matcherTable.find();
		}
	}

}
