package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Zemianek
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmotionalParameter {

    public String target();

    public boolean booleanValue() default false;

    public double doubleValue() default 0;

    public String stringValue() default "";

    public String fieldValue() default "";

    public String methodValue() default "";

    public String parameter();



}
