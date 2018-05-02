package sk.tuke.fei.bdi.emotionalengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Zemianek
 *
 * The agent which wants to have remote access to other agent's elements, needs to use
 * this anootation on ICommunicationService variable.
 *
 * Attribute name specifies a name of remote agent.
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmotionalAgent {

    String name();

}
