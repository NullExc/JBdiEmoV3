package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal;

import jadex.bdiv3.annotation.Deliberation;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalCreationCondition;
import jadex.bdiv3.annotation.RawEvent;
import jadex.bdiv3.runtime.ChangeEvent;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 6/17/2017.
 */
@Goal(unique = true, deliberation = @Deliberation(cardinalityone = true))
public class CommunityVolunteering {

    private int localId;

    public CommunityVolunteering(int id) {
        localId = id;
    }

    @EmotionalGoal({
            @EmotionalParameter(parameter = R.PARAM_OTHER_DESIRE_GOAL_SUCCESS, target = R.FIELD, fieldValue = "otherDesireSuccess")
    })
    public CommunityVolunteering() {

    }

    @GoalCreationCondition(rawevents = {@RawEvent(value = ChangeEvent.FACTCHANGED, second = "engine")})
    public static CommunityVolunteering creationCondition(HungryPaulBDI agent) {

        double intensity = agent.engine.getElement("EatUnhealthyPlan", R.PLAN).getEmotion(R.SHAME).getIntensity();

        if (intensity > 0.75) {
            return new CommunityVolunteering((int) intensity);
        }

        return null;
    }

    @Override
    public int hashCode() {
        return localId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommunityVolunteering) {
            CommunityVolunteering goal = (CommunityVolunteering) obj;
            if (goal.localId == this.localId) {
                return true;
            }
        }
        return false;
    }
}
