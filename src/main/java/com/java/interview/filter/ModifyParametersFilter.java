package com.java.interview.filter;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import com.java.interview.util.DeviceUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类暂时没用
 * @author shichangjian
 *
 */
@Order(4)
@WebFilter(filterName = "modifyParametersFilter", urlPatterns = "/a/*")
public class ModifyParametersFilter extends OncePerRequestFilter {
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ModifyParametersWrapper mParametersWrapper = new ModifyParametersWrapper(request);
        String token = request.getHeader("token");

        String ua = request.getHeader("User-Agent"); //User-Agent 不用区分大小写

        if (DeviceUtils.isMobileDevice(request)) {
            System.err.println("访问设备来自手机");
        }
        if (DeviceUtils.isIOSDevice(request)) {
        	System.err.println("访问设备来自IOS");
        }
        if (DeviceUtils.isWeChat(request)) {
        	System.err.println("访问设备来自微信");
        }
        if (DeviceUtils.ispcFlag(request)) {
        	System.err.println("访问设备来自PC");
        }
//        if (token != null && CheckMobile.check(ua)) {
//        	mParametersWrapper.putHeader("Cookie", "JSESSIONID="+token);
//        }
        filterChain.doFilter(mParametersWrapper, response);
    }

    /**
     * 继承HttpServletRequestWrapper，创建装饰类，以达到修改HttpServletRequest参数的目的
     */
    private class ModifyParametersWrapper extends HttpServletRequestWrapper {
    	
        private final Map<String, String> customHeaders;

        ModifyParametersWrapper(HttpServletRequest request) {
            super(request);
            this.customHeaders = new HashMap<>();
        }

        void putHeader(String name, String value){
            this.customHeaders.put(name, value);
        }

        public String getHeader(String name) {
            // check the custom headers first
            String headerValue = customHeaders.get(name);

            if (headerValue != null){
                return headerValue;
            }
            // else return from into the original wrapped object
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }

        public Enumeration<String> getHeaderNames() {
            // create a set of the custom header names
            Set<String> set = new HashSet<>(customHeaders.keySet());

            // now add the headers from the wrapped request object
            Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
            while (e.hasMoreElements()) {
                // add the names of the request headers into the list
                String n = e.nextElement();
                set.add(n);
            }

            // create an enumeration from the set and return
            return Collections.enumeration(set);
        }
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
            if(matcherPhone.find()){
            	
            }
            boolean a = matcherTable.find();
            boolean b = matcherPhone.find();
            
            return matcherPhone.find() || matcherTable.find();
        }
    }
}
