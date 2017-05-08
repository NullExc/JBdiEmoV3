package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.model.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.RPlan;
import jadex.bdiv3x.runtime.IParameter;
import jadex.bridge.IInternalAccess;
import jadex.commons.FieldInfo;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.*;
import sk.tuke.fei.bdi.emotionalengine.parser.BeliefMapper;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class AgentModelMapper {

    private final Class agentClass;
    private final Object agentObject;
    private final Engine engine;
    private final IInternalAccess access;
    private final BDIModel model;
    private final MCapability capability;

    public AgentModelMapper(Object agent, Engine engine, IInternalAccess access) {
        this.agentObject = agent;
        this.agentClass = agent.getClass();
        this.engine = engine;
        this.access = access;

        model = (BDIModel) access.getExternalAccess().getModel().getRawModel();
        capability = model.getCapability();
    }

    public void mapPlans() {

        int planCount = 0;

        for(MPlan mPlan : capability.getPlans()) {

            MBody body = mPlan.getBody();

            try {
                if (body.getMethod() != null) {

                    for (Method method : agentClass.getDeclaredMethods()) {

                        if (method.isAnnotationPresent(EmotionalPlan.class)
                                && method.getName().equals(body.getMethod().getName())) {

                            EmotionalPlan emoPlan = method.getAnnotation(EmotionalPlan.class);
                            JBDIEmo.UserPlanParams.put(method.getName(), emoPlan);

                            engine.addElement(method.getName(), R.PLAN);
                            mPlan.setDescription(method.getName());

                            planCount++;
                        }
                    }
                } else if (body.getClazz() != null) {
                    Class planClass = Class.forName(body.getClazz().getTypeName());

                    if (planClass.isAnnotationPresent(EmotionalPlan.class)) {
                        String simpleName = body.getClazz().getType0().getSimpleName();

                        EmotionalPlan emoPlan = (EmotionalPlan) planClass.getAnnotation(EmotionalPlan.class);
                        JBDIEmo.UserPlanParams.put(simpleName, emoPlan);

                        engine.addElement(simpleName, R.PLAN);
                        mPlan.setDescription(simpleName);

                        planCount++;
                    }
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        int totalCount = capability.getPlans().size();

        System.out.println("Plans created : " + planCount + ", total count of plans : " + totalCount);

    }

    public void mapGoals() {

        int goalCount = 0;

        for (MGoal mGoal : capability.getGoals()) {
            try {
                Class goalClass = Class.forName(mGoal.getName());
                if (goalClass.isAnnotationPresent(EmotionalGoal.class)) {

                    engine.addElement(goalClass.getSimpleName(), R.GOAL);
                    mGoal.setDescription(goalClass.getSimpleName());

                    goalCount++;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        int totalCount = capability.getGoals().size();

        System.out.println("Goals created : " + goalCount + ", total count of goals : " + totalCount);
    }

    public void mapBeliefs() {

        int beliefCount = 0;

        BeliefMapper beliefMapper = new BeliefMapper(agentClass);

        for (Field field : beliefMapper.fieldBeliefs()) {
            if (field.isAnnotationPresent(EmotionalBelief.class)) {
                engine.addElement(field.getName(), R.BELIEF);
                beliefCount++;
            }
        }

        for (Method method : beliefMapper.methodBeliefs()) {
            if (method.isAnnotationPresent(EmotionalBelief.class)) {
                engine.addElement(method.getName(), R.BELIEF);
                beliefCount++;
            }
        }

        int totalCount = beliefMapper.fieldBeliefs().size() + beliefMapper.methodBeliefs().size();

        System.out.println("Beliefs created : " + beliefCount + ", total count of beliefs : " + totalCount);

    }

}
