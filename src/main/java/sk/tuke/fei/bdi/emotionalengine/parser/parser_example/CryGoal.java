package sk.tuke.fei.bdi.emotionalengine.parser.parser_example;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalAPI;
import jadex.bdiv3.runtime.IGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;

/**
 * Created by Peter on 21.3.2017.
 */
@EmotionalGoal
@Goal
public class CryGoal {

    @GoalAPI
    protected IGoal goal;

    public String parameter;

    public CryGoal(String parameter) {
        this.parameter = parameter;
    }
}
