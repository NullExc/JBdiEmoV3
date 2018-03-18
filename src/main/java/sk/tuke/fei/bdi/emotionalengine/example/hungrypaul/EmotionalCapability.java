package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul;

import jadex.bdiv3.annotation.*;
import sk.tuke.fei.bdi.emotionalengine.belief.EmotionalBelief;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalGoal;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;

@Capability
public class EmotionalCapability {

    @Belief
    public boolean capaBelief = false;

    @Belief
    public EmotionalBelief emotionalBelief = new EmotionalBelief("emotionalBelief", null, true, true, 0.8);

    @EmotionalPlan({
            @EmotionalParameter(parameter = R.PARAM_APPROVAL, target = R.DOUBLE, doubleValue = 0.7)
    })
    @Plan(trigger = @Trigger(goals = Translate.class))
    protected void capaPlan() {

      //  System.err.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

    }

    @Goal
    public static class Translate
    {
        @EmotionalGoal({
                @EmotionalParameter(parameter = R.PARAM_DESIRABILITY, target = R.DOUBLE, doubleValue = 0.2)
        })
        public Translate(String eword)
        {

        }

        @GoalCreationCondition(beliefs = "capaBelief")
        public static Translate creationCondition() {
            return new Translate("");
        }
    }

    @EmotionalPlan({
            @EmotionalParameter(parameter = R.PARAM_DISAPPROVAL, target = R.DOUBLE, doubleValue = 0.7)
    })
    @Plan(trigger = @Trigger(factremoveds = "capaBelief"))
    public static class CapaClassPlan {

        @PlanBody
        public void body() {
            System.err.println(" **************** capaclassplan");
        }

    }

    public void setBelief(boolean value) {
        this.capaBelief = value;
    }

    public void updateBelief() {

        System.err.println("Updating beliefs");

        emotionalBelief.updateBelief(true, true, 0.77777);

        //emotionalBelief = new EmotionalBelief("emotionalBelief", null, false, false, 0.4);
    }

}
