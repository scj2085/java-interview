package com.java.interview.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  //RetentionPolicy.RUNTIME这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.
@Target({ElementType.METHOD})  		//@Target说明了Annotation所修饰的对象范围
public @interface LoginAop {
	
	String name() default "";  
    String value() default "";
}
