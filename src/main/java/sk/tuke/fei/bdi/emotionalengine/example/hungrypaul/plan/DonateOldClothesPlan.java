package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI.CommunityVolunteering;
import sk.tuke.fei.bdi.emotionalengine.res.R;

@EmotionalPlan({
        @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.FIELD, fieldName = "approval")
})
@Plan(trigger = @Trigger(goals = CommunityVolunteering.class))
public class DonateOldClothesPlan {

    @PlanAPI
    protected IPlan plan;

    @PlanBody
    public void body() {

        plan.waitFor((long) (3000 + (Math.random() * 3000))).get();

        if (Math.random() > 0.5) {

            System.out.println("");
            System.out.println("Paul donated old clothes to the local community help center");
            System.out.println("");

        } else {

            System.out.println("");
            System.out.println("Paul didn't have any clothes to donate to the local community help center");
            System.out.println("");

            throw new PlanFailureException();

        }

    }
}
