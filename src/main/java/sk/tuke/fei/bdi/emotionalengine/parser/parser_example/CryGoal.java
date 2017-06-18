package sk.tuke.fei.bdi.emotionalengine.parser.parser_example;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalAPI;
import jadex.bdiv3.runtime.IGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by Peter on 21.3.2017.
 */

@Goal
public class CryGoal {

    @GoalAPI
    protected IGoal goal;

    public String parameter;

    @EmotionalGoal( value = {
            @EmotionalParameter(parameter = R.PARAM_OTHER_DESIRE_GOAL_SUCCESS, target = R.SIMPLE_DOUBLE, doubleValue = 0.7d),
            @EmotionalParameter(parameter = R.PARAM_OTHER_DESIRE_GOAL_FAILURE, target = R.SIMPLE_DOUBLE, doubleValue = 0.8d),
            @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.SIMPLE_DOUBLE, doubleValue = 0.9d)}
    )
    public CryGoal(String parameter) {
        this.parameter = parameter;
    }

    public CryGoal() {

    }
}
