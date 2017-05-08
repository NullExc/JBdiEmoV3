package sk.tuke.fei.bdi.emotionalengine.parser.parser_example;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;

/**
 * Created by Peter on 21.3.2017.
 */
@Plan
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


        throw new PlanFailureException();

    }
}