package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.*;

/**
 * @author Peter Zemianek
 */

@Repeatable(EmotionalGoal.class)
public @interface EmotionalParameter {

    public String target();

    public boolean booleanValue() default false;

    public double doubleValue() default 0;

    public String stringValue() default "";

    public String fieldValue() default "";

    public String methodValue() default "";

    public String parameter();



}
