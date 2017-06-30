package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ChangeEvent;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 6/17/2017.
 */
@Goal(unique = true, deliberation = @Deliberation(cardinalityone = true))
public class CampaignAgainstJunkFood {

    private int localId;

    private static boolean active;

    @EmotionalGoal({
            @EmotionalParameter(parameter = R.PARAM_OTHER_DESIRE_GOAL_FAILURE, target = R.FIELD, fieldValue = "otherDesireFailure"),
            @EmotionalParameter(parameter = R.PARAM_PROBABILITY, target = R.FIELD, fieldValue = "probability"),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.FIELD, fieldValue = "desirability")
    })
    public CampaignAgainstJunkFood() {

    }

    public CampaignAgainstJunkFood(int id) {
        this.localId = id;
    }

    @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.FACTCHANGED, second = "engine")})
    public static CampaignAgainstJunkFood creationCondition(HungryPaulBDI agent) {

        double intensity = agent.engine.getElement("advertiseLocalFastFood", R.PLAN).getEmotion(R.ANGER).getIntensity();

        if (intensity > 0.25) {

            if (!active) {
                active = true;
                return new CampaignAgainstJunkFood((int) intensity);
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
        return localId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CampaignAgainstJunkFood) {
            CampaignAgainstJunkFood goal = (CampaignAgainstJunkFood) obj;
            if (goal.localId == this.localId) {
                return true;
            }
        }
        return false;
    }
}
