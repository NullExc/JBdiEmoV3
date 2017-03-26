package sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Peter on 23.2.2017.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmoGoal {
    public String goalName() default  "emoGoal";
    public double otherDesireGoalSucces() default 0;
    public double otherDesireGoalFailure() default 0;
    public double desirability() default 0;
    public double probability() default  0;
}
