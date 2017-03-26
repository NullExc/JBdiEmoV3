package sk.tuke.fei.bdi.emotionalengine.BDIParser.parser_example;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.IBeliefListener;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.IResultListener;
import jadex.rules.eca.ChangeInfo;

/**
 * Created by Peter on 21.3.2017.
 */
@Plan
public class CryPlan {

    @PlanAPI
    protected IPlan plan;

    public CryPlan() {

    }

    @PlanBody
    public void crying() {

    }
}