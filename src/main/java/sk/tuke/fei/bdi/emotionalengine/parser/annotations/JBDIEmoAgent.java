package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Zemianek
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JBDIEmoAgent {
    String others() default "";
    boolean guiEnabled() default false;
    boolean loggerEnabled() default false;
    int loggingDelayMillis() default 1000;
    int decayTimeMillis() default 750;
    int decayStepsToMin() default 20;

}
