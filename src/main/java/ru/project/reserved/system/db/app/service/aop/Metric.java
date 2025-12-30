package ru.project.reserved.system.db.app.service.aop;


import ru.project.reserved.system.db.app.service.dto.type.MetricType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metric {

    MetricType type();
    String description() default "";
}
