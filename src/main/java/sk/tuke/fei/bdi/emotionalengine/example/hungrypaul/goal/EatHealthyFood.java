package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ChangeEvent;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 6/17/2017.
 */
@Goal(unique = true, deliberation = @Deliberation(cardinalityone = true))
public class EatHealthyFood {

    @GoalParameter
    Hunger hunger;

    private int id;

    private static boolean active;

    @EmotionalGoal({
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldValue = "eatHealthyFoodDesirability")
    })
    public EatHealthyFood() {

    }

    public EatHealthyFood(Hunger hunger) {
        this.hunger = hunger;
        id = (int) hunger.getHungerValue();
    }

    @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.BELIEFCHANGED, second = "hunger")})
    public static EatHealthyFood creationCondition(Hunger hunger) {

        if (hunger.getHungerValue() > ((Math.random() * 0.4) + 0.4)) {
            if (!active) {
                active = true;
                return new EatHealthyFood(hunger);
            }
        }
        return null;
    }

    @GoalFinished
    public void finished() {
        active = false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EatHealthyFood) {
            EatHealthyFood goal = (EatHealthyFood) obj;
            if (goal.id == this.id) {
                return true;
            }
        }
        return false;
    }

}
