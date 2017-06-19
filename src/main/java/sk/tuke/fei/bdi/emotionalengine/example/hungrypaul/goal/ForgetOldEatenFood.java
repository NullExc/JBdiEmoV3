package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalCreationCondition;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.RawEvent;
import jadex.bdiv3.runtime.ChangeEvent;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.MyFoodEmotionalBelief;

/**
 * Created by PeterZemianek on 6/17/2017.
 */
@Goal
public class ForgetOldEatenFood {

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
