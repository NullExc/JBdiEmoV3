package sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.plan;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.runtime.IPlan;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.HungryPaulBDI;
import sk.tuke.fei.bdi.emotionalengine.example.hungrypaul.belief.Hunger;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;

/**
 * Created by PeterZemianek on 5/14/2017.
 */
@Plan
public class MetabolismServicePlan {

    @PlanAPI
    protected IPlan plan;

    @PlanCapability
    protected HungryPaulBDI paulBDI;

    private String[] hungerLevel;
    private String lastHungerLevel;

    public MetabolismServicePlan() {
        hungerLevel = new String[5];

        hungerLevel[0] = "Not hungry";         // <0.0, 0.2)
        hungerLevel[1] = "Slightly hungry";    // <0.2, 0.4)
        hungerLevel[2] = "Hungry";             // <0.4, 0.6)
        hungerLevel[3] = "Very hungry";        // <0.6, 0.8)
        hungerLevel[4] = "Starving";           // <0.8, 1.0)

        lastHungerLevel = hungerLevel[0];
    }

    @PlanBody
    public void body() {

        System.err.println("Metabolism plan starts");

        while (true) {

            plan.waitFor(1500).get();

            double hunger = paulBDI.getHunger().getHungerValue();

            if (hunger + 0.02 <= 1) {
                hunger = hunger + 0.02;

             //   System.out.println("MetabolismServicePlan : " + hunger);

                paulBDI.setHunger(new Hunger(hunger));
                //hungerBelief = new Hunger(hunger);
                //model.getCapability().getBelief("hunger").setValue(agentAccess, new Hunger(hunger));
            }


            String selectedHungerLevel;

            if (hunger < 0.2) {

                selectedHungerLevel = hungerLevel[0];

            } else if (hunger >= 0.2 && hunger < 0.4) {

                selectedHungerLevel = hungerLevel[1];

            } else if (hunger >= 0.4 && hunger < 0.6) {

                selectedHungerLevel = hungerLevel[2];

            } else if (hunger >= 0.6 && hunger < 0.8) {

                selectedHungerLevel = hungerLevel[3];

            } else {

                selectedHungerLevel = hungerLevel[4];

            }

//                EmotionalBelief hungerEmotions = (EmotionalBelief) getBeliefbase().getBelief("hunger_emotions").getFact();
//                if (hunger < 0.4) {
//                    hungerEmotions.updateBelief(true, true, (1 - hunger) * 0.1);
//                } else {
//                    hungerEmotions.updateBelief(true, false, hunger * 0.1);
//                }

            if (!selectedHungerLevel.equals(lastHungerLevel)) {

                System.out.println("John is " + selectedHungerLevel + " (" + MyMath.roundDouble(hunger, 2) + ")");

                lastHungerLevel = selectedHungerLevel;

            }
        }


    }
}
