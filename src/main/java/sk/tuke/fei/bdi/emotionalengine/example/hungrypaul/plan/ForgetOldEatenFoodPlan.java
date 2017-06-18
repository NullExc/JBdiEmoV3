package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal.ForgetOldEatenFood;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@Plan(trigger = @Trigger(goals = ForgetOldEatenFood.class))
public class ForgetOldEatenFoodPlan {

    @PlanAPI
    IPlan plan;

    @PlanReason
    ForgetOldEatenFood goal;

    @PlanCapability
    HungryPaulBDI agent;

    private int delayBeforeForgettingMillis = 2000;

    @PlanBody
    public void body() {

        // Default value 20 seconds
        int waitForTimeout = 20000;

        try {
            waitForTimeout = delayBeforeForgettingMillis;
        } catch (Exception e) {
//          Try catch used because it can't be tested otherwise because Jadex bug where you
//          can't get parameter array of plan but you can get parameter by name
        }

        plan.waitFor(waitForTimeout).get();

        EmotionalBelief food = goal.getFoodBelief(); //(EmotionalBelief) getParameter("food").getValue();

        // Get the food object from belief base to check if it wasn't eaten already
        boolean foodFact = agent.food.contains(food);//(EmotionalBelief) getBeliefbase().getBeliefSet("food").getFact(food);

        // If it wasn't eaten, remove food from belief base
        //if (foodFact != null) {
        if (foodFact) {

//            System.out.println("");
//            System.out.println("Forget about food I ate earlier: " + food.getName() + ", wasn't important anymore...");
//            System.out.println("");

            //getBeliefbase().getBeliefSet("food").removeFact(food);
            agent.food.remove(food);
        }

    }
}
