package com.java.interview.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * 实现Web层的日志切面
 * 
 * @author shichangjian
 *
 */
@Aspect
@Component
@Order(1)//同一个方法自定义多个AOP，指定他们的执行顺序呢,order越小越是最先执行，但更重要的是最先执行的最后结束
public class WebLogAspect {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.java.interview.aop.LoginAop)")  
    private void aop() {  
    	System.err.println("定义一个注解切入点");
    }  
      
      
    //声明前置通知  
//    @Before("aop()")  
//    public void doBefore(JoinPoint point) {  
//    	logger.info("进入登录接口之前");  
//    	System.err.println("前置增强方法前==执行顺序3");
//        return;  
//    }  
  
    //声明后置通知  
//    @AfterReturning(pointcut = "aop()", returning = "returnValue")  
//    public void doAfterReturning(JoinPoint point,Object returnValue) {  
//    	logger.info("后置增强方法结束后进入");  
//    	System.err.println("后置增强方法结束后进入==执行顺序7");
//    }  
  
    //声明例外通知  
//    @AfterThrowing(pointcut = "aop()", throwing = "e")  
//    public void doAfterThrowing(Exception e) {  
//    	logger.info("声明例外通知");  
//    	System.err.println("声明例外通知");
//    }  
  
    //声明最终通知  
//    @After("aop()")  
//    public void doAfter() {  
//    	logger.info("最终增强，方法结束后进入");  
//    	System.err.println("最终增强，方法结束后进入==执行顺序6");
//    }  
  
    //声明环绕通知  
//    @Around("aop()")  
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {  
//    	logger.info("环绕增强方法之前进入");  
//    	System.err.println("环绕增强方法之前进入==执行顺序2");
//        Object obj = pjp.proceed();  
//        logger.info("环绕增强方法结束后进入");  
//        System.err.println("环绕增强方法结束后进入==执行顺序4");
//        return obj;  
//    } 
	
    @Around("aop()") 
    public Object beforeExec(ProceedingJoinPoint joinPoint){  
        
        System.err.println("没有从redis中查到数据...执行顺序1");
        Method method = null;
		try {
			method = getMethod(joinPoint);
		} catch (Exception e) {
			logger.error("[error message] 获取注解缓存类，函数，参数异常!");	
		}

		LoginAop loginAop = method.getAnnotation(LoginAop.class);
		System.err.println("参数====" + loginAop.value());
		String value = parseKey(loginAop.value(), method, joinPoint.getArgs());
		System.err.println("参数====" + value);
		
        //没有查到，那么查询数据库
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            
            e.printStackTrace();
        }
        
        System.err.println("从数据库中查询的数据...==执行顺序5");
        if(null != object)
        	System.err.println("返回值===" + object.toString());
        return object;
    }
    
    /**
	 * 获取被拦截方法对象 MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而缓存的注解在实现类的方法上
	 * 所以应该使用反射获取当前对象的方法对象
	 * @throws NoSuchMethodException 
	 */
	public Method getMethod(ProceedingJoinPoint pjp) {
		// 获取参数的类型
		Object[] args = pjp.getArgs();
		Class[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Method method = null;
		try {
			// 只识别类Long，not long
			method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[error message] 获取注解缓存类，函数，参数异常!");	
//			throw new ServiceException("scheduleCacheAspect.ObtainParametersException");
		}
		return method;
	}
	
	/**
	 * 获取缓存的key,key定义在注解上，支持SPEL表达式 ，只识别impl类
	 * 
	 * @param pjp
	 * @return
	 */
	private String parseKey(String key, Method method, Object[] args) {
		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);

		// 使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		try {
			return parser.parseExpression(key).getValue(context, String.class);
		} catch (Exception e) {
			logger.error("[error message] key解析异常");
//			throw new ServiceException("scheduleCacheAspect.keyParseException");
		}
		return null;
	}
}
