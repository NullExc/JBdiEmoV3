package sk.tuke.fei.bdi.emotionalengine.example.telemarketeranna;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.nonfunctional.annotation.NameValue;
import jadex.micro.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.EmotionalCapability;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan.EatHealthyPlan;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan.MetabolismServicePlan;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.JBDIEmoAgent;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.CommunicationService;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;

@Plans({@Plan(body = @Body(InitializeEmotionalEnginePlan.class))})
@ProvidedServices({
        @ProvidedService(name = R.MESSAGE_SERVICE, type = ICommunicationService.class, implementation = @Implementation(CommunicationService.class))
})
@BDIConfigurations({
        @BDIConfiguration(name = "initial", initialplans = {@NameValue(name = "initial",clazz = InitializeEmotionalEnginePlan.class)})
})
@Agent
@JBDIEmoAgent(guiEnabled = true, others = "HungryPaul")
public class TelemarketerAnnaBDI {

    @AgentFeature
    protected IBDIAgentFeature agentFeature;

    @Belief
    public Engine engine = new Engine();

    @Capability
    protected EmotionalCapability capability = new EmotionalCapability();

    @AgentBody
    public void body() {


    }}
