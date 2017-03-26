package sk.tuke.fei.bdi.emotionalengine.BDIParser.parser_example;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalAPI;
import jadex.bdiv3.runtime.IBeliefListener;
import jadex.bdiv3.runtime.IGoal;
import jadex.bdiv3.runtime.impl.GoalInfo;
import jadex.rules.eca.ChangeInfo;

/**
 * Created by Peter on 21.3.2017.
 */
@Goal
public class CryGoal {

    @GoalAPI
    protected IGoal goal;

    public CryGoal() {

    }
}
