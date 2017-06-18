package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.goal.CampaignAgainstJunkFood;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 * Created by PeterZemianek on 5/14/2017.
 */

@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.FIELD, fieldValue = "approval"),
        @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.FIELD, fieldValue = "disapproval")
})
@Plan(trigger = @Trigger(goals = CampaignAgainstJunkFood.class))
public class SprayPaintBillboardPlan {

    @PlanAPI
    IPlan plan;

    private static int step;

    @PlanBody
    public void body() {

        step++;

        plan.waitFor((long) (3000 + (Math.random() * 3000))).get();

        if (Math.random() > 0.5) {

            System.out.println("");
            System.out.println("Paul sprayed over fast food billboard to point out how unhealthy it is. " + step);
            System.out.println("");

        } else {

            System.out.println("");
            System.out.println("Paul wanted to damage fast food billboard but there was a police car so he went back" +
                    "home without taking action " + step);
            System.out.println("");

            throw new PlanFailureException();
            //fail();

        }

    }
}
