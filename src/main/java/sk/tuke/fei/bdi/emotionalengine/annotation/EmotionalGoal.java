package sk.tuke.fei.bdi.emotionalengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Zemianek
 *
 * Class annotated by EmotionalGoal is recognized as Emotional Goal for JBdiEmo
 *
 * Warning : Because of Jadex bug, annotation has to be used by empty constructor, not class definition
 */
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmotionalGoal {

    EmotionalParameter[] value() default {};
}
