package sk.tuke.fei.bdi.emotionalengine.parser.parser_example;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bdiv3.runtime.impl.RParameterElement;
import jadex.bridge.*;
import jadex.bridge.component.*;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.*;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.plan.ReceiveEmotionalMessageServicePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;

/**
 * Created by Peter on 20.3.2017.
 */
@Agent
@JBDIEmoAgent(guiEnabled = true, loggerEnabled = false, others = "Reciever")
@Goals({
        @Goal(clazz = CryGoal.class)
})
@Plans({@Plan(body = @Body(CryPlan.class)),
        @Plan(body = @Body(InitializeEmotionalEnginePlan.class)),
        @Plan(body = @Body(ReceiveEmotionalMessageServicePlan.class))})
@RequiredServices({
        @RequiredService(name = R.COMPONENT_SERVICE, type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
})
@Arguments({@Argument(name = R.ENGINE, description = "Engine component", clazz = Engine.class)})
public class SenderBDI {

    @AgentFeature
    protected IBDIAgentFeature agentFeature;

    @AgentFeature
    protected IExecutionFeature execFeature;

    @Agent
    private IInternalAccess agentAccess;

    public double time = System.currentTimeMillis();//(Math.random() * 0.5) + 0.5;

    @AgentCreated
    public void init() {



    }

    @AgentBody
    public void body() {

        agentFeature.adoptPlan(new InitializeEmotionalEnginePlan(this)).get();

        agentFeature.dispatchTopLevelGoal(new CryGoal("Peterek")).get();

        agentFeature.adoptPlan("senderPlan").get();

    }


    @EmotionalPlan({
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldValue = "time"),
            @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.METHOD, methodValue = "getRandomNumber"),
            @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.SIMPLE_DOUBLE, doubleValue = 0.5d)}
    )
    @Plan(trigger=@Trigger(goals=CryGoal.class))
    public String senderPlan(IPlan plan) {

        System.out.println("Start senderPlan .....");
        plan.waitFor(5000).get();
        System.out.println("End senderPlan .....");

        return "Hello";



    }

    public double getRandomNumber() {
        return (Math.random() * 0.5) + 0.5;
    }

}
