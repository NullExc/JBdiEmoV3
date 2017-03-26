package sk.tuke.fei.bdi.emotionalengine.BDIParser.parser_example;

import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.bridge.service.types.message.IMessageService;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.JBdiEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;

/**
 * Created by Peter on 23.3.2017.
 */
@Agent
@JBdiEmoAgent(others = {"1", "2", "3", "4"}, guiEnabled = true, loggerEnabled = false)
@Plans({@Plan(body = @Body(CryPlan.class)),
        @Plan(body = @Body(InitializeEmotionalEnginePlan.class))})
@RequiredServices({
        @RequiredService(name = "messageservice", type = IMessageService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL)),
        @RequiredService(name = "CMS", type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
public class RecieverBDI {

    @Agent
    private IInternalAccess agentAccess;

    @AgentFeature
    protected IBDIAgentFeature agentFeature;

    @AgentFeature
    protected IExecutionFeature execFeature;

    public RecieverBDI() {

    }

    @AgentCreated
    public void init() {


    }

    @AgentBody
    public void body() {

        agentFeature.adoptPlan(new InitializeEmotionalEnginePlan(this.getClass(), agentAccess, new String[]{"Sender"})).get();

    }
}
