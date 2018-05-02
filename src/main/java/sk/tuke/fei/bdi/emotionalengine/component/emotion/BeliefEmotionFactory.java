package sk.tuke.fei.bdi.emotionalengine.component.emotion;

import sk.tuke.fei.bdi.emotionalengine.component.emotion.belief.*;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.calculators.PolynomialCalculator;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.belief.BeliefAttractiveChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.belief.BeliefAttractivenessIntensityChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.belief.BeliefFamiliarChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.element.BeliefChangedChecker;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.operator.Not;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Provides set of emotions related to Belief. The set of emotions is given to a Element instance of emotional Beliefs.
 *
 * Creates instances of these emotions: LIKING, DISLIKING, LOVE, HATE, INTEREST, DISGUST
 *
 *
 *
 * @author Tomáš Herich
 */

public class BeliefEmotionFactory implements EmotionFactory {

    @Override
    public Set<Emotion> getPossibleEmotions() {

        // Create possible emotions set
        Set<Emotion> possibleEmotions = new HashSet<Emotion>();

        // Add emotions
        possibleEmotions.add(getEmotion(R.LIKING));
        possibleEmotions.add(getEmotion(R.DISLIKING));

        possibleEmotions.add(getEmotion(R.LOVE));
        possibleEmotions.add(getEmotion(R.HATE));

        possibleEmotions.add(getEmotion(R.INTEREST));
        possibleEmotions.add(getEmotion(R.DISGUST));

        return possibleEmotions;

    }

    @Override
    public Emotion getEmotion(int emotionId) {

        Emotion emotion = null;

        if (emotionId == R.LIKING) {

            // Basic emotion info
            emotion = new Liking();
            emotion.setEmotionId(R.LIKING);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.LIKING));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_ORANGE);
            emotion.setIntensity(0);

            // Event checkers (decide if emotion intensity calculation will be triggered)
            emotion.addEmotionalEventChecker(new BeliefChangedChecker());

            // Value parameter calculators (not needed in every emotions)
            emotion.addSystemParameterValueCalculator(R.LOVE, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.INTEREST, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.DISLIKING) {

            emotion = new Disliking();
            emotion.setEmotionId(R.DISLIKING);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.DISLIKING));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_LIGHT_BLUE);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new BeliefChangedChecker());

            emotion.addSystemParameterValueCalculator(R.HATE, new PolynomialCalculator(R.CALC_QUADRATIC));
            emotion.addSystemParameterValueCalculator(R.DISGUST, new PolynomialCalculator(R.CALC_QUADRATIC));

        }

        if (emotionId == R.LOVE) {

            emotion = new Love();
            emotion.setEmotionId(R.LOVE);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.LOVE));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_RED);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new BeliefChangedChecker());
            emotion.addEmotionalEventChecker(new BeliefFamiliarChecker());
            emotion.addEmotionalEventChecker(new BeliefAttractiveChecker());
            emotion.addEmotionalEventChecker(new BeliefAttractivenessIntensityChecker());

        }

        if (emotionId == R.HATE) {

            emotion = new Hate();
            emotion.setEmotionId(R.HATE);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.HATE));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_BLUE);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new BeliefChangedChecker());
            emotion.addEmotionalEventChecker(new BeliefFamiliarChecker());
            emotion.addEmotionalEventChecker(new Not(new BeliefAttractiveChecker()));
            emotion.addEmotionalEventChecker(new BeliefAttractivenessIntensityChecker());

        }

        if (emotionId == R.INTEREST) {

            emotion = new Interest();
            emotion.setEmotionId(R.INTEREST);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.INTEREST));
            emotion.setEmotionPositive(true);
            emotion.setColor(R.GUI_YELLOW);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new BeliefChangedChecker());
            emotion.addEmotionalEventChecker(new Not(new BeliefFamiliarChecker()));
            emotion.addEmotionalEventChecker(new BeliefAttractiveChecker());
            emotion.addEmotionalEventChecker(new BeliefAttractivenessIntensityChecker());

        }

        if (emotionId == R.DISGUST) {

            emotion = new Disgust();
            emotion.setEmotionId(R.DISGUST);
            emotion.setEmotionName(R.EMOTIONAL_IDS_NAMES.get(R.DISGUST));
            emotion.setEmotionPositive(false);
            emotion.setColor(R.GUI_TEAL);
            emotion.setIntensity(0);

            emotion.addEmotionalEventChecker(new BeliefChangedChecker());
            emotion.addEmotionalEventChecker(new Not(new BeliefFamiliarChecker()));
            emotion.addEmotionalEventChecker(new Not(new BeliefAttractiveChecker()));
            emotion.addEmotionalEventChecker(new BeliefAttractivenessIntensityChecker());

        }

        return emotion;

    }
}
