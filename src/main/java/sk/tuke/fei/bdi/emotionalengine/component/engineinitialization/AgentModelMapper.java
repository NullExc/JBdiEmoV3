package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.features.impl.BDIMonitoringComponentFeature;
import jadex.bdiv3.features.impl.BDIProvidedServicesComponentFeature;
import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bdiv3.model.*;
import jadex.bdiv3.runtime.wrappers.SetWrapper;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.impl.IInternalMessageFeature;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class AgentModelMapper {

    private final Class agentClass;
    private final Engine engine;
    private final IInternalAccess access;
    private final MCapability capability;

    public AgentModelMapper(Object agent, IInternalAccess access) {

        this.agentClass = agent.getClass();
        this.access = access;

        this.capability = access.getComponentFeature(IInternalBDIAgentFeature.class).getBDIModel().getCapability();
        this.engine = (Engine) access.getComponentFeature(IInternalBDIAgentFeature.class).getBDIModel().getCapability().getBelief("engine").getValue(access);

    }

    public void mapPlans() {

        int planCount = 0;

        for (MPlan mPlan : capability.getPlans()) {

            MBody body = mPlan.getBody();

            try {
                if (body.getMethod() != null) {

                    /*for (Method method : agentClass.getDeclaredMethods()) {

                        if (method.isAnnotationPresent(EmotionalPlan.class)
                                && method.getName().equals(body.getMethod().getName())) {

                            EmotionalPlan emoPlan = method.getAnnotation(EmotionalPlan.class);
                            JBDIEmo.UserPlanParams.get(engine.getAgentName()).put(method.getName(), emoPlan);

                            engine.addElement(method.getName(), R.PLAN);
                            mPlan.setDescription(method.getName());

                            planCount++;
                        }
                    }*/

                    boolean isEmotional;

                    if (mPlan.getCapabilityName() == null) {
                        isEmotional = parsePlanMethod(mPlan, agentClass);
                    } else {

                        Class capabilityClass = agentClass.getDeclaredField(mPlan.getCapabilityName()).getType();
                        isEmotional = parsePlanMethod(mPlan, capabilityClass);
                    }

                    if (isEmotional) planCount++;


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
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        int totalCount = capability.getPlans().size();

        System.out.println("Plans created : " + planCount + ", total count of plans : " + totalCount);
    }

    public void mapGoals() {

        int goalCount = 0;

        for (MGoal mGoal : capability.getGoals()) {
            try {

                //System.err.println(mGoal.getName());

                Class goalClass = Class.forName(mGoal.getElementName());

                for (Constructor constructor : goalClass.getConstructors()) {

                    if (constructor.isAnnotationPresent(EmotionalGoal.class)) {

                        EmotionalGoal emotionalGoal = (EmotionalGoal) constructor.getAnnotation(EmotionalGoal.class);

                        String goalName = goalClass.getSimpleName();

                        if (mGoal.getCapabilityName() != null) {
                            goalName = mGoal.getCapabilityName() + "." + goalName;
                        }

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

            String beliefName = belief.getName();

            if (belief.getValue(access) instanceof EmotionalBelief) {

                EmotionalBelief emotionalBelief = (EmotionalBelief) belief.getValue(access);

                String capaBeliefName = belief.getCapabilityName() + "/" + emotionalBelief.getName();

                //System.err.println(beliefName + " " + emotionalBelief.getName() + " " + belief.getCapabilityName());

                if (emotionalBelief != null && emotionalBelief.getName() != null
                        && !emotionalBelief.getName().equals("") && ( emotionalBelief.getName().equals(beliefName)
                        || capaBeliefName.equals(beliefName) ) ) {

                    //if (belief.getCapabilityName() != null) engine.addElement(capaBeliefName, R.BELIEF);
                    //else engine.addElement(beliefName, R.BELIEF);
                    engine.addElement(beliefName, R.BELIEF);

                    if (belief.getCapabilityName() != null) emotionalBelief.setName(capaBeliefName);

                    beliefCount++;
                } else {
                    if (!emotionalBelief.getName().equals(beliefName)) {
                        System.out.println("Belief name doesn't match EmotionalBelief name: " + belief.getName() + ", " + emotionalBelief.getName());
                    }
                }
                //BDI v3 doesn't support BeliefSet, so we have to check if this belief is a Collection,
                // to save it as BeliefSet in JBdiEmo
            } else if (belief.getValue(access) instanceof Collection) {

                Collection<EmotionalBelief> collection = (Collection<EmotionalBelief>) belief.getValue(access);

                System.out.println("wrapper " + belief.isFieldBelief() + " " + belief.getField());

                engine.addElement(beliefName, R.BELIEF_SET);

                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {

                    Object object = iterator.next();

                    if (object instanceof EmotionalBelief) {

                        EmotionalBelief elementBelief = (EmotionalBelief) object;

                        if (beliefName != null && !beliefName.equals("")) {

                            // Add element corresponding to particular belief into emotional engine
                            engine.addElement(elementBelief.getName(), R.BELIEF_SET_BELIEF, beliefName);

                            // Increment belief count
                            beliefCount++;
                        }
                    }
                }
            } else if (belief.getValue(access) instanceof Map) {
                Map map = (Map) belief.getValue(access);

                if (map != null) {
                    Iterator iterator = map.keySet().iterator();

                    engine.addElement(beliefName, R.BELIEF_SET);

                    while (iterator.hasNext()) {
                        Map.Entry pair = (Map.Entry) iterator.next();

                        if (pair.getValue() instanceof EmotionalBelief) {

                            EmotionalBelief elementBelief = (EmotionalBelief) pair.getValue();

                            if (beliefName != null && !beliefName.equals("")) {

                                // Add element corresponding to particular belief into emotional engine
                                engine.addElement(elementBelief.getName(), R.BELIEF_SET_BELIEF, beliefName);

                                // Increment belief count
                                beliefCount++;
                            }
                        }
                    }
                }
            }
        }

        int totalCount = capability.getBeliefs().size();

        System.out.println("Beliefs created : " + beliefCount + ", total count of beliefs : " + totalCount);

    }

    private boolean parsePlanMethod(MPlan mPlan, Class clazz) {

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(EmotionalPlan.class)
                    && method.getName().equals(mPlan.getBody().getMethod().getName())) {

                EmotionalPlan emoPlan = method.getAnnotation(EmotionalPlan.class);

                String planName = method.getName();

                if (mPlan.getCapabilityName() != null) {
                    planName = mPlan.getCapabilityName() + "." + planName;
                }

                JBDIEmo.UserPlanParams.get(engine.getAgentName()).put(planName, emoPlan);

                engine.addElement(planName, R.PLAN);
                mPlan.setDescription(planName);

                return true;

            }
        }
        return false;
    }

}
