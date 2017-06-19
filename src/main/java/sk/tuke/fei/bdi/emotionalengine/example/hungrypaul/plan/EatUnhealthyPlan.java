package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Fridge;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.MyFoodEmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal.EatHealthyFood;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.FIELD, fieldValue = "disapproval")
})
@Plan(trigger = @Trigger(goals = EatHealthyFood.class))
public class EatUnhealthyPlan {

    @PlanAPI
    protected IPlan plan;

    @PlanCapability
    HungryPaulBDI agent;

    @PlanBody
    public void body() {

        Fridge fridge = agent.fridge; //(Fridge) getBeliefbase().getBelief("fridge").getFact();

        MyFoodEmotionalBelief food = fridge.getFood("food", false);

        agent.food.add(food); //getBeliefbase().getBeliefSet("food").addFact(food);

        plan.waitFor((int) (1000 + Math.random() * 1000)).get();

        food.setEaten(true);

        System.out.println("");
        System.out.println("Paul is eating unhealthy food: " + food.getName() + ", nom nom...");
        System.out.println("");

        double adjustedAttractionIntensity = food.getAttractionIntensity() + (Math.random() - 0.3);

        if (adjustedAttractionIntensity > 1) {
            adjustedAttractionIntensity = 1;
        }

        food.updateBelief(true, null, adjustedAttractionIntensity);

        int index = agent.food.indexOf(food);

        agent.food.set(index, food);

        Hunger hungerBelief = agent.getHunger();//(Hunger) getBeliefbase().getBelief("hunger").getFact();
        double hunger = hungerBelief.getHungerValue();
        hunger = Math.min(1, Math.max(0, (hunger - food.getNutritionValue())));
        agent.setHunger(new Hunger(hunger)); //getBeliefbase().getBelief("hunger").setFact(new Hunger(hunger));

        throw new PlanFailureException();
        //fail();


    }

}
