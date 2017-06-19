package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class or method annotated by EmotionalPlan is recognized as Emotional Plan for JBdiEmo
 *
 * @author Peter Zemianek
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmotionalPlan {

    public EmotionalParameter[] value() default {};

}
