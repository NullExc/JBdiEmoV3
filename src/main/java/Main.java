

import sk.tuke.fei.bdi.emotionalengine.starter.JadexStarter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class Main {
    
    public static void main(String[] args) {


        //PlatformConfiguration config  = PlatformConfiguration.getDefaultNoGui();

       // config.addComponent(SenderBDI.class);

        String[] defaultArgs = new String[]
                {
                        "-gui", "false",
                        "-welcome", "false",
                        "-cli", "false",
                        "-printpass", "false"
                };

       /* IFuture<IExternalAccess> fut = Starter.createPlatform(defaultArgs);
        IExternalAccess platform = fut.get();

        IFuture<IComponentManagementService> future = SServiceProvider.getService(platform, IComponentManagementService.class);

        IComponentManagementService cms = future.get();

        ITuple2Future<IComponentIdentifier, Map<String, Object>> fut_cid =
                cms.createComponent(SenderBDI.class.getSimpleName(), SenderBDI.class.getName() + ".class", null);

        IComponentIdentifier cid = fut_cid.getFirstResult();

        System.out.println("Started component: " + cid);*/

        //new GoalMapper(SenderBDI.class);

        HashMap<String, String> agents = new HashMap<String, String>();
        agents.put("Reciever", "sk.tuke.fei.bdi.emotionalengine.BDIParser.parser_example.RecieverBDI.class");
        agents.put("Sender", "sk.tuke.fei.bdi.emotionalengine.BDIParser.parser_example.SenderBDI.class");


        JadexStarter.start(defaultArgs, agents);

    }
}
