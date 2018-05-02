package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.FindDate;
import sk.tuke.fei.bdi.emotionalengine.res.R;

@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.DOUBLE, doubleValue = 0.7d),
        @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.DOUBLE, doubleValue = 0.8d)}
)
@Plan(trigger = @Trigger(goals = FindDate.class))
public class CallAnnaOutPlan {

    @PlanAPI
    IPlan plan;

    @PlanBody
    public void body() {

        System.err.println("********* CallAnnaOutPlan Started ! ");

        plan.waitFor((long) (3000 + (Math.random() * 3000))).get();

        if (Math.random() > 0.5) {

            System.out.println("");
            System.out.println("Paul called anna out and she agreed to go for a date");
            System.out.println("");

        } else {

            System.out.println("");
            System.out.println("Paul called anna out but she rejected to go out for a date");
            System.out.println("");

            throw new PlanFailureException("Anna rejected to go out with Paul");

        }
    }
}
