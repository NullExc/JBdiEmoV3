package sk.tuke.fei.bdi.emotionalengine.BDIParser;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 15.3.2017.
 */
public class PlanMapper {

    private final Class<?> agent;

    public PlanMapper(Class<?> agent) {
        this.agent = agent;
        externalPlans();
        internalPlans();
    }

    public List<Class> externalPlans() {
        List<Class> extPlans = new ArrayList<Class>();
        if (agent.isAnnotationPresent(Plans.class)) {
            Plans plans = agent.getAnnotation(Plans.class);
            for (Plan plan : plans.value()) {
                extPlans.add(plan.body().value());
            }
        }
        return extPlans;
    }

    public List<Class> internalPlans() {
        List<Class> intPlans = new ArrayList<Class>();
        for (Class clazz : agent.getClasses()) {
            if (clazz.isAnnotationPresent(Plan.class)) {
                intPlans.add(clazz);
            }
        }
        return intPlans;
    }

    public List<Method> methodPlans() {
        List<Method> methPlans = new ArrayList<Method>();
        for (Method method : agent.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Plan.class)) {
                methPlans.add(method);
            }
        }
        return methPlans;
    }
}
