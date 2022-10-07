package ru.practicum.ewm.client;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;

/**
 * @author rs-popov
 * Класс-аспект для реализации логики по добавлению событий при
 * создании (аннотация @CretingEvent), удалении (аннотация @RemovingEvent)
 * и обновлении (аннотация @UpdatingEvent)
 **/

@Aspect
@Component
@RequiredArgsConstructor
public class HitAspect {

    private final StatisticsClient statisticsClient;
    @Pointcut("@annotation(ru.practicum.ewm.client.CreatingHit)")
    public void addHit() {
    }

    @AfterReturning(value = "addHit()", returning = "returningValue")
    public void createAddHit(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
            statisticsClient.addEndpointHit(request.getRemoteAddr(), request.getRequestURI());

        }
    }

}