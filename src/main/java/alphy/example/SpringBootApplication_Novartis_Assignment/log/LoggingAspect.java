package alphy.example.SpringBootApplication_Novartis_Assignment.log;

//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class LoggingAspect {
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Before("execution(* alphy.example.SpringBootApplication_Novartis_Assignment..*.*(..))")
//    public void logBefore(JoinPoint joinPoint) {
//        logger.info("Entering: " + joinPoint.getSignature().toShortString());
//    }
//
//    @After("execution(* alphy.example.SpringBootApplication_Novartis_Assignment..*.*(..))")
//    public void logAfter(JoinPoint joinPoint) {
//        logger.info("Exiting: " + joinPoint.getSignature().toShortString());
//    }
//}


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    //All public classes
    @Pointcut("execution(public * alphy.example.SpringBootApplication_Novartis_Assignment.*.*(..))")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void logIncomingRequest(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String methodName = joinPoint.getSignature().getName();
        String requestURL = request.getRequestURL().toString();
        String clientIP = request.getRemoteAddr();
        logger.info("Incoming request: Method={}, URL={}, ClientIP={}", methodName, requestURL, clientIP);
        logger.info("Entering: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logOutgoingResponse(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Outgoing response: Method={}, Response={}", methodName, result);
        logger.info("Exiting: " + joinPoint.getSignature().toShortString());
    }
}
