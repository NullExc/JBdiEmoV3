package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.EmoBelief;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.EmoGoal;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.EmoPlan;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.BeliefMapper;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.GoalMapper;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.PlanMapper;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Peter on 14.3.2017.
 */
public class AgentModelMapper {

    private final Class agent;
    private final Engine engine;

    public AgentModelMapper(Class agent, Engine engine) {
        this.agent = agent;
        this.engine = engine;
    }

    public void mapPlans() {

        int planCount = 0;

        PlanMapper planMapper = new PlanMapper(agent);

        for (Class clazz : planMapper.internalPlans()) {
            if (clazz.isAnnotationPresent(EmoPlan.class)) {
                engine.addElement(clazz.getSimpleName(), R.PLAN);
                planCount++;
            }
        }

        for (Class clazz : planMapper.externalPlans()) {
            if (clazz.isAnnotationPresent(EmoPlan.class)) {
                engine.addElement(clazz.getSimpleName(), R.PLAN);
                planCount++;
            }
        }

        for (Method method : planMapper.methodPlans()) {
            if (method.isAnnotationPresent(EmoPlan.class)) {
                engine.addElement(method.getName(), R.PLAN);
                planCount++;
            }
        }

        int totalCount = planMapper.externalPlans().size() + planMapper.internalPlans().size() + planMapper.methodPlans().size();

        System.out.println("Plans created : " + planCount + ", total count of plans : " + totalCount);
    }

    public void mapGoals() {

        int goalCount = 0;

        GoalMapper goalMapper = new GoalMapper(agent);

        for (Class goal : goalMapper.internalGoals()) {
            if (goal.isAnnotationPresent(EmoGoal.class)) {
                engine.addElement(goal.getSimpleName(), R.GOAL);
                goalCount++;
            }
        }

        for (Class goal : goalMapper.externalGoals()) {
            if (goal.isAnnotationPresent(EmoGoal.class)) {
                engine.addElement(goal.getSimpleName(), R.GOAL);
                goalCount++;
            }
        }

        int totalCount = goalMapper.internalGoals().size() + goalMapper.externalGoals().size();

        System.out.println("Goals created : " + goalCount + ", total count of goals : " + totalCount);

    }

    public void mapBeliefs() {

        int beliefCount = 0;

        BeliefMapper beliefMapper = new BeliefMapper(agent);

        for (Field field : beliefMapper.fieldBeliefs()) {
            if (field.isAnnotationPresent(EmoBelief.class)) {
                engine.addElement(field.getName(), R.BELIEF);
                beliefCount++;
            }
        }

        for (Method method : beliefMapper.methodBeliefs()) {
            if (method.isAnnotationPresent(EmoBelief.class)) {
                engine.addElement(method.getName(), R.BELIEF);
                beliefCount++;
            }
        }

        int totalCount = beliefMapper.fieldBeliefs().size() + beliefMapper.methodBeliefs().size();

        System.out.println("Beliefs created : " + beliefCount + ", total count of beliefs : " + totalCount);

    }



}
