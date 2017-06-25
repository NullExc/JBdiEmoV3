package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal.FindDate;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.SIMPLE_DOUBLE, doubleValue = 0.7d),
        @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.SIMPLE_DOUBLE, doubleValue = 0.8d)}
)
@Plan(trigger = @Trigger(goals = FindDate.class))
public class CallAnnaOutPlan {

    @PlanAPI
    IPlan plan;

    private static int step;

    @PlanBody
    public void body() {

        step++;

        System.err.println("********* CallAnnaOutPlan Started ! " + step);

        plan.waitFor((long) (3000 + (Math.random() * 3000))).get();

        if (Math.random() > 0.5) {

            System.out.println("");
            System.out.println("Paul called anna out and she agreed to go for a date");
            System.out.println("");

            System.err.println("********* CallAnnaOutPlan Finished ! " + step);

        } else {

            System.out.println("");
            System.out.println("Paul called anna out but she rejected to go out for a date");
            System.out.println("");

            System.err.println("********* CallAnnaOutPlan Finished ! " + step);

            throw new PlanFailureException("Anna rejected to go out with Paul");

        }
    }
}
