package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.IGoal;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.nonfunctional.annotation.NameValue;
import jadex.bridge.service.types.chat.IChatService;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan.*;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;

import java.util.*;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@Plans({@Plan(body = @Body(InitializeEmotionalEnginePlan.class)),
        @Plan(body = @Body(MetabolismServicePlan.class)),
        @Plan(body = @Body(EatHealthyPlan.class))
})
@Goals({
        @Goal(clazz = EmotionalCapability.Translate.class)
})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
})
@BDIConfigurations({
        @BDIConfiguration(name = "initial", initialplans = {@NameValue(name = "initial",clazz = InitializeEmotionalEnginePlan.class)})
})
@Agent
@JBDIEmoAgent(guiEnabled = true, others = "TelemarketerAnna")
public class HungryPaulBDI {

    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IBDIAgentFeature agentFeature;

    @Agent
    private IInternalAccess agentAccess;

    @Belief
    public Engine engine = new Engine();

    @Belief
    public Set<EmotionalBelief> food = new HashSet<>();

    @Belief
    protected EmotionalBelief testBelief = new EmotionalBelief("sokeres");

    @Capability
    protected EmotionalCapability capability = new EmotionalCapability();

    private Hunger hunger = new Hunger(0.55);

    public double approval = 0.4;

    public double disapproval = 0.4;

    public double eatHealthyFoodDesirability() {
        return getHunger().getHungerValue();
    }

    @AgentCreated
    public void init() {

    }

    @AgentBody
    public void body() {

       // agentFeature.dispatchTopLevelGoal(capability.new Translate("")).get();

//        System.err.println(agentAccess.getComponentFeature(BDIMonitoringComponentFeature.class).getCurrentStateEvents().size());

        execFeature.waitForDelay(5000).get();

       // agentFeature.adoptPlan(capability.new CapaClassPlan()).get();

        capability.setBelief(true);

        capability.updateBelief();

        agentFeature.adoptPlan(new MetabolismServicePlan()).get();

        //capability.capaBelief = true;
    }

    @Goal(deliberation = @Deliberation(cardinalityone = true))
    public static class EatHealthyFood {

        @GoalAPI
        IGoal goal;

        @GoalParameter
        protected Hunger hunger;

        public double getHungerValue() {
            return hunger.getHungerValue();
        }

        @EmotionalGoal({
                @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.METHOD, methodName = "getHungerValue", agentClass = false)
        })
        public EatHealthyFood(Hunger hunger) {
            this.hunger = hunger;
        }

        @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.BELIEFCHANGED, second = "hunger")})
        public static EatHealthyFood creationCondition(Hunger hunger) {
            if (hunger.getHungerValue() > 0.7) {
                return new EatHealthyFood(hunger);
            }
            return null;
        }
    }

    @Plan(trigger = @Trigger(factchangeds = "testBelief"))
    protected void testPlan() {
        System.err.println("testPlan triggered");
    }

    @Belief
    public Hunger getHunger() {
        return hunger;
    }

    @Belief
    public void setHunger(Hunger hunger) {
        this.hunger = hunger;
    }
}
