package sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Peter on 23.2.2017.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmoPlan {
    public String planName() default  "emoPlan";
    public double approval() default  0;
    public double disapproval() default  0;
    public double desirability() default 0;
    public String emotionalOther() default "";
    public String emotionalOtherPlan() default "";
    public boolean emotionalOtherGroup() default false;
}
