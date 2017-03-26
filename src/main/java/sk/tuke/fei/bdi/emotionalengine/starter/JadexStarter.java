package sk.tuke.fei.bdi.emotionalengine.starter;

import jadex.base.PlatformConfiguration;
import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ITuple2Future;
import jadex.commons.future.ThreadSuspendable;

import java.util.*;

/**
 * Created by Peter on 21.3.2017.
 */
public class JadexStarter {

    public static Set<IComponentIdentifier> CIDs = new HashSet<IComponentIdentifier>();

    public static IComponentManagementService CMS;

    public JadexStarter() {

    }

    public static void start(String[] args, HashMap<String, String> agents) {

        PlatformConfiguration configuration;

        IFuture<IExternalAccess> fut = Starter.createPlatform(args);
        IExternalAccess platform = fut.get();

        IFuture<IComponentManagementService> future = SServiceProvider.getService(platform, IComponentManagementService.class);

        IComponentManagementService cms = future.get();

      //  PlatformConfiguration   config  = PlatformConfiguration.getDefaultNoGui();

        Iterator it = agents.entrySet().iterator();

        //CMS = cms;

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();
            String agentName = (String) pair.getKey();
            String agentModel = (String) pair.getValue();

            ITuple2Future<IComponentIdentifier, Map<String, Object>> fut_cid =
                    cms.createComponent(agentName, agentModel, null);

            IComponentIdentifier cid =  fut_cid.getFirstResult();

            System.out.println("Started component: " + cid);

            CIDs.add(cid);
            //config.addComponent(agentModel);

        }

     //   Starter.createPlatform(config).get();
    }
}
