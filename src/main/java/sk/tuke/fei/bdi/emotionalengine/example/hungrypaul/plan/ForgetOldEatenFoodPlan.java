package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.ForgetOldEatenFood;
import sk.tuke.fei.bdi.emotionalengine.res.R;


@Plan(trigger = @Trigger(goals = ForgetOldEatenFood.class))
public class ForgetOldEatenFoodPlan {

    @PlanAPI
    IPlan plan;

    @PlanReason
    ForgetOldEatenFood goal;

    @PlanCapability
    HungryPaulBDI agent;

    private int delayBeforeForgettingMillis = 20000;

    @PlanBody
    public void body() {

        plan.waitFor(delayBeforeForgettingMillis).get();

        EmotionalBelief food = goal.getFoodBelief();

        // Get the food object from belief base to check if it wasn't eaten already
        boolean foodFact = agent.food.contains(food);

        // If it wasn't eaten, remove food from belief base
        if (foodFact) {

            agent.food.remove(food);

            agent.engine.removeElement(food.getName(), R.BELIEF_SET_BELIEF);

        }

    }
}
