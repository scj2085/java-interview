package com.java.interview.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.interview.util.User;

@RestController
public class ClientTest {
	
	long a = 0;
	
    @RequestMapping(value = "/a/httpClientTest")
    public String httpclient(@RequestParam("accountName") String account) throws Exception {
    	a++;
    	System.err.println("被调次数==" + a);
        return "hello" + a; 
    }
    
    @RequestMapping(value = "/httpClientTest2")
    public String httpclient(@RequestBody User user) throws Exception {
    	a++;
    	System.err.println("被调次数==" + a);
    	return "hello" + a; 
    }
    
    
    
    
}
