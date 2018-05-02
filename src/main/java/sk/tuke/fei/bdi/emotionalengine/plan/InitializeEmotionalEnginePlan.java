package sk.tuke.fei.bdi.emotionalengine.plan;


import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import sk.tuke.fei.bdi.emotionalengine.annotation.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;
import sk.tuke.fei.bdi.emotionalengine.component.enginegui.EngineGui;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.AgentModelMapper;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.ElementEventMonitor;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.PlatformOtherMapper;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.component.logger.EngineLogger;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Plan responsible for initialization process of JBDIEmo components. It includes mapping agents, monitoring emotional elements
 * and searching for other agents in platform.
 *
 * Plan has to be executed before running of agent's body.
 *
 * @author Peter Zemianek
 */
@Plan
public class InitializeEmotionalEnginePlan {

    /**
     * Maps all emotional BDI elements
     */
    private AgentModelMapper agentModelMapper;
    /**
     * Maps other agents in platform to communicate with them
     */
    private PlatformOtherMapper platformOtherMapper;
    /**
     * Plain object of agent.
     */
    private Object agentObject;
    /**
     * Core of JBDIEmo.
     */
    private Engine engine;
    /**
     * Annotation, containing all of initialize parameters.
     */
    private JBDIEmoAgent emotionalAgent;
    /**
     * String array of all other emotional agent's names.
     */
    private String[] emotionalOthers;

    /**
     * @param agent Jadex automatically injects instance of agent's class
     */
    public InitializeEmotionalEnginePlan(Object agent) {
        this.agentObject = agent;
    }

    /**
     * Body of the plan execution. Starts initialization process.
     * @param access
     * @throws JBDIEmoException This plan throws JBDIEmoException when a agent doesn't have 'engine' Belief or JBDIEmo
     * annotation
     */
    @PlanBody
    public void body(IInternalAccess access) throws JBDIEmoException {

        handleExepctions(access);

        this.emotionalOthers = emotionalAgent.others().split(",");

        this.engine = (Engine) access.getComponentFeature(IInternalBDIAgentFeature.class)
                .getBDIModel().getCapability().getBelief("engine").getValue(access);

        engine.setAgentName(access.getComponentIdentifier().getName());
        engine.setAgentObject(agentObject);

        JBDIEmo.UserPlanParams.put(engine.getAgentName(), new LinkedHashMap());
        JBDIEmo.UserGoalParams.put(engine.getAgentName(), new LinkedHashMap());

        agentModelMapper = new AgentModelMapper(agentObject, access);
        platformOtherMapper = new PlatformOtherMapper(access);

        mapAgentModel();
        mapAgentOther();

        initializeEngine();
        initializeGui();
        initializeEngineLogger();

        MessageCenter messageCenter = new MessageCenter(access);

        ElementEventMonitor elementEventMonitor = new ElementEventMonitor(access, agentObject, messageCenter);

        elementEventMonitor.goalsAndPlansMonitoring();
        elementEventMonitor.beliefMonitoring();
        elementEventMonitor.beliefSetMonitoring();
    }

    private void mapAgentModel() {

        System.out.println("");
        System.out.println("---------- Map Agent Elements ---------");

        agentModelMapper.mapEmotionalAgents();
        agentModelMapper.mapPlans();
        agentModelMapper.mapGoals();
        agentModelMapper.mapBeliefs();

        System.out.println("---------------------------------------");
        System.out.println("");
    }

    private void mapAgentOther() {

        String[] otherNames = null;

        // Get emotional other names parameter from ADF if exists
        if (emotionalOthers.length != 0) {
            otherNames = emotionalOthers;
        }

        // If other names are valid
        if (otherNames != null) {

            // Set emotional other names to other mapper for further ADF mapping
            // Other mapper will try to find emotional other agents on current platform
            platformOtherMapper.setEmotionalOtherNames(new HashSet<String>(Arrays.asList(otherNames)));

            // Run thread in other mapper to search current platform for defined emotional other agentClass names
            platformOtherMapper.setRunning(true);
        }
    }

    private void initializeEngine() {

        // Get decay time parameter from ADF if exists and set decay time in engine
        engine.setDecayDelay(emotionalAgent.decayTimeMillis());

        // Get decay steps parameter from ADF if exists and set decay steps in engine
        engine.setDecaySteps(emotionalAgent.decayStepsToMin());

        // Set engine initialized
        engine.setInitialized(true);
    }

    private void initializeGui() {

        boolean isGui;

        isGui = emotionalAgent.guiEnabled();

        // If guy parameter is true start gui
        if (isGui) {
            EngineGui gui = new EngineGui(engine);
        }
    }

    private void initializeEngineLogger() {

        boolean isLogger = false;
        Integer loggingDelayMillis = null;

        isLogger = emotionalAgent.loggerEnabled();

        loggingDelayMillis = emotionalAgent.loggingDelayMillis();

        // If logger parameter is true start logging
        if (isLogger) {
            EngineLogger logger = new EngineLogger(loggingDelayMillis, engine);
        }
    }

    private void handleExepctions(IInternalAccess access) throws JBDIEmoException {

        this.emotionalAgent = agentObject.getClass().getAnnotation(JBDIEmoAgent.class);

        if (emotionalAgent == null) {

            access.killComponent();

            throw new JBDIEmoException("The agent '" + access.getComponentIdentifier().getLocalName()
                    + "' has no 'JBDIEmoAgent' annotation. Please define JBDIEmo annotation on agent's class.");
        }

        if (access.getComponentFeature(IInternalBDIAgentFeature.class)
                .getBDIModel().getCapability().getBelief("engine") == null) {

            access.killComponent();

            throw new JBDIEmoException("Emotional agent '" + access.getComponentIdentifier().getLocalName()
                    + "' has no 'engine' belief. Please define Engine belief in agent class.");
        }

    }
}