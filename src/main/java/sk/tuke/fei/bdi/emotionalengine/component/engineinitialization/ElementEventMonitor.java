package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.runtime.IGoal;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3x.runtime.IElement;
import jadex.commons.future.IResultListener;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.EmoBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.ParameterValueMapper;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Peter on 14.3.2017.
 */
public class ElementEventMonitor {

    private IPlan parentPlan;
    private Engine engine;
    private MessageCenter messageCenter;
    private Map<String, IGoal> myActiveGoals = Collections.synchronizedMap(new ConcurrentHashMap<String, IGoal>());
    private Map<String, Object[]> myActivePlans = Collections.synchronizedMap(new ConcurrentHashMap<String, Object[]>());
    private ParameterValueMapper params;

    public ElementEventMonitor(Engine engine, IPlan plan) {
        this.parentPlan = plan;

        messageCenter = new MessageCenter();
        this.engine = engine;
        //engine = (Engine) parentPlan.getBeliefbase().getBelief(R.ENGINE).getFact();
        params = new ParameterValueMapper();
    }

    public void addPlansForMonitoring() {

        // Get plan element names
      /*  String[] planNames = engine.getElementsNames(R.PLAN);

        // Iterate plan element names
        if (planNames != null) {
            for (String planName : planNames) {

                System.out.println("Plan added for monitoring: " + planName);

                // Add plan listener for plan added and plan finished events


                parentPlan.getPlanbase().addPlanListener(planName, new IPlanListener() {

                    public void planAdded(AgentEvent ae) {
                        handlePlanAddedEvent();
                    }


                    public void planFinished(AgentEvent ae) {
                        handlePlanFinishedEvent();
                    }
                });

            }
        }*/
    }

    private void handlePlanAddedEvent() {

        // Get currently active agent plans
       /* IPlan[] plans = parentPlan.getPlanbase().getPlans();

        for (IPlan plan : plans) {

            // Check if plan is emotional (it's name is contained in emotional engine)
            if (Arrays.asList(engine.getElementsNames(R.PLAN)).contains(plan.getModelElement().getName())) {

                // Check if plan instance is new and fire plan added emotional event if is is
                if (!myActivePlans.containsKey(plan.toString())) {


                    // Check if plans reason is emotional goal
                    String reasonElementName = null;
                    // Get plan reason
                    IElement reason = plan.getReason();
                    if (reason != null) {
                        // Get emotional goal names (possible reasons)
                        for (String goalName : engine.getElementsNames(R.GOAL)) {

                            if (reason.getModelElement().getName().equals(goalName)) {
                                reasonElementName = goalName;
                            }
                        }
                    }

                    // Put new plan instance into active plan map to avoid multiple plan added events for one plan instance
                    myActivePlans.put(plan.toString(), new Object[]{plan, reasonElementName});

                    // Get element
                    String elementName = plan.getModelElement().getName();
                    Element element = engine.getElement(elementName, R.PLAN);

                    // Create emotional event
                    EmotionalEvent emotionalEvent = new EmotionalEvent();

                    // Set emotional event values
                    emotionalEvent.setElementName(elementName);
                    emotionalEvent.setEventType(R.EVT_PLAN_CREATED);
                    emotionalEvent.setResultType(R.RESULT_NULL);
                    emotionalEvent.setUserParameters(params.getUserParameterValues(plan));
                    emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));

                    // Fire emotional event
                    element.processEmotionalEvent(emotionalEvent);

                    // Send emotional message
                  //  messageCenter.sendEmotionalMessage(elementName, R.EVT_PLAN_CREATED, R.RESULT_NULL);

                }
            }
        }*/
    }

    private void handlePlanFinishedEvent() {

       /* // Get currently active agent plans
        IPlan[] agentActivePlans = parentPlan.getPlanbase().getPlans();

        // Iterate plan instances stored in my active plan map to find if we have record of plan which is no longer active
        for (String myActivePlanKey : myActivePlans.keySet()) {

            boolean isStillActive = false;

            // Iterate currently active agent plans
            for (IPlan plan : agentActivePlans) {
                // If plan instance stored in my active plan map is still currently active agent plan break and continue
                // with testing next plan instance stored in my active plan map
                if (myActivePlanKey.equals(plan.toString())) {
                    isStillActive = true;
                    break;
                }
            }

            // If plan instance stored in my active plan map is no longer currently active agent plan fire plan finished emotional event
            if (!isStillActive) {

                Object[] planObject = myActivePlans.get(myActivePlanKey);
                IPlan plan = (IPlan) planObject[0];
                String reason = (String) planObject[1];

                // Remove no longer active plan instance from my active plan map to avoid multiple plan finished events for one plan instance
                myActivePlans.remove(myActivePlanKey);

                // Get element
                String elementName = plan.getModelElement().getName();
                Element element = engine.getElement(elementName, R.PLAN);

                // Create emotional event
                EmotionalEvent emotionalEvent = new EmotionalEvent();

                // Get plan result
                int planResult;
                if (plan.getLifecycleState().equals("passed")) {
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
                emotionalEvent.setUserParameters(params.getUserParameterValues(plan));
                emotionalEvent.setSystemParameters(systemParams);

                // Fire emotional event
                element.processEmotionalEvent(emotionalEvent);

                // Send emotional message
                messageCenter.sendEmotionalMessage(elementName, R.EVT_PLAN_FINISHED, planResult);

            }

        }*/

    }

