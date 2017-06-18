package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.model.*;
import jadex.bridge.IInternalAccess;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.parser.BeliefMapper;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

        for (MPlan mPlan : capability.getPlans()) {

            MBody body = mPlan.getBody();

            try {
                if (body.getMethod() != null) {

                    for (Method method : agentClass.getDeclaredMethods()) {

                        if (method.isAnnotationPresent(EmotionalPlan.class)
                                && method.getName().equals(body.getMethod().getName())) {

                            EmotionalPlan emoPlan = method.getAnnotation(EmotionalPlan.class);
                            JBDIEmo.UserPlanParams.get(engine.getAgentName()).put(method.getName(), emoPlan);

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
                        JBDIEmo.UserPlanParams.get(engine.getAgentName()).put(simpleName, emoPlan);

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

                for (Constructor constructor : goalClass.getConstructors()) {

                    if (constructor.isAnnotationPresent(EmotionalGoal.class)) {

                        EmotionalGoal emotionalGoal = (EmotionalGoal) constructor.getAnnotation(EmotionalGoal.class);

                        String goalName = goalClass.getSimpleName();

                        JBDIEmo.UserGoalParams.get(engine.getAgentName()).put(goalName, emotionalGoal);

                        engine.addElement(goalName, R.GOAL);
                        mGoal.setDescription(goalName);

                        goalCount++;

                        break;
                    }
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

        for (MBelief belief : capability.getBeliefs()) {

            if (belief.getValue(access) instanceof EmotionalBelief) {

                EmotionalBelief emotionalBelief = (EmotionalBelief) belief.getValue(access);

                if (emotionalBelief != null && emotionalBelief.getName() != null
                        && !emotionalBelief.getName().equals("") && emotionalBelief.getName().equals(belief.getName())) {

                    engine.addElement(belief.getName(), R.BELIEF);

                    beliefCount++;
                } else {
                    if (!emotionalBelief.getName().equals(belief.getName())) {
                        System.out.println("Belief name doesn't match EmotionalBelief name: " + belief.getName() + ", " + emotionalBelief.getName());
                    }
                }
            } else if (belief.getValue(access) instanceof Collection<?>) {

                Collection<EmotionalBelief> collection = (Collection<EmotionalBelief>) belief.getValue(access);

                engine.addElement(belief.getName(), R.BELIEF_SET);

                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {

                    EmotionalBelief elementBelief = (EmotionalBelief) iterator.next();

                    if (belief.getName() != null && !belief.getName().equals("")) {

                        // Add element corresponding to particular belief into emotional engine
                        engine.addElement(elementBelief.getName(), R.BELIEF_SET_BELIEF, belief.getName());

                        // Increment belief count
                        beliefCount++;
                    }
                }
            }
        }

        int totalCount = capability.getBeliefs().size();

        System.out.println("Beliefs created : " + beliefCount + ", total count of beliefs : " + totalCount);

    }

}