package ru.practicum.ewm.loggingAop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.event.model.dto.EventOutputDto;
import ru.practicum.ewm.user.model.User;

//TODO названия методов
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("@annotation(ru.practicum.ewm.loggingAop.CreationLogging)")
    public void loggingCreate() {
    }

    @AfterReturning(value = "loggingCreate()", returning = "returningValue")
    public void addLogOfCreate(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            Object o = returningValue;
            if (o instanceof Category) {
                Category category = (Category) o;
                log.info("New category name = {} was created with id={}", category.getName(), category.getId());
            }
            if (o instanceof Compilation) {
                Compilation compilation = (Compilation) o;
                log.info("New compilation title = {} was created with id={}", compilation.getTitle(), compilation.getId());
            }
            if (o instanceof EventOutputDto) {
                EventOutputDto event = (EventOutputDto) o;
                log.info("New event title = {} was created with id={}", event.getTitle(), event.getId());
            }
            if (o instanceof User) {
                User user = (User) o;
                log.info("New user email = {} was created with id={}", user.getEmail(), user.getId());
            }
        }
    }

    @Pointcut("@annotation(ru.practicum.ewm.loggingAop.DeletionLogging)")
    public void loggingRemove() {
    }

    @AfterReturning(value = "loggingRemove()", returning = "returningValue")
    public void addLogOfRemove(JoinPoint joinPoint, Object returningValue) {
        log.info("Object {} was removed id={}", joinPoint.getSignature().getName().substring(6), joinPoint.getArgs()[0]);
    }

    @Pointcut("@annotation(ru.practicum.ewm.loggingAop.UpdateLogging)")
    public void loggingUpdate() {
    }

    @AfterReturning(value = "loggingUpdate()", returning = "returningValue")
    public void addLogOfUpdate(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            Object o = returningValue;
            if (o instanceof Category) {
                Category category = (Category) o;
                log.info("Category id={} was updated.", category.getId());
            }
            if (o instanceof Compilation) {
                Compilation compilation = (Compilation) o;
                log.info("Compilation id={} was updated.", compilation.getId());
            }
            if (o instanceof EventOutputDto) {
                EventOutputDto event = (EventOutputDto) o;
                log.info("Event id={} was updated or changed in method {}.", event.getId(), joinPoint.getSignature().getName());
            }
        }
    }
}