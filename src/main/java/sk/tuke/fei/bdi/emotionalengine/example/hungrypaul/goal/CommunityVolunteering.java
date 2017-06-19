package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.model.MProcessableElement;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.commons.SUtil;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 6/17/2017.
 */
@Goal(retry = true, unique = true, deliberation = @Deliberation(cardinalityone = true))
public class CommunityVolunteering {

    private int localId;

    private static boolean active;

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
            if (!active) {
                active = true;
                return new CommunityVolunteering((int) intensity);
            }
        }
        return null;
    }

    @GoalFinished
    public void finished() {
        active = false;
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + HungryPaulBDI.class.hashCode();
        result = prime * result + localId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean ret = false;
        if(obj instanceof CommunityVolunteering)
        {
            CommunityVolunteering other = (CommunityVolunteering) obj;
            ret = localId == other.localId;
        }
        return ret;
    }
}
