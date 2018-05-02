package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bdiv3.model.*;
import jadex.bridge.IInternalAccess;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalAgent;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.service.RemoteAgent;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Maps all emotional elements of agent or BDI module (Capability).
 *
 * Uses reflection to parse java representation of BDI elemets to find out if they are emotional.
 *
 * Founded elements are stored to Engine belief.
 *
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class AgentModelMapper {

    private final Class agentClass;
    private final Object agent;
    private final Engine engine;
    private final IInternalAccess access;
    private final MCapability capability;

    public AgentModelMapper(Object agent, IInternalAccess access) {

        this.agentClass = agent.getClass();
        this.agent = agent;
        this.access = access;

        this.capability = access.getComponentFeature(IInternalBDIAgentFeature.class).getBDIModel().getCapability();
        this.engine = (Engine) access.getComponentFeature(IInternalBDIAgentFeature.class).getBDIModel().getCapability().getBelief("engine").getValue(access);

    }

    /**
     * Maps variables in agent's class which are remote access to other agents.
     */
    public void mapEmotionalAgents() {

        for (Field field: agentClass.getDeclaredFields()) {

            if (field.isAnnotationPresent(EmotionalAgent.class)
                    && field.getType().getSimpleName().equals("ICommunicationService")) {

                RemoteAgent remoteAgent = new RemoteAgent(field.getAnnotation(EmotionalAgent.class).name(), access);

                field.setAccessible(true);

                try {
                    field.set(agent, remoteAgent);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Maps emotional plans. Checks if plan is defined as class or method and tries to find @EmotionalPlan annotation.
     */
    public void mapPlans() {

        int planCount = 0;

        for (MPlan mPlan : capability.getPlans()) {

            MBody body = mPlan.getBody();

            try {
                if (body.getMethod() != null) {

                    boolean isEmotional;

                    if (mPlan.getCapabilityName() == null) {
                        isEmotional = parsePlanMethod(mPlan, agentClass);
                    } else {

                        Class capabilityClass = agentClass.getDeclaredField(mPlan.getCapabilityName()).getType();
                        isEmotional = parsePlanMethod(mPlan, capabilityClass);
                    }

                    if (isEmotional) planCount++;


                } else if (body.getClazz() != null) {

                    Class planClass = ClassLoader.getSystemClassLoader().loadClass(body.getClazz().getTypeName()); // Class.forName(body.getClazz().getTypeName());

                    if (planClass.isAnnotationPresent(EmotionalPlan.class)) {

                        EmotionalPlan emoPlan = (EmotionalPlan) planClass.getAnnotation(EmotionalPlan.class);

                        String simpleName = body.getClazz().getType0().getSimpleName();

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

    /**
     * Maps emotional goals. Checks if class of goal is annotated with @EmotionalGoal.
     */
    public void mapGoals() {

        int goalCount = 0;

        for (MGoal mGoal : capability.getGoals()) {

            try {

                Class goalClass = Class.forName(mGoal.getElementName());

                if (goalClass.isAnnotationPresent(EmotionalGoal.class)) {

                    EmotionalGoal emotionalGoal = (EmotionalGoal) goalClass.getAnnotation(EmotionalGoal.class);

                    String goalName = goalClass.getSimpleName();

                    if (mGoal.getCapabilityName() != null) {
                        goalName = mGoal.getCapabilityName() + "." + goalName;
                    }

                    JBDIEmo.UserGoalParams.get(engine.getAgentName()).put(goalName, emotionalGoal);

                    engine.addElement(goalName, R.GOAL);

                    mGoal.setDescription(goalName);

                    goalCount++;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        int totalCount = capability.getGoals().size();

        System.out.println("Goals created : " + goalCount + ", total count of goals : " + totalCount);
    }

    /**
     * Maps emotional beliefs. Checks type of a belief value. If the type is EmotionalBelief, belief is considered as
     * emotional. If the type is Map or Collection and has EmotionalBelief generic type, it's considered as emotional
     * beliefset.
     */
    public void mapBeliefs() {

        int beliefCount = 0;

        for (MBelief belief : capability.getBeliefs()) {

            String beliefName = belief.getName();

            if (belief.getValue(access) instanceof EmotionalBelief) {

                EmotionalBelief emotionalBelief = (EmotionalBelief) belief.getValue(access);

                String capaBeliefName = belief.getCapabilityName() + "/" + emotionalBelief.getName();

                //System.err.println(beliefName + " " + emotionalBelief.getName() + " " + belief.getCapabilityName());

                if (emotionalBelief != null && emotionalBelief.getName() != null
                        && !emotionalBelief.getName().equals("") && (emotionalBelief.getName().equals(beliefName)
                        || capaBeliefName.equals(beliefName))) {

                    //if (belief.getCapabilityName() != null) engine.addElement(capaBeliefName, R.BELIEF);
                    //else engine.addElement(beliefName, R.BELIEF);
                    engine.addElement(beliefName, R.BELIEF);

                    if (belief.getCapabilityName() != null) emotionalBelief.setName(capaBeliefName);

                    beliefCount++;
                } else {
                    if (!emotionalBelief.getName().equals(beliefName)) {
                        System.err.println("Belief name doesn't match EmotionalBelief name: " + belief.getName() + ", " + emotionalBelief.getName());
                    }
                }
                //BDI v3 doesn't support BeliefSet, so we have to check if this belief is a Collection,
                // to save it as BeliefSet in JBdiEmo
            } else if (belief.getValue(access) instanceof Collection) {

                ParameterizedType parameterizedType = (ParameterizedType) belief.getField().getField(agent.getClass().getClassLoader()).getGenericType();

                Class<?> listClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                if ("sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief".equals(listClass.getName())) {

                    engine.addElement(beliefName, R.BELIEF_SET);

                    Collection<EmotionalBelief> collection = (Collection<EmotionalBelief>) belief.getValue(access);

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

                }

            } else if (belief.getValue(access) instanceof Map) {
                Map map = (Map) belief.getValue(access);

                Type type = belief.getField().getField(agent.getClass().getClassLoader()).getGenericType();

                if (!(type instanceof ParameterizedType)) continue;

                ParameterizedType parameterizedType = (ParameterizedType) type;

                Class<?> listClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];

                if (map != null && "sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief".equals(listClass.getName())) {
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
