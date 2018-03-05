package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.FIELD, fieldName = "approval")
})
@Plan(trigger = @Trigger(goals = HungryPaulBDI.EatHealthyFood.class))
public class EatHealthyPlan {

    @PlanAPI
    protected IPlan plan;

    @PlanCapability
    HungryPaulBDI agent;

    @PlanBody
    public void body() {

        plan.waitFor(5000).get();

        Hunger hungerBelief = agent.getHunger();//(Hunger) getBeliefbase().getBelief("hunger").getFact();
        double hunger = hungerBelief.getHungerValue();
        hunger = Math.min(1, Math.max(0, (hunger - 0.2)));

        EmotionalBelief belief = new EmotionalBelief("Apple" + Math.random(), "food", true, true, Math.random());

        agent.food.add(belief);

        plan.waitFor(5000).get();

        agent.food.remove(belief);

        agent.setHunger(new Hunger(hunger)); //getBeliefbase().getBelief("hunger").setFact(new Hunger(hunger));

    }

}
