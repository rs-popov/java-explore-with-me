package ru.practicum.ewm.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.event.model.dto.EventOutputDto;
import ru.practicum.ewm.user.model.dto.UserOutputDto;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("@annotation(ru.practicum.ewm.logging.CreationLogging)")
    public void loggingCreate() {
    }

    @AfterReturning(value = "loggingCreate()", returning = "returningValue")
    public void addLogOfCreate(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            if (returningValue instanceof Category) {
                Category category = (Category) returningValue;
                log.info("New category (name = {}) was created with id={}", category.getName(), category.getId());
            }
            if (returningValue instanceof Compilation) {
                Compilation compilation = (Compilation) returningValue;
                log.info("New compilation (title = {}) was created with id={}", compilation.getTitle(), compilation.getId());
            }
            if (returningValue instanceof EventOutputDto) {
                EventOutputDto event = (EventOutputDto) returningValue;
                log.info("New event (title = {}) was created with id={}", event.getTitle(), event.getId());
            }
            if (returningValue instanceof UserOutputDto) {
                UserOutputDto user = (UserOutputDto) returningValue;
                log.info("New user (email = {}) was created with id={}", user.getEmail(), user.getId());
            }
        }
    }

    @Pointcut("@annotation(ru.practicum.ewm.logging.DeletionLogging)")
    public void loggingRemove() {
    }

    @AfterReturning(value = "loggingRemove()", returning = "returningValue")
    public void addLogOfRemove(JoinPoint joinPoint, Object returningValue) {
        log.info("Object {} was removed id={}", joinPoint.getSignature().getName().substring(6), joinPoint.getArgs()[0]);
    }

    @Pointcut("@annotation(ru.practicum.ewm.logging.UpdateLogging)")
    public void loggingUpdate() {
    }

    @AfterReturning(value = "loggingUpdate()", returning = "returningValue")
    public void addLogOfUpdate(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            if (returningValue instanceof Category) {
                Category category = (Category) returningValue;
                log.info("Category id={} was updated.", category.getId());
            }
            if (returningValue instanceof Compilation) {
                Compilation compilation = (Compilation) returningValue;
                log.info("Compilation id={} was updated.", compilation.getId());
            }
            if (returningValue instanceof EventOutputDto) {
                EventOutputDto event = (EventOutputDto) returningValue;
                log.info("Event id={} was updated or changed in method {}.", event.getId(), joinPoint.getSignature().getName());
            }
        }
    }
}