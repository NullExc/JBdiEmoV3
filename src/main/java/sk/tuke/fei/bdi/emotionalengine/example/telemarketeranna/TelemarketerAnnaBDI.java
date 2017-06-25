package sk.tuke.fei.bdi.emotionalengine.example.telemarketeranna;

/**
 * Created by PeterZemianek on 5/14/2017.
 */

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;

import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.component.IExecutionFeature;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;

@Agent
@JBDIEmoAgent(guiEnabled = true, others = "HungryPaul")
@Plans(@Plan(body = @Body(InitializeEmotionalEnginePlan.class)))
@RequiredServices({
        @RequiredService(name = R.COMPONENT_SERVICE, type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
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

        agentFeature.adoptPlan(new InitializeEmotionalEnginePlan(this)).get();

        execFeature.repeatStep(0, 20000, iInternalAccess -> {

            agentFeature.dispatchTopLevelGoal(new MakeCall()).get();

            return IFuture.DONE;
        });

    }

    @Goal(randomselection = true)
    public class MakeCall {

        @EmotionalGoal
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

