package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.IGoal;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 5/25/2017.
 */
@Goal(deliberation = @Deliberation(cardinalityone = true, inhibits = FindDate.class))
public class FindDate {

    @GoalAPI
    protected IGoal goal;

    private int localId;

    @EmotionalGoal(value = {
            @EmotionalParameter(parameter = R.PARAM_PROBABILITY, target = R.FIELD, fieldValue = "probability"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldValue = "desirability")
    })
    public FindDate() {

    }

    public FindDate(int id) {
        localId = id;
    }

    @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.FACTCHANGED, second = "engine")})
    public static FindDate conditionMethod(HungryPaulBDI agent) {

        double intensity = agent.engine.getElement("advertiseBioFoodStore", R.PLAN).getEmotion(R.GRATITUDE).getIntensity();
        //0.75
        if (intensity > 0.4) {
            return new FindDate((int) intensity);
        }

        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        /*if (obj instanceof FindDate) {
            FindDate goal = (FindDate) obj;
            if (goal.localId == this.localId) {
                return true;
            }
        }
        return false;*/
        return true;
    }
}
