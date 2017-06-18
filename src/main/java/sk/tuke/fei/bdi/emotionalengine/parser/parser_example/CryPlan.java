package sk.tuke.fei.bdi.emotionalengine.parser.parser_example;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by Peter on 21.3.2017.
 */
@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.SIMPLE_DOUBLE, doubleValue = 0.7d),
        @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.SIMPLE_DOUBLE, doubleValue = 0.8d)}
)
@Plan(trigger=@Trigger(goals=CryGoal.class))
public class CryPlan {

    @PlanAPI
    protected IPlan plan;

    @PlanReason
    protected CryGoal goal;


    public CryPlan() {

    }

    @PlanBody
    public void crying() {
        System.out.println("Start CryPlan ....." + plan.getReason());
        plan.waitFor(12000).get();
        System.out.println("End CryPlan ....." + plan.getReason());


    }
}