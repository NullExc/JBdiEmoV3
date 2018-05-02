package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.IGoal;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.nonfunctional.annotation.NameValue;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.annotation.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.component.service.ICommunicationService;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Fridge;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.MyFoodEmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan.*;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@Plans({@Plan(body = @Body(InitializeEmotionalEnginePlan.class)),
        @Plan(body = @Body(CallAnnaOutPlan.class)),
        @Plan(body = @Body(MetabolismServicePlan.class)),
        @Plan(body = @Body(EatHealthyPlan.class), priority = 1),
        @Plan(body = @Body(EatUnhealthyPlan.class), priority = 2),
        @Plan(body = @Body(DonateOldClothesPlan.class)),
        @Plan(body = @Body(SprayPaintBillboardPlan.class)),
        @Plan(body = @Body(ForgetOldEatenFoodPlan.class))
})
@RequiredServices({
        @RequiredService(name = R.COMPONENT_SERVICE, type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
})
@BDIConfigurations({
        @BDIConfiguration(name = R.INIT_PLAN,initialplans = @NameValue(name=R.INIT_PLAN,clazz=InitializeEmotionalEnginePlan.class))
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
    public Fridge fridge = new Fridge();

    @Belief
    public List<EmotionalBelief> food = new ArrayList<>();

    @Belief
    public List<String> test = new ArrayList<>();

    private Hunger hunger = new Hunger(0.5);

    public double probability = Math.random();

    public double desirability = (Math.random() * 0.3) + 0.7;

    public double approval = (Math.random() * 0.5) + 0.5;

    public double eatHealthyFoodDesirability = getHunger().getHungerValue();

    public double disapproval = (Math.random() * 0.5) + 0.5;

    public double otherDesireSuccess = (Math.random() * 0.3) + 0.7;

    public double otherDesireFailure = (Math.random() * 0.3) + 0.7;

    public double disapprovalApprovalFood = (Math.random() * 0.2) + 0.8;

    public double desirabilityFood = Math.min(getHunger().getHungerValue() + Math.random() + 0.3, 1);

    @AgentCreated
    public void init() {

    }

    @AgentBody
    public void body() {

        agentFeature.adoptPlan(new MetabolismServicePlan()).get();
    }

    @EmotionalPlan({
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER, target = R.STRING, stringValue = "TelemarketerAnna"),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_GROUP, target = R.BOOLEAN, booleanValue = false),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_PLAN, target = R.STRING, stringValue = "advertiseBioFoodStore"),
            @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.FIELD, fieldName = "disapprovalApprovalFood"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldName = "desirabilityFood")
    })
    @Plan
    public void advertiseBioFoodStore(IPlan plan) {

    }

    @EmotionalPlan({
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER, target = R.STRING, stringValue = "TelemarketerAnna"),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_GROUP, target = R.BOOLEAN, booleanValue = false),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_PLAN, target = R.STRING, stringValue = "advertiseLocalFastFood"),
            @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.FIELD, fieldName = "disapprovalApprovalFood"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldName = "desirabilityFood")
    })
    @Plan
    public void advertiseLocalFastFood(IPlan plan) {

    }

    @Belief
    public Hunger getHunger() {
        return hunger;
    }

    @Belief
    public void setHunger(Hunger hunger) {
        this.hunger = hunger;
    }

    @EmotionalGoal({
            @EmotionalParameter(parameter = R.PARAM_OTHER_DESIRE_GOAL_FAILURE, target = R.FIELD, fieldName = "otherDesireFailure"),
            @EmotionalParameter(parameter = R.PARAM_PROBABILITY, target = R.FIELD, fieldName = "probability"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldName = "desirability")
    })
    @Goal(unique = true, deliberation = @Deliberation(cardinalityone = true))
    public static class CampaignAgainstJunkFood {

        public CampaignAgainstJunkFood() {

        }

        @GoalCreationCondition(beliefs = "engine")
        public static CampaignAgainstJunkFood creationCondition(HungryPaulBDI agent) {

            double intensity = agent.engine.getElement("advertiseLocalFastFood", R.PLAN).getEmotion(R.ANGER).getIntensity();

            if (intensity > 0.25) {
                System.err.println("CampaignAgainstJunkFood" + intensity);
                return new CampaignAgainstJunkFood();
            }
            return null;
        }
    }

    @EmotionalGoal({
            @EmotionalParameter(parameter = R.PARAM_OTHER_DESIRE_GOAL_SUCCESS, target = R.FIELD, fieldName = "otherDesireSuccess")
    })
    @Goal(unique = true, deliberation = @Deliberation(cardinalityone = true))
    public static class CommunityVolunteering {

        public CommunityVolunteering() {

        }

        @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.BELIEFCHANGED, second = "hunger")})
        public static CommunityVolunteering creationCondition(HungryPaulBDI agent) {

            double intensity = agent.engine.getElement("EatUnhealthyPlan", R.PLAN).getEmotion(R.SHAME).getIntensity();

            if (intensity > 0.75) {
                return new CommunityVolunteering();
            }
            return null;
        }
    }

    @EmotionalGoal({
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldName = "eatHealthyFoodDesirability")
    })
    @Goal(unique = true, deliberation = @Deliberation(cardinalityone = true))
    public static class EatHealthyFood {

        @GoalParameter
        Hunger hunger;

        public EatHealthyFood(Hunger hunger) {
            this.hunger = hunger;
        }

        @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.BELIEFCHANGED, second = "hunger")})
        public static EatHealthyFood creationCondition(Hunger hunger) {

            if (hunger.getHungerValue() > ((Math.random() * 0.4) + 0.4)) {
                return new EatHealthyFood(hunger);
            }
            return null;
        }
    }

    @EmotionalGoal(value = {
            @EmotionalParameter(parameter = R.PARAM_PROBABILITY, target = R.FIELD, fieldName = "probability"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldName = "desirability")
    })
    @Goal(unique = true, deliberation = @Deliberation(cardinalityone = true))
    public static class FindDate {

        @GoalAPI
        protected IGoal goal;

        public FindDate() {

        }

        @GoalCreationCondition(beliefs = "engine")
        public static FindDate conditionMethod(HungryPaulBDI agent) {

            double intensity = agent.engine.getElement("advertiseBioFoodStore", R.PLAN).getEmotion(R.GRATITUDE).getIntensity();

            if (intensity > 0.75) {

                System.err.println("FindDate" + intensity);

                return new FindDate();
            }
            return null;
        }
    }

    @Goal
    public static class ForgetOldEatenFood {

        @GoalParameter
        private MyFoodEmotionalBelief foodBelief;

        public ForgetOldEatenFood(MyFoodEmotionalBelief belief) {
            this.foodBelief = belief;
        }

        @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.FACTCHANGED, second = "food")})
        public static ForgetOldEatenFood creationCondition(EmotionalBelief belief) {

            if (belief instanceof MyFoodEmotionalBelief) {
                MyFoodEmotionalBelief foodBelief = (MyFoodEmotionalBelief) belief;
                if (foodBelief.isEaten()) {

                    return new ForgetOldEatenFood(foodBelief);
                }
            }

            return null;
        }

        public MyFoodEmotionalBelief getFoodBelief() {
            return foodBelief;
        }
    }
}
