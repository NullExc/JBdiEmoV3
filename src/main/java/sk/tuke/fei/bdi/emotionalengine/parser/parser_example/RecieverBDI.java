package sk.tuke.fei.bdi.emotionalengine.parser.parser_example;

import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.*;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IResultListener;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;

/**
 * Created by Peter on 23.3.2017.
 */
@Agent
@JBDIEmoAgent(guiEnabled = true, loggerEnabled = false, others = "Sender")
@Plans(@Plan(body = @Body(InitializeEmotionalEnginePlan.class)))
@RequiredServices({
        @RequiredService(name = R.COMPONENT_SERVICE, type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
})
public class RecieverBDI {

    @Agent
    private IInternalAccess agentAccess;

    @AgentFeature
    protected IBDIAgentFeature agentFeature;

    @AgentFeature
    protected IExecutionFeature execFeature;

    @Belief
    protected Engine engine = new Engine();

    public RecieverBDI() {

    }

    @AgentCreated
    public void init() {

    }

    @AgentBody
    public void body() {

        agentFeature.adoptPlan(new InitializeEmotionalEnginePlan(this, engine)).get();

    }
}
