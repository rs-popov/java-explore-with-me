package ru.practicum.ewm.statistics.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.statistics.client.StatisticsClient;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class HitAspect {
    private final StatisticsClient statisticsClient;

    @Pointcut("@annotation(ru.practicum.ewm.statistics.aop.CreatingHit)")
    public void addHit() {
    }

    @AfterReturning(value = "addHit()", returning = "returningValue")
    public void createHit(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
            statisticsClient.addEndpointHit(request.getRemoteAddr(), request.getRequestURI());
        }
    }
}