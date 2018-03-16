package com.java.interview.httpclient;
import org.apache.http.client.config.RequestConfig;  
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
  
/**
 * 设置请求的各种配置
 * @author shichangjian
 *
 */
@Configuration 
public class MyRequestConfig {
	
	@Value("${httpclient.config.connectTimeout}")  
    private int connectTimeout = 2000;  //连接超时时间，取得了连接池中的某个连接之后到接通目标url的连接等待时间。发生超时，会抛出ConnectionTimeoutException异常
      
    @Value("${httpclient.config.connectRequestTimeout}")  
    private int connectRequestTimeout = 2000;  //从连接池中取连接的超时时间,从ConnectionManager管理的连接池中取出连接的超时时间,超时则抛出ConnectionPoolTimeoutException异常
      
    @Value("${httpclient.config.socketTimeout}")  
    private int socketTimeout = 2000;  //请求超时时间，连接到服务器之后到从服务器获取响应数据需要等待的时间，连接上一个url之后到获取response的返回等待时间。发生超时，会抛出SocketTimeoutException异常
    @Bean  
    public RequestConfig config(){  
        return RequestConfig.custom()  
                .setConnectionRequestTimeout(this.connectRequestTimeout)  
                .setConnectTimeout(this.connectTimeout)  
                .setSocketTimeout(this.socketTimeout)  
                .build();  
    }
}
