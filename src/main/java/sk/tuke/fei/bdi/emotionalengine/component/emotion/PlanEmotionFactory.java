package sk.tuke.fei.bdi.emotionalengine.component.emotion;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.calculators.PolynomialCalculator;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.plan.*;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.element.PlanCreatedChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.element.PlanFinishedChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.operator.Not;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.operator.Or;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.result.ResultNullChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.system.*;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.user.*;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomáš Herich
 */

public class PlanEmotionFactory implements EmotionFactory {

    @Override
    public Set<Emotion> getPossibleEmotions() {

        // Create possible emotions set
        Set<Emotion> possibleEmotions = new HashSet<Emotion>();

        // Add emotions
        possibleEmotions.add(getEmotion(R.APPROVING));
        possibleEmotions.add(getEmotion(R.DISAPPROVING));

        possibleEmotions.add(getEmotion(R.PRIDE));
        possibleEmotions.add(getEmotion(R.SHAME));

        possibleEmotions.add(getEmotion(R.GRATIFICATION));
        possibleEmotions.add(getEmotion(R.REMORSE));

        possibleEmotions.add(getEmotion(R.ADMIRATION));
        possibleEmotions.add(getEmotion(R.REPROACH));

        possibleEmotions.add(getEmotion(R.GRATITUDE));
        possibleEmotions.add(getEmotion(R.ANGER));

        return possibleEmotions;
    }

    @Override
    public Emotion getEmotion(int emotionId) {

        Emotion emotion = null;

        if (emotionId == R.APPROVING) {

            // Basic emotion info
            emotion = new Approving();
            emotion.setEmotionId(R.APPROVING);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.APPROVING));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_ORANGE);
            emotion.setIntensity(0);

            // Event checkers (decide if emotion intensity calculation will be triggered)
            emotion.addEmotionalEventChecker(new Or(new PlanCreatedChecker(), new PlanFinishedChecker()));

            // Value parameter calculators (not needed in every emotions)
            emotion.addSystemParameterValueCalculator(R.PRIDE, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.GRATIFICATION, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.ADMIRATION, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.GRATITUDE, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.DISAPPROVING) {

            emotion = new Disapproving();
            emotion.setEmotionId(R.DISAPPROVING);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.DISAPPROVING));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_LIGHT_BLUE);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new Or(new PlanCreatedChecker(), new PlanFinishedChecker()));

            emotion.addSystemParameterValueCalculator(R.SHAME, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.REMORSE, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.REPROACH, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.ANGER, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.PRIDE) {

            emotion = new Pride();
            emotion.setEmotionId(R.PRIDE);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.PRIDE));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_RED);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanCreatedChecker());
            emotion.addEmotionalEventChecker(new ResultNullChecker());
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOther()));
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOtherPlan()));
            emotion.addEmotionalEventChecker(new Or(new UserApprovalChecker(), new UserDisapprovalChecker()));

            emotion.addUserParameterValueCalculator(R.PARAM_APPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addUserParameterValueCalculator(R.PARAM_DISAPPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.SHAME) {

            emotion = new Shame();
            emotion.setEmotionId(R.SHAME);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.SHAME));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_BLUE);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanCreatedChecker());
            emotion.addEmotionalEventChecker(new ResultNullChecker());
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOther()));
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOtherPlan()));
            emotion.addEmotionalEventChecker(new Or(new UserApprovalChecker(), new UserDisapprovalChecker()));

            emotion.addUserParameterValueCalculator(R.PARAM_APPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addUserParameterValueCalculator(R.PARAM_DISAPPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.GRATIFICATION) {

            emotion = new Gratification();
            emotion.setEmotionId(R.GRATIFICATION);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.GRATIFICATION));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_MAGENTA);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanFinishedChecker());
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOther()));
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOtherPlan()));
            emotion.addEmotionalEventChecker(new SystemJoyChecker());
            emotion.addEmotionalEventChecker(new SystemPrideChecker());

        }

        if (emotionId == R.REMORSE) {

            emotion = new Remorse();
            emotion.setEmotionId(R.REMORSE);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.REMORSE));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_TEAL);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanFinishedChecker());
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOther()));
            emotion.addEmotionalEventChecker(new Not(new UserEmotionalOtherPlan()));
            emotion.addEmotionalEventChecker(new SystemDistressChecker());
            emotion.addEmotionalEventChecker(new SystemShameChecker());

        }

        if (emotionId == R.ADMIRATION) {

            emotion = new Admiration();
            emotion.setEmotionId(R.ADMIRATION);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.ADMIRATION));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_SAND);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanCreatedChecker());
            emotion.addEmotionalEventChecker(new Or(new UserApprovalChecker(), new UserDisapprovalChecker()));
            emotion.addEmotionalEventChecker(new UserEmotionalOther());
            emotion.addEmotionalEventChecker(new UserEmotionalOtherPlan());

            emotion.addUserParameterValueCalculator(R.PARAM_APPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addUserParameterValueCalculator(R.PARAM_DISAPPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.REPROACH) {

            emotion = new Reproach();
            emotion.setEmotionId(R.REPROACH);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.REPROACH));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_DARK_SAND);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanCreatedChecker());
            emotion.addEmotionalEventChecker(new Or(new UserApprovalChecker(), new UserDisapprovalChecker()));
            emotion.addEmotionalEventChecker(new UserEmotionalOther());
            emotion.addEmotionalEventChecker(new UserEmotionalOtherPlan());

            emotion.addUserParameterValueCalculator(R.PARAM_APPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addUserParameterValueCalculator(R.PARAM_DISAPPROVAL, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.GRATITUDE) {

            emotion = new Gratitude();
            emotion.setEmotionId(R.GRATITUDE);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.GRATITUDE));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_GREY);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanFinishedChecker());
            emotion.addEmotionalEventChecker(new UserDesirabilityChecker());
            emotion.addEmotionalEventChecker(new UserEmotionalOther());
            emotion.addEmotionalEventChecker(new UserEmotionalOtherPlan());
            emotion.addEmotionalEventChecker(new SystemAdmirationChecker());

            emotion.addUserParameterValueCalculator(R.PARAM_DESIRABILITY, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.ANGER) {

            emotion = new Anger();
            emotion.setEmotionId(R.ANGER);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.ANGER));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_DARK_GREY);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new PlanFinishedChecker());
            emotion.addEmotionalEventChecker(new UserDesirabilityChecker());
            emotion.addEmotionalEventChecker(new UserEmotionalOther());
            emotion.addEmotionalEventChecker(new UserEmotionalOtherPlan());
            emotion.addEmotionalEventChecker(new SystemReproachChecker());

            emotion.addUserParameterValueCalculator(R.PARAM_DESIRABILITY, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        return emotion;
    }

}