    public void addGoalsForMonitoring() {

        // Get goal element names
      /*  String[] goalNames = engine.getElementsNames(R.GOAL);

        // Iterate goal element names
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

        // Get currently active agent goals
       /* IGoal[] agentActiveGoals = parentPlan.getGoalbase().getGoals();

        for (IGoal goal : agentActiveGoals) {

            // Check if goal is emotional (it's name is contained in emotional engine)
            if (Arrays.asList(engine.getElementsNames(R.GOAL)).contains(goal.getModelElement().getName())) {

                // Check if goal instance is new and fire goal added emotional event if it is
                if (!myActiveGoals.containsKey(goal.toString())) {

                    // Put new goal instance into active goal map to avoid multiple goal added events for one goal instance
                    myActiveGoals.put(goal.toString(), goal);

                    // Get element
                    String elementName = goal.getModelElement().getName();
                    Element element = engine.getElement(elementName, R.GOAL);

                    // Create emotional event
                    EmotionalEvent emotionalEvent = new EmotionalEvent();

                    // Set emotional event values
                    emotionalEvent.setElementName(elementName);
                    emotionalEvent.setEventType(R.EVT_GOAL_CREATED);
                    emotionalEvent.setResultType(R.RESULT_NULL);
                    emotionalEvent.setUserParameters(params.getUserParameterValues(goal));
                    emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));

                    // Fire emotional event
                    element.processEmotionalEvent(emotionalEvent);

                }
            }
        }*/
    }

    private void handleGoalFinishedEvent() {

        // Get currently active agent goals
      /*  IGoal[] agentActiveGoals = parentPlan.getGoalbase().getGoals();

        // Iterate goal instances stored in my active goal map to find if we have record of goal which is no longer active
        for (String myActiveGoalKey : myActiveGoals.keySet()) {

            boolean isStillActive = false;

            // Iterate currently active agent goals
            for (IGoal goal : agentActiveGoals) {

                // If goal instance stored in my active goal map is still currently active agent goal break and continue
                // with testing next goal instance stored in my active goal map
                if (myActiveGoalKey.equals(goal.toString())) {
                    isStillActive = true;
                    break;
                }
            }

            // If goal instance stored in my active goal map is no longer currently active agent goal fire goal finished emotional event
            if (!isStillActive) {

                IGoal goal = myActiveGoals.get(myActiveGoalKey);

                // Remove no longer active goal instance from my active goal map to avoid multiple goal finished events for one goal instance
                myActiveGoals.remove(myActiveGoalKey);

                // Get element
                String elementName = goal.getModelElement().getName();
                Element element = engine.getElement(elementName, R.GOAL);

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
                emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));

                // Fire emotional event
                element.processEmotionalEvent(emotionalEvent);

            }

        }*/

    }

    public void addBeliefsForMonitoring() {

        // Get belief element names
       /* String[] beliefNames = engine.getElementsNames(R.BELIEF);

        // Iterate plan element names
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

    private void handleBeliefChangedEvent(EmoBelief belief) {

        // Get element
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

        /*// Get belief element names
        String[] beliefSetNames = engine.getElementsNames(R.BELIEF_SET);

        // Iterate plan element names
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

                        // Remove element from the engine
                        EmotionalBelief emotionalBelief = (EmotionalBelief) ae.getValue();
                        engine.removeElement(emotionalBelief.getName(), R.BELIEF_SET_BELIEF);

                    }
                });
            }
        }*/
    }

    private void handleFactChangedEvent(EmoBelief emotionalBelief) {

        // Get element
        String elementName = emotionalBelief.beliefName();
        Element element = engine.getElement(elementName, R.BELIEF_SET_BELIEF);

        // Check if element is valid
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


}
