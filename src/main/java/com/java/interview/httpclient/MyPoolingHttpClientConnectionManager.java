package com.java.interview.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用来管理客户端的连接池，并且可以为多个线程的请求提供服务
 * @author shichangjian
 *
 */
@Configuration
public class MyPoolingHttpClientConnectionManager {
	
	/**
	 * 连接池最大连接数
	 */
	@Value("${httpclient.config.connMaxTotal}")
	private int connMaxTotal = 20;

	/** 
	 *  如果所有的连接请求都是到同一个url，那可以把MaxPerRoute的值设置成和MaxTotal一致，这样就能更高效地复用连接
	 */
	@Value("${httpclient.config.maxPerRoute}")
	private int maxPerRoute = 20;

	/**
	 * 连接存活时间，单位为s
	 */
	@Value("${httpclient.config.timeToLive}")
	private int timeToLive = 60;

	@Bean
	public PoolingHttpClientConnectionManager poolingClientConnectionManager() {
		PoolingHttpClientConnectionManager poolHttpcConnManager = new PoolingHttpClientConnectionManager(60,
				TimeUnit.SECONDS);
		// 最大连接数
		poolHttpcConnManager.setMaxTotal(this.connMaxTotal);
		// 路由基数
		poolHttpcConnManager.setDefaultMaxPerRoute(this.maxPerRoute);
		return poolHttpcConnManager;
	}
}
