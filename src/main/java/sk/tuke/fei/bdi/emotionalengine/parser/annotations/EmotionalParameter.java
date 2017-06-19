package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Peter Zemianek
 *
 * This annotation is required for mapping Emotional Parameters to BDI goals and plans
 *
 * Simple format example : @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER, target = R.SIMPLE_STRING, stringValue = "TelemarketerAnna")
 * Field format example : @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldValue = "desirability")
 *
 * fieldValue of methodValue parameter is name of the field/method declared in agent's class
 */

@Target(value=ANNOTATION_TYPE)
@Retention(value=RUNTIME)
public @interface EmotionalParameter {
    /**
     * Specifies how is value of EmotionalParameter stored
     * @return Possible target specifications : R.METHOD, R.FIELD, R.SIMPLE_DOUBLE, R.SIMPLE_STRING, R.SIMPLE_BOOLEAN
     */
    public String target();

    /**
     * Has to be specified with R.SIMPLE_BOOLEAN target
     * @return boolean value (true, false)
     */
    public boolean booleanValue() default false;

    /**
     * Has to be specified with R.SIMPLE_DOUBLE target
     * @return double value (0-1)
     */
    public double doubleValue() default 0;

    /**
     * Has to be specified with R.SIMPLE_STRING target
     * @return String value
     */
    public String stringValue() default "";

    /**
     * Name of declared field in agent's class
     * @return value of declared field
     */
    public String fieldValue() default "";

    /**
     * Name of declared method in agent's class
     * @return Returned value from invoked method
     */
    public String methodValue() default "";

    /**
     * @return Emotional Parameter identification
     */
    public String parameter();



}
