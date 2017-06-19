package sk.tuke.fei.bdi.emotionalengine.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Zemianek
 *
 * This annotation is required for agent to define him as Emotional Agent for JBdiEmo
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JBDIEmoAgent {

    /**
     * Names of other agents for future communication.
     * For defining more other agents, R.MESSAGE_DELIMITER has to be used
     * Example : others = Agent1 + R.MESSAGE_DELIMITER + Agent2
     *
     * @return names of other agents
     */
    String others() default "";

    /**
     * @return option for enabling GUI of agent
     */
    boolean guiEnabled() default false;

    /**
     * @return option for enabling logging of agent
     */
    boolean loggerEnabled() default false;

    /**
     * @return specifies interval for saving Emotion values into logger file
     */
    int loggingDelayMillis() default 1000;

    /**
     * @return specifies interval for decaying Emotion values
     */
    int decayTimeMillis() default 750;

    /**
     * @return specifies parameter of inverse sigmoid function
     */
    int decayStepsToMin() default 20;

}
