package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Zemianek
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmotionalPlan {

    public EmotionalParameter[] value() default {};

   /* public double approval() default  0;
    public double disapproval() default  0;
    public double desirability() default 0;
    public String emotionalOther() default "";
    public String emotionalOtherPlan() default "";
    public boolean emotionalOtherGroup() default false;*/
}
