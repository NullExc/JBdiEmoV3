package sk.tuke.fei.bdi.emotionalengine.BDIParser;

import jadex.micro.annotation.Agent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peter on 14.3.2017.
 */
public class AgentMapper {

    private Set<Class<?>> allClasses;

    public AgentMapper() {
        allClasses = new ClassReader().getInstance().getClasses("");
    }

    public Set<Class<?>> agentClasses() {

        Set<Class<?>> agentClasses = new HashSet<Class<?>>();

        for (Class clazz : allClasses) {
            if (clazz.isAnnotationPresent(Agent.class)) {
                agentClasses.add(clazz);
            }
        }
        return agentClasses;
    }
}
