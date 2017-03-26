package sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Peter on 23.2.2017.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmoBelief {
    public Class[] agents() default {};
    public String beliefName() default  "emoBelief";
    public boolean isFamiliar() default false;
    public boolean isAttractive() default false;
    public double attractionIntensity() default 0;
}
