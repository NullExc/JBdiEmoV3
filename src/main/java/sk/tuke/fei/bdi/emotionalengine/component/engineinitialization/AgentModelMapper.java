package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.model.*;
import jadex.bridge.IInternalAccess;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal.CampaignAgainstJunkFood;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

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
        this.engine = (Engine) ((BDIModel) access.getExternalAccess().getModel().getRawModel()).getCapability().getBelief("engine").getValue(access);
        this.access = access;

        BDIModel model = (BDIModel) access.getExternalAccess().getModel().getRawModel();
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
                //BDI v3 doesn't support BeliefSet, so we have to check if this belief is a Collection,
                // to save it as BeliefSet in JBdiEmo
            } else if (belief.getValue(access) instanceof Collection) {

                Collection<EmotionalBelief> collection = (Collection<EmotionalBelief>) belief.getValue(access);

                if (collection.getClass().getComponentType() != null) {
                    System.err.println(collection.getClass().getComponentType().isAssignableFrom(EmotionalBelief.class));
                }
                engine.addElement(belief.getName(), R.BELIEF_SET);

                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {

                    Object object = iterator.next();

                    if (object instanceof EmotionalBelief) {

                        EmotionalBelief elementBelief = (EmotionalBelief) object;

                        if (belief.getName() != null && !belief.getName().equals("")) {

                            // Add element corresponding to particular belief into emotional engine
                            engine.addElement(elementBelief.getName(), R.BELIEF_SET_BELIEF, belief.getName());

                            // Increment belief count
                            beliefCount++;
                        }
                    }
                }
            }
        }

        int totalCount = capability.getBeliefs().size();

        System.out.println("Beliefs created : " + beliefCount + ", total count of beliefs : " + totalCount);

    }

}
