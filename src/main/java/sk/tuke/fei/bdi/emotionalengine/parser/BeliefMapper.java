package sk.tuke.fei.bdi.emotionalengine.parser;

import jadex.bdiv3.annotation.Belief;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Zemianek
 */
public class BeliefMapper {

    private Class<?> agent;

    public BeliefMapper(Class agent) {
        this.agent = agent;
    }

    public List<Field> fieldBeliefs() {
        List<Field> beliefs = new ArrayList<Field>();

        for (Field field : agent.getDeclaredFields()) {
            if (field.isAnnotationPresent(Belief.class)) {
                beliefs.add(field);
            }
        }
        return beliefs;
    }

    public List<Method> methodBeliefs() {
        List<Method> beliefs = new ArrayList<Method>();

        for (Method method : agent.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Belief.class)) {
                beliefs.add(method);
            }
        }
        return beliefs;
    }
}
