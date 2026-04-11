package com.example.demo.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    @Around("@annotation(com.example.demo.annotation.tracktime)")
    public Object monitorTime(ProceedingJoinPoint joinPoint ) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println(">>> [PERF] " + joinPoint.getSignature().getName() + " took " + (end - start) + "ms");

        return result;
    }
}
