package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.model.BDIModel;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Fridge;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal.*;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan.*;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@Agent
@JBDIEmoAgent(guiEnabled = true, others = "TelemarketerAnna")
@Plans({@Plan(body = @Body(InitializeEmotionalEnginePlan.class)),
        @Plan(body = @Body(CallAnnaOutPlan.class)),
        @Plan(body = @Body(MetabolismServicePlan.class)),
        @Plan(body = @Body(EatHealthyPlan.class)),
        @Plan(body = @Body(EatUnhealthyPlan.class)),
        @Plan(body = @Body(DonateOldClothesPlan.class)),
        @Plan(body = @Body(SprayPaintBillboardPlan.class)),
        @Plan(body = @Body(ForgetOldEatenFoodPlan.class))
})
@Goals({
        @Goal(clazz = CampaignAgainstJunkFood.class),
        @Goal(clazz = CommunityVolunteering.class),
        @Goal(clazz = EatHealthyFood.class),
        @Goal(clazz = FindDate.class),
        @Goal(clazz = ForgetOldEatenFood.class)
})
@RequiredServices({
        @RequiredService(name = R.COMPONENT_SERVICE, type = IComponentManagementService.class, binding = @Binding(scope = RequiredServiceInfo.SCOPE_GLOBAL))
})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
})
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

    private Hunger hunger = new Hunger(0.5);

    @Belief
    private EmotionalBelief testBelief = new EmotionalBelief("testBelief", null, true, true, 0.9);

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

        agentFeature.adoptPlan(new InitializeEmotionalEnginePlan(this, engine)).get();

        testBelief = new EmotionalBelief("testBelief", null, true, true, 0.99);

        agentFeature.adoptPlan(new MetabolismServicePlan()).get();
       // agentFeature.adoptPlan("metabolismServicePlan").get();
    }

    @EmotionalPlan({
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER, target = R.SIMPLE_STRING, stringValue = "TelemarketerAnna"),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_GROUP, target = R.SIMPLE_BOOLEAN, booleanValue = false),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_PLAN, target = R.SIMPLE_STRING, stringValue = "advertiseBioFoodStore"),
            @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.FIELD, fieldValue = "disapprovalApprovalFood"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldValue = "desirabilityFood")
    })
    @Plan
    public void advertiseBioFoodStore(IPlan plan) {

    }

    @EmotionalPlan({
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER, target = R.SIMPLE_STRING, stringValue = "TelemarketerAnna"),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_GROUP, target = R.SIMPLE_BOOLEAN, booleanValue = false),
            @EmotionalParameter(parameter = R.PARAM_EMOTIONAL_OTHER_PLAN, target = R.SIMPLE_STRING, stringValue = "advertiseLocalFastFood"),
            @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.FIELD, fieldValue = "disapprovalApprovalFood"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldValue = "desirabilityFood")
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
}
