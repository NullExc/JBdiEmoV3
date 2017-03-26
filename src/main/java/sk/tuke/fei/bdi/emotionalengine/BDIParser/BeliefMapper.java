package sk.tuke.fei.bdi.emotionalengine.BDIParser;

import jadex.bdiv3.annotation.Belief;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.EmoBelief;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Peter on 15.3.2017.
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
