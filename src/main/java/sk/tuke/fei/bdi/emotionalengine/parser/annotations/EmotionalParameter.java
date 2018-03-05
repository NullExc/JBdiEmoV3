package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Peter Zemianek
 *
 * This annotation is required for mapping Emotional Parameters to BDI goals and plans
 *
 * Simple format example : @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER, target = R.STRING, stringValue = "TelemarketerAnna")
 * Field format example : @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldName = "desirability")
 *
 * fieldName of methodName parameter is name of the field/method declared in agent's class
 */

@Target(value=ANNOTATION_TYPE)
@Retention(value=RUNTIME)
public @interface EmotionalParameter {
    /**
     * Specifies how is value of EmotionalParameter stored
     * @return Possible target specifications : R.METHOD, R.FIELD, R.DOUBLE, R.STRING, R.BOOLEAN
     */
    String target();

    /**
     * Has to be specified with R.BOOLEAN target
     * @return boolean value (true, false)
     */
    boolean booleanValue() default false;

    /**
     * Has to be specified with R.DOUBLE target
     * @return double value (0-1)
     */
    double doubleValue() default 0;

    /**
     * Has to be specified with R.STRING target
     * @return String value
     */
    String stringValue() default "";

    /**
     * Name of declared field in agent's class
     * @return value of declared field
     */
    String fieldName() default "";

    /**
     * Name of declared method in agent's class
     * @return value from invoked method
     */
    String methodName() default "";

    /**
     * @return Emotional Parameter identification
     */
    String parameter();

    boolean agentClass() default true;

}
