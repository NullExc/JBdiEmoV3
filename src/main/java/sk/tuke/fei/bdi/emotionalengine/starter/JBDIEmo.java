package sk.tuke.fei.bdi.emotionalengine.starter;

import jadex.base.PlatformConfiguration;
import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.modelinfo.IModelInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ITuple2Future;
import org.reflections.Reflections;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller of JBdiEmo engine.
 *
 * @author Peter Zemianek
 */
public class JBDIEmo {

    /**
     * Storage of all Emotional Plan Parameters for each Emotional Agent
     */
    public static  Map<String, Map<String, EmotionalPlan>> UserPlanParams = new LinkedHashMap<>();

    /**
     * Storage of all Emotional Goal Parameters for each Emotional Agent
     */
    public static  Map<String, Map<String, EmotionalGoal>> UserGoalParams = new LinkedHashMap<>();

    /**
     * PLATFORM, which can be used to retrieve global services.
     */
    public static IExternalAccess PLATFORM = null;

    public JBDIEmo() {

    }

    /**
     * Function called by user to start Jadex.
     * @param agents Map object containing Key-Value pair
     * @param platform Platform provided by user. If Null, Platform will be created with default settings
     * @param rootPackage Name of root package of all emotional agents.
     * Key is Agent's name in JBdiEmo engine
     * Value is Agent's full class path
     * Example : Map.put("HelloAgent", "com.example.main.HelloAgentBDI.class")
     */
    public static void start(Map<String, String> agents, IExternalAccess platform, String rootPackage) throws JBDIEmoException {

        Reflections reflections = new Reflections(rootPackage);

        reflections.getTypesAnnotatedWith(EmotionalPlan.class);

        reflections.getTypesAnnotatedWith(EmotionalGoal.class);

        PlatformConfiguration configuration = null;

        if (platform == null) {
            configuration = PlatformConfiguration.getDefaultNoGui();

            PLATFORM = Starter.createPlatform(configuration).get();

        } else {
            PLATFORM = platform;
        }

        IFuture<IComponentManagementService> future = SServiceProvider.getService(PLATFORM, IComponentManagementService.class);

        IComponentManagementService cms = future.get();

        Iterator it = agents.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();
            String agentName = (String) pair.getKey();
            String agentModel = (String) pair.getValue();

            ITuple2Future<IComponentIdentifier, Map<String, Object>> fut_cid =
                    cms.createComponent(agentName, agentModel, null);

            IComponentIdentifier cid =  fut_cid.getFirstResult();

            IModelInfo modelInfo = SServiceProvider.getService(PLATFORM, IComponentManagementService.class).get()
                    .getExternalAccess(cid).get().getModel();

            if (modelInfo.getConfiguration(R.INIT_PLAN) == null) {
                throw new JBDIEmoException("The agent '" + cid.getLocalName() +
                        "' hasn't InitializeEmotionalEnginePlan. Pleas add this plan to the BDI Configuration.");
            }

            System.out.println("Started component : " + cid.getLocalName());
        }
    }

    /**
     * Helper function to retrieve agent's jadex component
     * @param object instance of agent
     * @param clazz class definition of requested agent's component
     * @param <T> Generic type of component
     * @return Agent's Jadex component
     * @throws JBDIEmoException
     */
    public static <T> T findAgentComponent(Object object, Class<T> clazz) throws JBDIEmoException {

        Object component = null;

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(clazz)) {
                field.setAccessible(true);
                try {
                    component = field.get(object);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (object == null) {
            throw new JBDIEmoException("Agent class has missing field for component : " + clazz.getSimpleName());
        }

        return (T) component;
    }

    /**
     * Helper function to find Emotional Parameter of Emotional Plan.
     * @param plan Emotional Plan
     * @param paramName requested Emotional Parameter name
     * @return Emotional Parameter
     */
    public static EmotionalParameter findPlanParameter(EmotionalPlan plan, String paramName) {

        if (plan == null) {
            return null;
        }

        for (EmotionalParameter parameter : plan.value()) {
            if (parameter.parameter().equals(paramName)) {
                return parameter;
            }
        }
        return null;
    }

}
