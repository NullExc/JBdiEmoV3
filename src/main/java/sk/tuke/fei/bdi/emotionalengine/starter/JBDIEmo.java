package sk.tuke.fei.bdi.emotionalengine.starter;

import jadex.base.PlatformConfiguration;
import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ITuple2Future;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Peter Zemianek
 *
 * Controller of JBdiEmo engine.
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
     * Set of all Emotional Agents who are using ICommunicationService
     */
    public static Set<IComponentIdentifier> MessageListeners = new HashSet<>();

    public JBDIEmo() {

    }

    /**
     * Function called by user to start Jadex.
     * @param agents Map object containing Key-Value pair
     * Key is Agent's name in JBdiEmo engine
     * Value is Agent's full class path
     * Example : Map.put("HelloAgent", "com.example.main.HelloAgentBDI.class")
     */
    public static void start(Map<String, String> agents) {

        PlatformConfiguration configuration = PlatformConfiguration.getDefaultNoGui();

        IFuture<IExternalAccess> fut = Starter.createPlatform(configuration);
        IExternalAccess platform = fut.get();

        IFuture<IComponentManagementService> future = SServiceProvider.getService(platform, IComponentManagementService.class);

        IComponentManagementService cms = future.get();

        Iterator it = agents.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();
            String agentName = (String) pair.getKey();
            String agentModel = (String) pair.getValue();

            ITuple2Future<IComponentIdentifier, Map<String, Object>> fut_cid =
                    cms.createComponent(agentName, agentModel, null);

            IComponentIdentifier cid =  fut_cid.getFirstResult();

            System.out.println("Started component: " + cid);
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
