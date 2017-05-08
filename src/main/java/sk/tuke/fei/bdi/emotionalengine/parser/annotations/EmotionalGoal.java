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
public @interface EmotionalGoal {
    public String goalName() default  "emoGoal";
    public double otherDesireGoalSucces() default 0;
    public double otherDesireGoalFailure() default 0;
    public double desirability() default 0;
    public double probability() default  0;
}
