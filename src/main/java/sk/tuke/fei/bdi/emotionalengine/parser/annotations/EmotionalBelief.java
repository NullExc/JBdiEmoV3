package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Zemianek
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmotionalBelief {
    public Class[] agents() default {};
    public String beliefName() default  "emoBelief";
    public boolean isFamiliar() default false;
    public boolean isAttractive() default false;
    public double attractionIntensity() default 0;
}
