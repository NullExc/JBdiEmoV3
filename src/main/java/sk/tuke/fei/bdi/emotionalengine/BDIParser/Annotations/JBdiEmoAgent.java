package sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Peter on 22.2.2017.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JBdiEmoAgent {
    String[] others() default {};
    boolean guiEnabled() default false;
    boolean loggerEnabled() default false;
    int loggingDelayMillis() default 1000;
    int decayTimeMillis() default 750;
    int decayStepsToMin() default 20;

}
