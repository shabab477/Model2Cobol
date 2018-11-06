package io.shabab477.github.model2cobol.annotations;

import io.shabab477.github.model2cobol.constants.CalculationStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shabab
 * @since 11/6/18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataEntity {

    int level();

    String dataName();

    int length() default 0;

    CalculationStrategy strategy() default CalculationStrategy.PROVIDED;
}
