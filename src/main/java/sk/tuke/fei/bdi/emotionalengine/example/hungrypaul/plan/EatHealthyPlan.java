package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Fridge;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.MyFoodEmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.EatHealthyFood;
import sk.tuke.fei.bdi.emotionalengine.res.R;

@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.FIELD, fieldName = "approval")
})
@Plan(trigger = @Trigger(goals = EatHealthyFood.class), priority = 2)
public class EatHealthyPlan {

    @PlanAPI
    protected IPlan plan;

    @PlanCapability
    HungryPaulBDI agent;

    @PlanBody
    public void body() {

        //waitFor(10000);

        Fridge fridge = agent.fridge;

        MyFoodEmotionalBelief food = fridge.getFood("food", true);

        if (Math.random() > 0.3) {

            agent.food.add(food);

            plan.waitFor((int) (1000 + Math.random() * 1000)).get();

            food.setEaten(true);

            System.out.println("");
            System.out.println("Paul is eating healthy food: " + food.getName() + ", nom nom...");
            System.out.println("");

            double adjustedAttractionIntensity = food.getAttractionIntensity() + (Math.random() - 0.3);

            if (adjustedAttractionIntensity > 1) {
                adjustedAttractionIntensity = 1;
            }

            food.updateBelief(true, null, adjustedAttractionIntensity);

            int index = agent.food.indexOf(food);

            agent.food.set(index, food);

            Hunger hungerBelief = agent.getHunger();
            double hunger = hungerBelief.getHungerValue();
            hunger = Math.min(1, Math.max(0, (hunger - food.getNutritionValue())));
            agent.setHunger(new Hunger(hunger));

        } else {


            System.out.println("");
            System.out.println("Paul wants to eat something healthy.");
            System.out.println("Paul didn't find anything healthy to eat...");
            System.out.println("");

            new PlanFailureException();
        }

    }

}
