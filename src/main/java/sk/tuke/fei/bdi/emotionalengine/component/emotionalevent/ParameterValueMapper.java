package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent;


import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.EmoGoal;
import sk.tuke.fei.bdi.emotionalengine.BDIParser.Annotations.EmoPlan;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Peter on 23.3.2017.
 */
public class ParameterValueMapper {

    public Map<String, Double> getUserParameterValues(Annotation annotation) {

        Map<String, Double> userParameters = new HashMap<String, Double>();

        if (annotation instanceof EmoPlan) {

            EmoPlan emoPlan = (EmoPlan) annotation;
            if (emoPlan.approval() > 0) userParameters.put(R.PARAM_APPROVAL, emoPlan.approval());
            if (emoPlan.disapproval() > 0) userParameters.put(R.PARAM_DISAPPROVAL, emoPlan.disapproval());
            if (emoPlan.desirability() > 0) userParameters.put(R.PARAM_DESIRABILITY, emoPlan.desirability());

        } else if (annotation instanceof EmoGoal) {

            EmoGoal emoGoal = (EmoGoal) annotation;
            if (emoGoal.probability() > 0) userParameters.put(R.PARAM_PROBABILITY, emoGoal.probability());
            if (emoGoal.desirability() > 0) userParameters.put(R.PARAM_DESIRABILITY, emoGoal.desirability());
            if (emoGoal.otherDesireGoalSucces() > 0) userParameters.put(R.PARAM_OTHER_DESIRE_GOAL_SUCCESS, emoGoal.otherDesireGoalSucces());
            if (emoGoal.otherDesireGoalFailure() > 0) userParameters.put(R.PARAM_OTHER_DESIRE_GOAL_FAILURE, emoGoal.otherDesireGoalFailure());
        }

        return userParameters;
    }



    public Map<Integer, Double> getSystemParameterValues(Element element) {

        // Create user parameter value map for emotional event
        Map<Integer, Double> systemParameters = new HashMap<Integer, Double>();

        // Check if element is valid
        if (element != null) {

            // Check if element instance contains any system parameter
            Set<Emotion> elementInstanceEmotions = element.getEmotions();

            if (elementInstanceEmotions != null && !elementInstanceEmotions.isEmpty()) {
                for (Emotion emotion : element.getEmotions()) {

                    // If element instance contains valid system parameter get its value
                    if (emotion.getIntensity() != 0) {

                        systemParameters.put(emotion.getEmotionId(), emotion.getIntensity());

                    }
                }
            }

        }

        return  systemParameters;
    }

}
