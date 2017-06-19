package sk.tuke.fei.bdi.emotionalengine.plan;


import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.component.IProvidedServicesFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IResultListener;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;
import sk.tuke.fei.bdi.emotionalengine.component.enginegui.EngineGui;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.AgentModelMapper;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.ElementEventMonitor;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.PlatformOtherMapper;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.component.logger.EngineLogger;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * @author Peter Zemianek
 */
@Plan
public class InitializeEmotionalEnginePlan {

    private AgentModelMapper agentModelMapper;
    private PlatformOtherMapper platformOtherMapper;
    private ElementEventMonitor elementEventMonitor;
    private EngineGui gui;
    private EngineLogger logger;
    private Object agentObject;
    private IInternalAccess access;
    private IBDIAgentFeature bdiFeature;
    private Engine engine;
    private JBDIEmoAgent emotionalAgent;
    private String[] emotionalOthers;
    private MessageCenter messageCenter;

    public InitializeEmotionalEnginePlan(Object agent, Engine engine) {
        this.agentObject = agent;
        this.emotionalAgent = agent.getClass().getAnnotation(JBDIEmoAgent.class);

        this.emotionalOthers = emotionalAgent.others().split(R.MESSAGE_DELIMITER);

        try {
            this.access = JBDIEmo.findAgentComponent(agent, IInternalAccess.class);
            this.bdiFeature = JBDIEmo.findAgentComponent(agent, IBDIAgentFeature.class);
        } catch (JBDIEmoException e) {
            e.printStackTrace();
        }

        this.engine = engine;

        engine.setAgentName(access.getComponentIdentifier().getName());
        engine.setAgentObject(agent);

        JBDIEmo.UserPlanParams.put(engine.getAgentName(), new LinkedHashMap<>());
        JBDIEmo.UserGoalParams.put(engine.getAgentName(), new LinkedHashMap<>());
    }

    @PlanBody
    public void body() {

        agentModelMapper = new AgentModelMapper(agentObject, engine, access);
        platformOtherMapper = new PlatformOtherMapper(engine, access);

        mapAgentModel();
        mapAgentOther();

        initializeEngine();
        initializeGui();
        initializeEngineLogger();

        messageCenter = new MessageCenter(agentObject, engine);

        elementEventMonitor = new ElementEventMonitor(agentObject, engine, messageCenter);

        elementEventMonitor.goalsAndPlansMonitoring();
        elementEventMonitor.beliefMonitoring();
        elementEventMonitor.beliefSetMonitoring();

        ICommunicationService service = (ICommunicationService) access.getComponentFeature(IProvidedServicesFeature.class)
                .getProvidedService(R.MESSAGE_SERVICE);

        service.setEngine(new Future<>(engine)).get();

        service.initialize(new Future<>(access), access.getComponentIdentifier()).addResultListener(new IResultListener<Void>() {
            @Override
            public void exceptionOccurred(Exception exception) {
                exception.printStackTrace();
            }

            @Override
            public void resultAvailable(Void result) {
                JBDIEmo.MessageListeners.add(access.getComponentIdentifier());
            }
        });

    }

    private void mapAgentModel() {

        System.out.println("");
        System.out.println("---------- Map Agent Elements ---------");

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

        boolean isGui = false;

        isGui = emotionalAgent.guiEnabled();

        // If guy parameter is true start gui
        if (isGui) {
            gui = new EngineGui(engine);
        }
    }

    private void initializeEngineLogger() {

        boolean isLogger = false;
        Integer loggingDelayMillis = null;

        isLogger = emotionalAgent.loggerEnabled();

        loggingDelayMillis = emotionalAgent.loggingDelayMillis();

        // If logger parameter is true start logging
        if (isLogger) {
            logger = new EngineLogger(loggingDelayMillis, engine);
        }

    }
}