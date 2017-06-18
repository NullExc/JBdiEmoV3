package sk.tuke.fei.bdi.emotionalengine.starter;

import jadex.base.PlatformConfiguration;
import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.SUtil;
import jadex.commons.future.IFuture;
import jadex.commons.future.ITuple2Future;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Peter Zemianek
 */

public class JBDIEmo {

    //public static  Map<String, EmotionalPlan> UserPlanParams = new LinkedHashMap<String, EmotionalPlan>();
    //public static  Map<String, EmotionalGoal> UserGoalParams = new LinkedHashMap<String, EmotionalGoal>();

    public static  Map<String, Map<String, EmotionalPlan>> UserPlanParams = new LinkedHashMap<>();
    public static  Map<String, Map<String, EmotionalGoal>> UserGoalParams = new LinkedHashMap<>();

    public static Set<IComponentIdentifier> MessageListeners = new HashSet<>();

    public static boolean JBDIEmoReady = false;

    public static IComponentManagementService CMS;

    public JBDIEmo() {

    }

    public static void start(Map<String, String> agents) {



        PlatformConfiguration configuration = PlatformConfiguration.getDefaultNoGui();

        configuration.setDebugFutures(true);


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

        JBDIEmoReady = true;
    }

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
