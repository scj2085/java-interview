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

@Order(3)
@WebFilter(filterName = "tokenAuthorFilter", urlPatterns = "/a/*")
public class TokenAuthorFilter implements Filter{

	private static Logger logger = LoggerFactory.getLogger(TokenAuthorFilter.class);
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;  
        response.setCharacterEncoding("UTF-8");  
        response.setContentType("application/json; charset=utf-8");  
        String token = req.getHeader("token");  
        
        chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	 /**
     * 检测是否为移动端设备访问
     *
     * @author      : Cuichenglong
     * @group       : tgb8
     * @Version     : 1.00
     * @Date        : 2014-7-7 下午01:34:31
     */
    static class CheckMobile {
        // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
        // 字符串在编译时会被转码一次,所以是 "\\b"
        // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
        static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
                +"|windows (phone|ce)|blackberry"
                +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
                +"|laystation portable)|nokia|fennec|htc[-_]"
                +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
        static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
                +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
        //移动设备正则匹配：手机端、平板
        static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
        static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);
        /**
         * 检测是否是移动设备访问
         *
         * @Title: check
         * @Date : 2014-7-7 下午01:29:07
         * @param userAgent 浏览器标识
         * @return true:移动设备接入，false:pc端接入
         */
        static boolean check(String userAgent){
            if(null == userAgent){
                userAgent = "";
            }
            // 匹配
            Matcher matcherPhone = phonePat.matcher(userAgent);
            Matcher matcherTable = tablePat.matcher(userAgent);
            return matcherPhone.find() || matcherTable.find();
        }
    }

}
