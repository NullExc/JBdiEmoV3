package sk.tuke.fei.bdi.emotionalengine.component.emotion;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.calculators.PolynomialCalculator;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.goal.*;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.element.GoalCreatedChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.element.GoalFinishedChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.result.ResultFailureChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.result.ResultNullChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.result.ResultSuccessChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.system.SystemFearChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.system.SystemHopeChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.user.*;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomáš Herich
 */

public class GoalEmotionFactory implements EmotionFactory {

    public Set<Emotion> getPossibleEmotions() {
        // Create possible emotions set
        Set<Emotion> possibleEmotions = new HashSet<Emotion>();

        // Add emotions
        possibleEmotions.add(getEmotion(R.PLEASED));
        possibleEmotions.add(getEmotion(R.DISPLEASED));

        possibleEmotions.add(getEmotion(R.HOPE));
        possibleEmotions.add(getEmotion(R.FEAR));

        possibleEmotions.add(getEmotion(R.JOY));
        possibleEmotions.add(getEmotion(R.DISTRESS));

        possibleEmotions.add(getEmotion(R.RELIEF));
        possibleEmotions.add(getEmotion(R.FEAR_CONFIRMED));

        possibleEmotions.add(getEmotion(R.SATISFACTION));
        possibleEmotions.add(getEmotion(R.DISAPPOINTMENT));

        possibleEmotions.add(getEmotion(R.HAPPY_FOR));
        possibleEmotions.add(getEmotion(R.RESENTMENT));

        possibleEmotions.add(getEmotion(R.GLOATING));
        possibleEmotions.add(getEmotion(R.PITY));

        return possibleEmotions;
    }

    @Override
    public Emotion getEmotion(int emotionId) {

        Emotion emotion = null;

        if (emotionId == R.PLEASED) {

            // Basic emotion info
            emotion = new Pleased();
            emotion.setEmotionId(R.PLEASED);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.PLEASED));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_ORANGE);
            emotion.setIntensity(0);

            // Event checkers (decide if emotion intensity calculation will be triggered)
            emotion.addEmotionalEventChecker(new GoalFinishedChecker());

            // Value parameter calculators (not needed in every emotions)
            emotion.addSystemParameterValueCalculator(R.SATISFACTION, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.RELIEF, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.JOY, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.HOPE, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.HAPPY_FOR, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.GLOATING, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.DISPLEASED) {

            emotion = new Displeased();
            emotion.setEmotionId(R.DISPLEASED);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.DISPLEASED));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_LIGHT_BLUE);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());

            emotion.addSystemParameterValueCalculator(R.FEAR, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.FEAR_CONFIRMED, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.DISAPPOINTMENT, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.DISTRESS, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.RESENTMENT, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.PITY, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.HOPE) {

            emotion = new Hope();
            emotion.setEmotionId(R.HOPE);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.HOPE));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_PINK);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalCreatedChecker());
            emotion.addEmotionalEventChecker(new ResultNullChecker());
            emotion.addEmotionalEventChecker(new UserProbabilityChecker());
            emotion.addEmotionalEventChecker(new UserDesirabilityChecker());

            emotion.addUserParameterValueCalculator(R.PARAM_DESIRABILITY, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.FEAR) {

            emotion = new Fear();
            emotion.setEmotionId(R.FEAR);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.FEAR));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_GREEN);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalCreatedChecker());
            emotion.addEmotionalEventChecker(new ResultNullChecker());
            emotion.addEmotionalEventChecker(new UserProbabilityChecker());
            emotion.addEmotionalEventChecker(new UserDesirabilityChecker());

            emotion.addUserParameterValueCalculator(R.PARAM_DESIRABILITY, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.JOY) {

            emotion = new Joy();
            emotion.setEmotionId(R.JOY);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.JOY));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_RED);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultSuccessChecker());
            emotion.addEmotionalEventChecker(new UserDesirabilityChecker());

            emotion.addUserParameterValueCalculator(R.PARAM_DESIRABILITY, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.DISTRESS) {

            emotion = new Distress();
            emotion.setEmotionId(R.DISTRESS);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.DISTRESS));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_BLUE);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultFailureChecker());
            emotion.addEmotionalEventChecker(new UserDesirabilityChecker());

            emotion.addUserParameterValueCalculator(R.PARAM_DESIRABILITY, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.RELIEF) {

            emotion = new Relief();
            emotion.setEmotionId(R.RELIEF);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.RELIEF));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_YELLOW);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultSuccessChecker());
            emotion.addEmotionalEventChecker(new SystemFearChecker());

        }

        if (emotionId == R.FEAR_CONFIRMED) {

            emotion = new FearConfirmed();
            emotion.setEmotionId(R.FEAR_CONFIRMED);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.FEAR_CONFIRMED));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_TEAL);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultFailureChecker());
            emotion.addEmotionalEventChecker(new SystemFearChecker());

        }

        if (emotionId == R.SATISFACTION) {

            emotion = new Satisfaction();
            emotion.setEmotionId(R.SATISFACTION);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.SATISFACTION));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_MAGENTA);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultSuccessChecker());
            emotion.addEmotionalEventChecker(new SystemHopeChecker());

        }

        if (emotionId == R.DISAPPOINTMENT) {

            emotion = new Satisfaction();
            emotion.setEmotionId(R.DISAPPOINTMENT);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.DISAPPOINTMENT));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_PURPLE);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultFailureChecker());
            emotion.addEmotionalEventChecker(new SystemHopeChecker());

        }

        if (emotionId == R.HAPPY_FOR) {

            emotion = new HappyFor();
            emotion.setEmotionId(R.HAPPY_FOR);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.HAPPY_FOR));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_SAND);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultSuccessChecker());
            emotion.addEmotionalEventChecker(new UserOtherDesireGoalSuccess());

            emotion.addUserParameterValueCalculator(R.PARAM_OTHER_DESIRE_GOAL_SUCCESS, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.RESENTMENT) {

            emotion = new Resentment();
            emotion.setEmotionId(R.RESENTMENT);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.RESENTMENT));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_GREY);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultFailureChecker());
            emotion.addEmotionalEventChecker(new UserOtherDesireGoalFailure());

            emotion.addUserParameterValueCalculator(R.PARAM_OTHER_DESIRE_GOAL_FAILURE, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.GLOATING) {

            emotion = new Gloating();
            emotion.setEmotionId(R.GLOATING);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.GLOATING));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_DARK_SAND);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultSuccessChecker());
            emotion.addEmotionalEventChecker(new UserOtherDesireGoalFailure());

            emotion.addUserParameterValueCalculator(R.PARAM_OTHER_DESIRE_GOAL_FAILURE, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.PITY) {

            emotion = new Pity();
            emotion.setEmotionId(R.PITY);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.PITY));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_DARK_GREY);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new GoalFinishedChecker());
            emotion.addEmotionalEventChecker(new ResultFailureChecker());
            emotion.addEmotionalEventChecker(new UserOtherDesireGoalSuccess());

            emotion.addUserParameterValueCalculator(R.PARAM_OTHER_DESIRE_GOAL_SUCCESS, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        return emotion;
    }
}
