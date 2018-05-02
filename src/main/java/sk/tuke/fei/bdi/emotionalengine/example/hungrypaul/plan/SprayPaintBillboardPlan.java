package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.CampaignAgainstJunkFood;
import sk.tuke.fei.bdi.emotionalengine.res.R;

@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.FIELD, fieldName = "approval"),
        @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.FIELD, fieldName = "disapproval")
})
@Plan(trigger = @Trigger(goals = CampaignAgainstJunkFood.class))
public class SprayPaintBillboardPlan {

    @PlanAPI
    IPlan plan;

    @PlanBody
    public void body() {

        plan.waitFor((long) (3000 + (Math.random() * 3000))).get();

        if (Math.random() > 0.5) {

            System.out.println("");
            System.out.println("Paul sprayed over fast food billboard to point out how unhealthy it is. ");
            System.out.println("");

        } else {

            System.out.println("");
            System.out.println("Paul wanted to damage fast food billboard but there was a police car so he went back" +
                    "home without taking action ");
            System.out.println("");

            throw new PlanFailureException();
        }
    }
}
