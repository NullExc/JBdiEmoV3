package sk.tuke.fei.bdi.emotionalengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class or method annotated by EmotionalPlan is recognized as Emotional Plan for JBdiEmo
 *
 * @author Peter Zemianek
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmotionalPlan {

    EmotionalParameter[] value() default {};

}
