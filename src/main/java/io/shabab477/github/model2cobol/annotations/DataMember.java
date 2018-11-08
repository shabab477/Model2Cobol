package io.shabab477.github.model2cobol.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shabab
 * @since 11/6/18
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataMember {

    int level();

    String dataName();

    int length() default 0;

    String type() default "";

    Class parseStrategy() default Void.class;
}
