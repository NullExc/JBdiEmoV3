package sk.tuke.fei.bdi.emotionalengine.BDIParser;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Goals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 15.3.2017.
 */
public class GoalMapper {

    private final Class agent;

    public GoalMapper(Class agent) {
        this.agent = agent;
        externalGoals();
    }

    public List<Class> internalGoals() {
        List<Class> intGoals = new ArrayList<Class>();
        for (Class clazz : agent.getClasses()) {
            if (clazz.isAnnotationPresent(Goal.class)) {
                intGoals.add(clazz);
            }
        }
        return intGoals;
    }

    public List<Class> externalGoals() {
        List<Class> extGoals = new ArrayList<Class>();
        if (agent.isAnnotationPresent(Goals.class)) {
            Goals goals = (Goals) agent.getAnnotation(Goals.class);
            for (Goal goal : goals.value()) {
                System.out.println(goal.clazz().getSimpleName());
            }
        }
        return extGoals;
    }


}
