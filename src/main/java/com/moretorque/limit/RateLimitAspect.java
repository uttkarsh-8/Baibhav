package com.moretorque.limit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class RateLimitAspect {

    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastResetTimes = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object limitRate(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        int limit = rateLimit.value();

        long currentTime = System.currentTimeMillis();
        long lastResetTime = lastResetTimes.getOrDefault(methodName, 0L);

        if (currentTime - lastResetTime > 60000) { // 1 minute
            requestCounts.put(methodName, new AtomicInteger(0));
            lastResetTimes.put(methodName, currentTime);
        }

        AtomicInteger count = requestCounts.computeIfAbsent(methodName, k -> new AtomicInteger(0));
        if (count.incrementAndGet() > limit) {
            throw new RuntimeException("Rate limit exceeded for method: " + methodName);
        }

        return joinPoint.proceed();
    }
}