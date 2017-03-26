package sk.tuke.fei.bdi.emotionalengine.plan;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   29. 10. 2012
   9:30 AM

*/

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IInternalAccess;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.JBdiEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.enginegui.EngineGui;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.AgentModelMapper;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.ElementEventMonitor;
import sk.tuke.fei.bdi.emotionalengine.component.engineinitialization.PlatformOtherMapper;
import sk.tuke.fei.bdi.emotionalengine.component.logger.EngineLogger;

import java.util.Arrays;
import java.util.HashSet;

@Plan
public class InitializeEmotionalEnginePlan {

    private AgentModelMapper agentModelMapper;
    private PlatformOtherMapper platformOtherMapper;
    private ElementEventMonitor elementEventMonitor;
    private EngineGui gui;
    private EngineLogger logger;

    private final Class<?> agent;
    private final IInternalAccess access;

    private Engine engine;

    private JBdiEmoAgent emotionalAgent;
    private String[] emotionalOthers;

    @PlanAPI
    private IPlan plan;

    public InitializeEmotionalEnginePlan(Class<?> agent, IInternalAccess access, String[] emotionalOthers) {
        this.agent = agent;
        this.access = access;
        emotionalAgent = agent.getAnnotation(JBdiEmoAgent.class);
        this.emotionalOthers = emotionalOthers;
        System.err.println("length of other agents : " + emotionalAgent.others().length);
        setEngine(new Engine());
    }

    @PlanBody
    public void body() {

        agentModelMapper = new AgentModelMapper(agent, engine);
        platformOtherMapper = new PlatformOtherMapper(agent, engine, access);
        elementEventMonitor = new ElementEventMonitor(engine, plan);

        mapAgentModel();
        mapAgentOther();
        addElementsForMonitoring();
        initializeEngine();
        initializeGui();
        initializeEngineLogger();

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

    private void addElementsForMonitoring() {

        System.out.println("");
        System.out.println("-------- Monitor Agent Elements -------");

        elementEventMonitor.addGoalsForMonitoring();

        System.out.println("");

        elementEventMonitor.addPlansForMonitoring();

        System.out.println("");

        elementEventMonitor.addBeliefsForMonitoring();

        System.out.println("");

        elementEventMonitor.addBeliefSetsForMonitoring();

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

            // Run thread in other mapper to search current platform for defined emotional other agent names
            platformOtherMapper.setRunning(true);

        }

    }

    private void initializeEngine() {

        engine.setAgentName(access.getComponentIdentifier().getName());

        // Get decay time parameter from ADF if exists and set decay time in engine

        engine.setDecayDelay(emotionalAgent.decayTimeMillis());


        // Get decay steps parameter from ADF if exists and set decay steps in engine
        engine.setDecaySteps(emotionalAgent.decayStepsToMin());

        // Set engine initialized
        engine.setInitialized(true);

    }

    private void initializeGui() {

        boolean isGui = false;

        // Get guy parameter from ADF if exist get value

        isGui = emotionalAgent.guiEnabled();

        // If guy parameter is true start gui
        if (isGui) {
            gui = new EngineGui(this);
        }

    }


    private void initializeEngineLogger() {

        // Get engine instance
  //     Engine engine = (Engine) getBeliefbase().getBelief(R.ENGINE).getFact();

        boolean isLogger = false;
        Integer loggingDelayMillis = null;


        isLogger = emotionalAgent.loggerEnabled();

        loggingDelayMillis = emotionalAgent.loggingDelayMillis();

        // If logger parameter is true start logging
        if (isLogger) {
            logger = new EngineLogger(loggingDelayMillis, engine);
        }

    }


    private Object getParameterValue(String parameterName) {

        try {
         //   return getParameter(parameterName).getValue();
        } catch (Exception e) {
//          Try catch used because it can't be tested otherwise because Jadex bug where you
//          can't get parameter array of plan but you can get parameter by name
            return null;
        }
        return  null; //len kvoli spusteniu!!!!!!!!!!!!
    }

    private Object[] getParameterSetValues(String parameterSetName) {

        try {
            Object[] values = null; //= getParameterSet(parameterSetName).getValues();
            return values;
        } catch (Exception e) {
//          Try catch used because it can't be tested otherwise because Jadex bug where you
//          can't get parameter array of plan but you can get parameter by name
            return null;
        }

    }

    public EngineGui getGui() {
        return gui;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}