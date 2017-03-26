package sk.tuke.fei.bdi.emotionalengine.BDIParser.parser_example;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.annotation.Publish;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bridge.IInternalAccess;
import jadex.bridge.IMessageAdapter;
import jadex.bridge.component.*;
import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.bridge.service.types.message.IMessageService;
import jadex.bridge.service.types.message.MessageType;
import jadex.commons.IFilter;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.JBdiEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peter on 20.3.2017.
 */
@Agent
@JBdiEmoAgent(others = {"1", "2", "3"}, guiEnabled = true, loggerEnabled = false)
@Goals(@Goal(clazz = CryGoal.class))
@Plans({@Plan(body = @Body(CryPlan.class)),
        @Plan(body = @Body(InitializeEmotionalEnginePlan.class))})
@RequiredServices({
        @RequiredService(name = "messageservice", type = IMessageService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL)),
        @RequiredService(name = "CMS", type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
@Description("my new bdi agent with JBdiEmo support")
public class SenderBDI {

    @Belief
    protected String emoBelief;

    @AgentFeature
    protected IBDIAgentFeature agentFeature;

    @AgentFeature
    protected IExecutionFeature execFeature;

    @AgentFeature
    protected IMessageFeature feature;

    @AgentFeature
    protected IMonitoringComponentFeature monitor;

    @Agent
    private IInternalAccess agentAccess;



    public SenderBDI() {

    }

    @AgentCreated
    public void init() {


    }

    @AgentBody
    public void body() {


        feature.sendMessage(new HashMap<String,Object>(), SFipa.FIPA_MESSAGE_TYPE);

        agentFeature.adoptPlan(new InitializeEmotionalEnginePlan(this.getClass(), agentAccess, new String[]{"Reciever"})).get();

        //IMessageService cs = (IMessageService) agentAccess.getComponentFeature(IRequiredServicesFeature.class).getRequiredService("messageservice").get();
      /*  IComponentManagementService cms = (IComponentManagementService) agentAccess.getComponentFeature(IRequiredServicesFeature.class).getRequiredService("CMS").get();

        IFuture<IComponentIdentifier[]> identifiersFuture = cms.getComponentIdentifiers();

        IComponentIdentifier[] identifiers = identifiersFuture.get();

        IComponentIdentifier iComponentIdentifier = null;

        for (IComponentIdentifier cid : identifiers) {
            System.out.println(cid.getName());
            if (cid.getLocalName().equals("Reciever")) {
                iComponentIdentifier = cid;
            }
        }*/

        //System.out.println(iComponentIdentifier.getLocalName());
    }


}
