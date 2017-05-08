package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bdiv3.runtime.IGoal;
import jadex.bdiv3.runtime.impl.*;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IMonitoringComponentFeature;
import jadex.bridge.service.types.monitoring.IMonitoringEvent;
import jadex.bridge.service.types.monitoring.IMonitoringService;
import jadex.commons.IFilter;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.ParameterValueMapper;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class ElementEventMonitor {


    private Engine engine;
    private IMonitoringComponentFeature monitor;
    private IInternalAccess access;
    private MessageCenter messageCenter;
    private Map<String, IGoal> myActiveGoals = Collections.synchronizedMap(new ConcurrentHashMap<String, IGoal>());
    private Map<String, Object[]> myActivePlans = Collections.synchronizedMap(new ConcurrentHashMap<String, Object[]>());
    private ParameterValueMapper params;

    private final IInternalBDIAgentFeature bdiFeature;

    public ElementEventMonitor(Object agent, Engine engine) {

        try {
            this.access = JBDIEmo.findAgentComponent(agent, IInternalAccess.class);
        } catch (JBDIEmoException e) {
            e.printStackTrace();
        }

        this.monitor = access.getComponentFeature(IMonitoringComponentFeature.class);

        this.engine = engine;

        bdiFeature = access.getComponentFeature(IInternalBDIAgentFeature.class);

        params = new ParameterValueMapper(agent);

        messageCenter = new MessageCenter(agent);

    }

    public void subscribeForMonitoring() {

        monitor.subscribeToEvents(new IFilter<IMonitoringEvent>() {
            public boolean filter(IMonitoringEvent event) {

                Map<String, Object> properties = event.getProperties();

                if (properties == null || properties.get("details") == null) {
                    return false;
                }

                if ((properties.get("details") instanceof AbstractBDIInfo)) {

                    AbstractBDIInfo bdiInfo = (AbstractBDIInfo) properties.get("details");

                    IInternalBDIAgentFeature bdiFeature = access.getComponentFeature(IInternalBDIAgentFeature.class);

                    System.out.println(event + " " + Arrays.asList(bdiFeature.getCapability().getPlans()));

                    if (bdiInfo instanceof PlanInfo) {
                        handlePlanEvent(bdiInfo);
                    } else if (bdiInfo instanceof GoalInfo) {
                        handleGoalEvent(bdiInfo);
                    } else if (bdiInfo instanceof BeliefInfo) {
                        handleBeliefEvent(bdiInfo);
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
            if (state.equals("NEW")) {
                handlePlanAddedEvent();
            } else if (state.equals("PASSED") || state.equals("ABORTED") || state.equals("FAILED")) {
                handlePlanFinishedEvent();
            }
        }
    }

    public void handleGoalEvent(AbstractBDIInfo info) {

    }

    public void handleBeliefEvent(AbstractBDIInfo info) {

    }

    private void handlePlanAddedEvent() {

        for (RPlan plan : bdiFeature.getCapability().getPlans()) {

            String description = plan.getModelElement().getDescription();

            // Check if plan is emotional (it's name is contained in emotional engine)
            if (Arrays.asList(engine.getElementsNames(R.PLAN)).contains(description)) {

                // Check if plan instance is new and fire plan added emotional event if is is
                if (!myActivePlans.containsKey(plan.toString())) {

                    System.err.println("<<<< Plan added to active plans! >>>> " + plan.getType());

                    // Check if plans reason is emotional goal
                    String reasonElementName = null;
                    // Get plan reason



                    RGoal reason = null;

                    if (plan.getReason() instanceof  RGoal) {
                        reason = (RGoal) plan.getReason();
                    }

                    if (reason != null) {

                        System.err.println("REASON FOUNDED " + reason);
                        // Get emotional goal names (possible reasons)
                        for (String goalName : engine.getElementsNames(R.GOAL)) {

                            if (reason.getModelElement().getDescription().equals(goalName)) {
                                reasonElementName = goalName;
                            }
                        }
                    }

                    //System.err.println("SAVED KEY : " + plan.toString());
                    // Put new plan instance into active plan map to avoid multiple plan added events for one plan instance
                    myActivePlans.put(plan.toString(), new Object[]{plan, reasonElementName});

                    // Get objectValue
                    String elementName = plan.getModelElement().getName();
                    Element element = engine.getElement(elementName, R.PLAN);

                    // Create emotional event
                    EmotionalEvent emotionalEvent = new EmotionalEvent();

                    // Set emotional event values
                    emotionalEvent.setElementName(elementName);
                    emotionalEvent.setEventType(R.EVT_PLAN_CREATED);
                    emotionalEvent.setResultType(R.RESULT_NULL);
                    emotionalEvent.setUserParameters(params.getUserParameterValues(JBDIEmo.UserPlanParams.get(description).value()));
                    emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));

                    // Fire emotional event
                    element.processEmotionalEvent(emotionalEvent);

                    // Send emotional message
                    messageCenter.sendEmotionalMessage(elementName, R.EVT_PLAN_CREATED, R.RESULT_NULL);

                }
            }
        }
    }

    private void handlePlanFinishedEvent() {

        System.err.println("<<<< handled  finish plan event >>>> ");

        // Iterate plan instances stored in my active plan map to find if we have record of plan which is no longer active
        for (String myActivePlanKey : myActivePlans.keySet()) {

            boolean isStillActive = false;

            // Iterate currently active access plans
            for (RPlan plan : bdiFeature.getCapability().getPlans()) {
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
                String elementName = plan.getModelElement().getName();
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
                emotionalEvent.setUserParameters(params.getUserParameterValues(JBDIEmo.UserPlanParams
                        .get(plan.getModelElement().getDescription()).value()));
                emotionalEvent.setSystemParameters(systemParams);

                // Fire emotional event
                element.processEmotionalEvent(emotionalEvent);

                // Send emotional message
                messageCenter.sendEmotionalMessage(elementName, R.EVT_PLAN_FINISHED, planResult);

            }

        }

    }

    public void addGoalsForMonitoring() {

        // Get goal objectValue names
      /*  String[] goalNames = engine.getElementsNames(R.GOAL);

        // Iterate goal objectValue names
        if (goalNames != null) {
            for (String goalName : goalNames) {

                System.out.println("Goal added for monitoring: " + goalName);

                // Add goal listener for goal added and goal finished events
                parentPlan.getGoalbase().addGoalListener(goalName, new IGoalListener() {

                    public void goalAdded(AgentEvent ae) {
                        handleGoalAddedEvent();
                    }


                    public void goalFinished(AgentEvent ae) {
                        handleGoalFinishedEvent();
                    }
                });

            }
        }*/
    }

    private void handleGoalAddedEvent() {

        // Get currently active access goals
       /* IGoal[] agentActiveGoals = parentPlan.getGoalbase().getGoals();

        for (IGoal goal : agentActiveGoals) {

            // Check if goal is emotional (it's name is contained in emotional engine)
            if (Arrays.asList(engine.getElementsNames(R.GOAL)).contains(goal.getModelElement().getName())) {

                // Check if goal instance is new and fire goal added emotional event if it is
                if (!myActiveGoals.containsKey(goal.toString())) {

                    // Put new goal instance into active goal map to avoid multiple goal added events for one goal instance
                    myActiveGoals.put(goal.toString(), goal);

                    // Get objectValue
                    String elementName = goal.getModelElement().getName();
                    Element objectValue = engine.getElement(elementName, R.GOAL);

                    // Create emotional event
                    EmotionalEvent emotionalEvent = new EmotionalEvent();

                    // Set emotional event values
                    emotionalEvent.setElementName(elementName);
                    emotionalEvent.setEventType(R.EVT_GOAL_CREATED);
                    emotionalEvent.setResultType(R.RESULT_NULL);
                    emotionalEvent.setUserParameters(params.getUserParameterValues(goal));
                    emotionalEvent.setSystemParameters(params.getSystemParameterValues(objectValue));

                    // Fire emotional event
                    objectValue.processEmotionalEvent(emotionalEvent);

                }
            }
        }*/
    }

    private void handleGoalFinishedEvent() {

        // Get currently active access goals
      /*  IGoal[] agentActiveGoals = parentPlan.getGoalbase().getGoals();

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
                String elementName = goal.getModelElement().getName();
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
                emotionalEvent.setUserParameters(params.getUserParameterValues(goal));
                emotionalEvent.setSystemParameters(params.getSystemParameterValues(objectValue));

                // Fire emotional event
                objectValue.processEmotionalEvent(emotionalEvent);

            }

        }*/

    }

    public void addBeliefsForMonitoring() {



        // Get belief objectValue names
       /* String[] beliefNames = engine.getElementsNames(R.BELIEF);

        // Iterate plan objectValue names
        if (beliefNames != null) {
            for (String beliefName : beliefNames) {

                System.out.println("Belief added for monitoring: " + beliefName);

                // Add belief listener for belief changed events
                parentPlan.getBeliefbase().getBelief(beliefName).addBeliefListener(new IBeliefListener() {

                    public void beliefChanged(AgentEvent ae) {
                        handleBeliefChangedEvent((IBelief) ae.getSource());
                    }
                });
            }
        }*/
    }

    private void handleBeliefChangedEvent(EmotionalBelief belief) {

        // Get objectValue
        String elementName = belief.beliefName();
        Element element = engine.getElement(elementName, R.BELIEF);

        // Create emotional event
        EmotionalEvent emotionalEvent = new EmotionalEvent();

        // Set emotional event values
        emotionalEvent.setElementName(elementName);
        emotionalEvent.setEventType(R.EVT_BELIEF_CHANGED);
        emotionalEvent.setResultType(R.RESULT_NULL);
        emotionalEvent.setUserParameters(null);
        emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));
        emotionalEvent.setBeliefFamiliar(belief.isFamiliar());
        emotionalEvent.setBeliefAttractive(belief.isAttractive());
        emotionalEvent.setBeliefAttractionIntensity(belief.attractionIntensity());

        // Fire emotional event
        element.processEmotionalEvent(emotionalEvent);

    }

    public void addBeliefSetsForMonitoring() {

        /*// Get belief objectValue names
        String[] beliefSetNames = engine.getElementsNames(R.BELIEF_SET);

        // Iterate plan objectValue names
        if (beliefSetNames != null) {
            for (String beliefSetName : beliefSetNames) {

                System.out.println("Belief set added for monitoring: " + beliefSetName);

                // Add belief listener for belief changed events
                parentPlan.getBeliefbase().getBeliefSet(beliefSetName).addBeliefSetListener(new IBeliefSetListener() {

                    public void factChanged(AgentEvent ae) {

                        handleFactChangedEvent((EmotionalBelief) ae.getValue());

                    }

                    public void factAdded(AgentEvent ae) {

                        // Get information from event
                        BeliefSetFlyweight beliefSet = (BeliefSetFlyweight) ae.getSource();
                        String beliefSetName = beliefSet.getModelElement().getName();
                        EmotionalBelief emotionalBelief = (EmotionalBelief) ae.getValue();

                        // If belief and belief set name is valid
                        if (emotionalBelief != null && beliefSetName != null) {

                            // Add belief to engine
                            engine.addElement(emotionalBelief.getName(), R.BELIEF_SET_BELIEF, beliefSetName);

                            // Act like fact changed event because of adding new fact
                            handleFactChangedEvent(emotionalBelief);
                        }

                    }

                    public void factRemoved(AgentEvent ae) {

                        // Remove objectValue from the engine
                        EmotionalBelief emotionalBelief = (EmotionalBelief) ae.getValue();
                        engine.removeElement(emotionalBelief.getName(), R.BELIEF_SET_BELIEF);

                    }
                });
            }
        }*/
    }

    private void handleFactChangedEvent(EmotionalBelief emotionalBelief) {

        // Get objectValue
        String elementName = emotionalBelief.beliefName();
        Element element = engine.getElement(elementName, R.BELIEF_SET_BELIEF);

        // Check if objectValue is valid
        if (element != null) {

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
            emotionalEvent.setBeliefAttractionIntensity(emotionalBelief.attractionIntensity());

            // Fire emotional event
            element.processEmotionalEvent(emotionalEvent);
        }

    }

    public MessageCenter getMessageCenter() {
        return messageCenter;
    }

    private boolean isPlanFinished(RPlan plan) {
        return plan.getLifecycleState().equals(RPlan.PlanLifecycleState.PASSED)
                || plan.getLifecycleState().equals(RPlan.PlanLifecycleState.ABORTED)
                || plan.getLifecycleState().equals(RPlan.PlanLifecycleState.FAILED);
    }


}
