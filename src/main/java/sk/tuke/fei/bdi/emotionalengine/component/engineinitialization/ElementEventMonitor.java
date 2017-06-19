package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bdiv3.runtime.IBeliefListener;
import jadex.bdiv3.runtime.IGoal;
import jadex.bdiv3.runtime.impl.*;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IMonitoringComponentFeature;
import jadex.bridge.service.types.monitoring.IMonitoringEvent;
import jadex.bridge.service.types.monitoring.IMonitoringService;
import jadex.commons.IFilter;
import jadex.rules.eca.ChangeInfo;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.ParameterValueMapper;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class ElementEventMonitor {

    private Engine engine;
    private IInternalAccess access;
    private Map<String, IGoal> myActiveGoals = Collections.synchronizedMap(new ConcurrentHashMap<String, IGoal>());
    private Map<String, Object[]> myActivePlans = Collections.synchronizedMap(new ConcurrentHashMap<String, Object[]>());

    private final IInternalBDIAgentFeature internalFeature;
    private final IMonitoringComponentFeature monitor;
    private final IBDIAgentFeature bdiFeature;
    private final MessageCenter messageCenter;
    private final ParameterValueMapper params;

    public ElementEventMonitor(Object agent, Engine engine, MessageCenter center) {

        try {
            this.access = JBDIEmo.findAgentComponent(agent, IInternalAccess.class);
        } catch (JBDIEmoException e) {
            e.printStackTrace();
        }

        this.engine = engine;

        this.monitor = access.getComponentFeature(IMonitoringComponentFeature.class);

        internalFeature = access.getComponentFeature(IInternalBDIAgentFeature.class);

        bdiFeature = access.getComponentFeature(IBDIAgentFeature.class);

        params = new ParameterValueMapper(agent);

        messageCenter = center;
    }

    /**
     * Subscribing for Goals and Plans events.
     * BDI v3 doesn't support Goals and Plans listeners.
     * For that reason we have to subscribe for events to let JBdiEmo know that goal/plan was added/finished
     */
    public void goalsAndPlansMonitoring() {

        monitor.subscribeToEvents(new IFilter<IMonitoringEvent>() {
            public boolean filter(IMonitoringEvent event) {

                Map<String, Object> properties = event.getProperties();

                if (properties == null || properties.get("details") == null) {
                    return false;
                }

                if ((properties.get("details") instanceof AbstractBDIInfo)) {

                    AbstractBDIInfo bdiInfo = (AbstractBDIInfo) properties.get("details");

                    if (bdiInfo instanceof PlanInfo) {
                        handlePlanEvent(bdiInfo);
                    } else if (bdiInfo instanceof GoalInfo) {
                        handleGoalEvent(bdiInfo);
                    }
                }
                return true;
            }
        }, true, IMonitoringService.PublishEventLevel.FINE);
    }

    public void handlePlanEvent(AbstractBDIInfo info) {

        PlanInfo planInfo = (PlanInfo) info;

        String[] planNames = engine.getElementsNames(R.PLAN);

        if (planNames == null) return;

        if (Arrays.asList(planNames).contains(planInfo.getType())) {
            String state = planInfo.getState();

            if (state.equals(RPlan.PlanLifecycleState.NEW.name())) {

                handlePlanAddedEvent();

            } else if (state.equals(RPlan.PlanLifecycleState.PASSED.name()) ||
                    state.equals(RPlan.PlanLifecycleState.ABORTED.name()) ||
                    state.equals(RPlan.PlanLifecycleState.FAILED.name())) {

                handlePlanFinishedEvent();
            }
        }
    }

    public void handleGoalEvent(AbstractBDIInfo info) {

        GoalInfo goalInfo = (GoalInfo) info;

        String[] goalNames = engine.getElementsNames(R.GOAL);

        if (goalNames == null) return;

        if (Arrays.asList(goalNames).contains(goalInfo.getType())) {

            String lifeState = goalInfo.getLifecycleState();

            if (lifeState.equals(IGoal.GoalLifecycleState.NEW.name()) ||
                    lifeState.equals(IGoal.GoalLifecycleState.ADOPTED.name())) {

                handleGoalAddedEvent();

            } else if (lifeState.equals(IGoal.GoalLifecycleState.DROPPED.name())) {

                handleGoalFinishedEvent();
            }
        }
    }

    private void handlePlanAddedEvent() {

        for (RPlan plan : internalFeature.getCapability().getPlans()) {

            String description = plan.getModelElement().getDescription();

            // Check if plan is emotional (it's name is contained in emotional engine)
            if (Arrays.asList(engine.getElementsNames(R.PLAN)).contains(description)) {

                // Check if plan instance is new and fire plan added emotional event if is is
                if (!myActivePlans.containsKey(plan.toString())) {

                    // Check if plans reason is emotional goal
                    String reasonElementName = null;
                    // Get plan reason
                    RGoal reason = null;

                    if (plan.getReason() instanceof  RGoal) {
                        reason = (RGoal) plan.getReason();
                    }

                    if (reason != null) {

                        // Get emotional goal names (possible reasons)
                        for (String goalName : engine.getElementsNames(R.GOAL)) {
                            if (reason.getModelElement().getDescription().equals(goalName)) {
                                reasonElementName = goalName;
                            }
                        }
                    }

                    // Put new plan instance into active plan map to avoid multiple plan added events for one plan instance
                    myActivePlans.put(plan.toString(), new Object[]{plan, reasonElementName});

                    // Get objectValue
                    Element element = engine.getElement(description, R.PLAN);

                    // Create emotional event
                    EmotionalEvent emotionalEvent = new EmotionalEvent();

                    // Set emotional event values
                    emotionalEvent.setElementName(description);
                    emotionalEvent.setEventType(R.EVT_PLAN_CREATED);
                    emotionalEvent.setResultType(R.RESULT_NULL);
                    emotionalEvent.setUserParameters(params.getUserParameterValues(JBDIEmo.UserPlanParams
                            .get(engine.getAgentName()).get(description).value()));
                    emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));

                    // Fire emotional event
                    element.processEmotionalEvent(emotionalEvent);

                    // Send emotional message
                    messageCenter.sendEmotionalMessage(description, R.EVT_PLAN_CREATED, R.RESULT_NULL);
                }
            }
        }
    }

    private void handlePlanFinishedEvent() {

        // Iterate plan instances stored in my active plan map to find if we have record of plan which is no longer active
        for (String myActivePlanKey : myActivePlans.keySet()) {

            boolean isStillActive = false;

            // Iterate currently active access plans
            for (RPlan plan : internalFeature.getCapability().getPlans()) {
                // If plan instance stored in my active plan map is still currently active access plan break and continue
                // with testing next plan instance stored in my active plan map
                if (myActivePlanKey.equals(plan.toString()) ^ isPlanFinished(plan)) {
                    isStillActive = true;
                    break;
                }
            }

            // If plan instance stored in my active plan map is no longer currently active access plan fire plan finished emotional event
            if (!isStillActive) {

                Object[] planObject = myActivePlans.get(myActivePlanKey);
                RPlan plan = (RPlan) planObject[0];
                String reason = (String) planObject[1];

                // Remove no longer active plan instance from my active plan map to avoid multiple plan finished events for one plan instance
                myActivePlans.remove(myActivePlanKey);

                // Get objectValue
                String elementName = plan.getModelElement().getDescription();
                Element element = engine.getElement(elementName, R.PLAN);

                // Create emotional event
                EmotionalEvent emotionalEvent = new EmotionalEvent();

                // Get plan result
                int planResult;
                if (plan.getLifecycleState().equals(RPlan.PlanLifecycleState.PASSED)) {
                    planResult = R.RESULT_SUCCESS;
                } else {
                    planResult = R.RESULT_FAILURE;
                }

                // Add system parameters needed for plan, goal emotions (unique for plan finished event)
                Map<Integer, Double> systemParams = params.getSystemParameterValues(element);
                params.addSystemParameterValuesForGoalPlanEmotions(engine, systemParams, reason);

                // Set emotional event values
                emotionalEvent.setElementName(elementName);
                emotionalEvent.setEventType(R.EVT_PLAN_FINISHED);
                emotionalEvent.setResultType(planResult);
                emotionalEvent.setUserParameters(params.getUserParameterValues(JBDIEmo.UserPlanParams.get(engine.getAgentName())
                        .get(plan.getModelElement().getDescription()).value()));
                emotionalEvent.setSystemParameters(systemParams);

                // Fire emotional event
                element.processEmotionalEvent(emotionalEvent);

                // Send emotional message
                messageCenter.sendEmotionalMessage(elementName, R.EVT_PLAN_FINISHED, planResult);
            }
        }
    }

    private void handleGoalAddedEvent() {

        for (RGoal goal : internalFeature.getCapability().getGoals()) {

            String description = goal.getModelElement().getDescription();
            // Check if goal is emotional (it's name is contained in emotional engine)
            if (Arrays.asList(engine.getElementsNames(R.GOAL)).contains(description)) {

                // Check if goal instance is new and fire goal added emotional event if it is
                if (!myActiveGoals.containsKey(goal.toString())) {

                    System.err.println("Adding goal " + description);
                    // Put new goal instance into active goal map to avoid multiple goal added events for one goal instance
                    myActiveGoals.put(goal.toString(), goal);

                    // Get objectValue

                    Element objectValue = engine.getElement(description, R.GOAL);

                    // Create emotional event
                    EmotionalEvent emotionalEvent = new EmotionalEvent();

                    // Set emotional event values
                    emotionalEvent.setElementName(description);
                    emotionalEvent.setEventType(R.EVT_GOAL_CREATED);
                    emotionalEvent.setResultType(R.RESULT_NULL);
                    emotionalEvent.setUserParameters(params.getUserParameterValues(JBDIEmo.UserGoalParams
                            .get(engine.getAgentName()).get(description).value()));
                    emotionalEvent.setSystemParameters(params.getSystemParameterValues(objectValue));

                    // Fire emotional event
                    objectValue.processEmotionalEvent(emotionalEvent);
                }
            }
        }
    }

    private void handleGoalFinishedEvent() {

        // Get currently active access goals
        Collection<RGoal> agentActiveGoals = internalFeature.getCapability().getGoals();

        // Iterate goal instances stored in my active goal map to find if we have record of goal which is no longer active
        for (String myActiveGoalKey : myActiveGoals.keySet()) {

            boolean isStillActive = false;

            // Iterate currently active access goals
            for (IGoal goal : agentActiveGoals) {

                // If goal instance stored in my active goal map is still currently active access goal break and continue
                // with testing next goal instance stored in my active goal map
                if (myActiveGoalKey.equals(goal.toString())) {
                    isStillActive = true;
                    break;
                }
            }

            // If goal instance stored in my active goal map is no longer currently active access goal fire goal finished emotional event
            if (!isStillActive) {

                IGoal goal = myActiveGoals.get(myActiveGoalKey);

                // Remove no longer active goal instance from my active goal map to avoid multiple goal finished events for one goal instance
                myActiveGoals.remove(myActiveGoalKey);

                // Get objectValue
                String elementName = goal.getModelElement().getDescription();
                Element objectValue = engine.getElement(elementName, R.GOAL);

                // Create emotional event
                EmotionalEvent emotionalEvent = new EmotionalEvent();

                // Get goal result for emotional event processing
                int goalResult;
                if (goal.isSucceeded()) {
                    goalResult = R.RESULT_SUCCESS;
                } else {
                    goalResult = R.RESULT_FAILURE;
                }

                // Set emotional event values
                emotionalEvent.setElementName(elementName);
                emotionalEvent.setEventType(R.EVT_GOAL_FINISHED);
                emotionalEvent.setResultType(goalResult);
                emotionalEvent.setUserParameters(params.getUserParameterValues(JBDIEmo.UserGoalParams
                        .get(engine.getAgentName()).get(elementName).value()));
                emotionalEvent.setSystemParameters(params.getSystemParameterValues(objectValue));

                // Fire emotional event
                objectValue.processEmotionalEvent(emotionalEvent);
            }
        }
    }

    public void beliefMonitoring() {

        String[] beliefNames = engine.getElementsNames(R.BELIEF);

        if (beliefNames != null) {
            for (String beliefName : beliefNames) {

                System.out.println("Belief added for monitoring: " + beliefName);

                bdiFeature.addBeliefListener(beliefName, new IBeliefListener<EmotionalBelief>() {
                    @Override
                    public void beliefChanged(ChangeInfo<EmotionalBelief> changeInfo) {
                        handleBeliefChangedEvent(changeInfo.getValue());
                    }

                    @Override
                    public void factAdded(ChangeInfo<EmotionalBelief> changeInfo) {

                    }

                    @Override
                    public void factRemoved(ChangeInfo<EmotionalBelief> changeInfo) {

                    }

                    @Override
                    public void factChanged(ChangeInfo<EmotionalBelief> changeInfo) {

                    }
                });
            }
        }
    }

    private void handleBeliefChangedEvent(EmotionalBelief emotionalBelief) {

        // Get element
        String elementName = emotionalBelief.getName();
        Element element = engine.getElement(elementName, R.BELIEF);

        // Create emotional event
        EmotionalEvent emotionalEvent = new EmotionalEvent();

        // Set emotional event values
        emotionalEvent.setElementName(elementName);
        emotionalEvent.setEventType(R.EVT_BELIEF_CHANGED);
        emotionalEvent.setResultType(R.RESULT_NULL);
        emotionalEvent.setUserParameters(null);
        emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));
        emotionalEvent.setBeliefFamiliar(emotionalBelief.isFamiliar());
        emotionalEvent.setBeliefAttractive(emotionalBelief.isAttractive());
        emotionalEvent.setBeliefAttractionIntensity(emotionalBelief.getAttractionIntensity());

        // Fire emotional event
        element.processEmotionalEvent(emotionalEvent);

    }

    public void beliefSetMonitoring() {
        String[] beliefNames = engine.getElementsNames(R.BELIEF_SET);

        if (beliefNames != null) {
            for (String beliefName : beliefNames) {

                System.out.println("BeliefSet added for monitoring: " + beliefName);

                bdiFeature.addBeliefListener(beliefName, new IBeliefListener<Object>() {

                    @Override
                    public void beliefChanged(ChangeInfo<Object> changeInfo) {

                    }

                    @Override
                    public void factAdded(ChangeInfo<Object> changeInfo) {

                        if (changeInfo.getValue() instanceof EmotionalBelief) {

                            EmotionalBelief emotionalBelief = (EmotionalBelief) changeInfo.getValue();
                            String beliefSet = emotionalBelief.getParent();

                            if (beliefSet != null &&
                                    engine.getElement(emotionalBelief.getName(), R.BELIEF_SET_BELIEF) == null) {

                                engine.addElement(emotionalBelief.getName(), R.BELIEF_SET_BELIEF, beliefSet);
                                handleFactChangedEvent(emotionalBelief);
                            }

                        }
                    }

                    @Override
                    public void factRemoved(ChangeInfo<Object> changeInfo) {
                        if (changeInfo.getValue() instanceof EmotionalBelief) {
                            EmotionalBelief emotionalBelief = (EmotionalBelief) changeInfo.getValue();
                            engine.removeElement(emotionalBelief.getName(), R.BELIEF_SET_BELIEF);
                        }
                    }

                    @Override
                    public void factChanged(ChangeInfo<Object> changeInfo) {
                        if (changeInfo.getValue() instanceof EmotionalBelief) {
                            handleFactChangedEvent((EmotionalBelief) changeInfo.getValue());
                        }
                    }
                });
            }
        }
    }

    private void handleFactChangedEvent(EmotionalBelief emotionalBelief) {

        // Get element
        String elementName = emotionalBelief.getName();
        Element element = engine.getElement(elementName, R.BELIEF_SET_BELIEF);

        // Check if element is valid
        if (element != null) {

            // Create emotional event
            EmotionalEvent emotionalEvent = new EmotionalEvent();

            // Check if emotional belief is valid (was initialized properly)
            if (emotionalBelief.isFamiliar() != null && emotionalBelief.isAttractive() != null && emotionalBelief.getAttractionIntensity() != null) {

                // Set emotional event values
                emotionalEvent.setElementName(elementName);
                emotionalEvent.setEventType(R.EVT_BELIEF_CHANGED);
                emotionalEvent.setResultType(R.RESULT_NULL);
                emotionalEvent.setUserParameters(null);
                emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));
                emotionalEvent.setBeliefFamiliar(emotionalBelief.isFamiliar());
                emotionalEvent.setBeliefAttractive(emotionalBelief.isAttractive());
                emotionalEvent.setBeliefAttractionIntensity(emotionalBelief.getAttractionIntensity());

                // Fire emotional event
                element.processEmotionalEvent(emotionalEvent);
            }
        }
    }

    private boolean isPlanFinished(RPlan plan) {
        return plan.getLifecycleState().equals(RPlan.PlanLifecycleState.PASSED)
                || plan.getLifecycleState().equals(RPlan.PlanLifecycleState.ABORTED)
                || plan.getLifecycleState().equals(RPlan.PlanLifecycleState.FAILED);
    }


}
