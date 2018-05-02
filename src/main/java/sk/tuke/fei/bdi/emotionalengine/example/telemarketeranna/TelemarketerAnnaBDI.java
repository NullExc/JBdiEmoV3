package sk.tuke.fei.bdi.emotionalengine.example.telemarketeranna;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IInternalAccess;
import jadex.bridge.nonfunctional.annotation.NameValue;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;

import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.component.IExecutionFeature;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.annotation.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.component.service.ICommunicationService;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

@Agent
@JBDIEmoAgent(guiEnabled = true, others = "HungryPaul")
@Plans(@Plan(body = @Body(InitializeEmotionalEnginePlan.class)))
@RequiredServices({
        @RequiredService(name = R.COMPONENT_SERVICE, type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
})
@BDIConfigurations({
        @BDIConfiguration(name = R.INIT_PLAN,initialplans = @NameValue(name=R.INIT_PLAN,clazz=InitializeEmotionalEnginePlan.class))
})
public class TelemarketerAnnaBDI {

    @AgentFeature
    protected IExecutionFeature execFeature;

    @AgentFeature
    protected IBDIAgentFeature agentFeature;

    @Agent
    private IInternalAccess agentAccess;

    @Belief
    protected Engine engine = new Engine();

    @AgentCreated
    public void init() {

    }

    @AgentBody
    public void body() {

        execFeature.repeatStep(0, 20000, iInternalAccess -> {

            agentFeature.dispatchTopLevelGoal(new MakeCall()).get();

            return IFuture.DONE;
        });

    }

    @EmotionalGoal
    @Goal(randomselection = true)
    public class MakeCall {

        public MakeCall() {

        }

    }

    @Plan(trigger=@Trigger(goals = MakeCall.class))
    @EmotionalPlan
    protected void advertiseBioFoodStore(IPlan plan) {

        plan.waitFor((int) (2000 + Math.random() * 3000)).get();

        System.out.println("");
        System.out.println("Anna called Paul to advertise local bio food store - Natural as You");
        System.out.println("");
    }

    @Plan(trigger=@Trigger(goals = MakeCall.class))
    @EmotionalPlan
    protected void advertiseLocalFastFood(IPlan plan) {

        plan.waitFor((int) (2000 + Math.random() * 3000)).get();

        System.out.println("");
        System.out.println("Anna called Paul to advertise local fast food - Bacon x Bacon");
        System.out.println("");

    }

}

