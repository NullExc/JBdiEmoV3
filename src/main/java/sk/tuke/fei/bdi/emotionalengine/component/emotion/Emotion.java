package sk.tuke.fei.bdi.emotionalengine.component.emotion;


import sk.tuke.fei.bdi.emotionalengine.component.JadexBeliefChangeDetectionSupport;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.calculators.Calculator;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEventChecker;
import sk.tuke.fei.bdi.emotionalengine.helper.MyMath;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tomáš Herich
 */

public abstract class Emotion extends JadexBeliefChangeDetectionSupport {

    private int emotionId;
    private String emotionName;
    private boolean isEmotionPositive;
    private Paint color;
    protected double intensity;
    protected Set<EmotionalEventChecker> eventCheckers;
    protected Map<String, Calculator> userParameterValueCalculators;
    protected Map<Integer, Calculator> systemParameterValueCalculators;

    private int decayStep = 0;
    private double decaySourceIntensity;

    protected Emotion() {
        super();

        eventCheckers = new HashSet<EmotionalEventChecker>();
        userParameterValueCalculators = new HashMap<String, Calculator>();
        systemParameterValueCalculators = new HashMap<Integer, Calculator>();

    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public String getEmotionName() {
        return emotionName;
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    public boolean isEmotionPositive() {
        return isEmotionPositive;
    }

    public void setEmotionPositive(boolean emotionPositive) {
        isEmotionPositive = emotionPositive;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    public double getIntensity() {
        return intensity;
    }

    /**
     * Set new emotion intensity element.
     * Method is called by new emotional intensity element
     * calculation. Method ensures trimming
     * of element so it belongs to <0,1> interval.
     * Method fireBeliefChange() from JadexBeliefChangeDetectionSupport
     * class is called to notify Jadex that Engine belief
     * was changed.
     *
     * @param newIntensity
     */
    public void setIntensity(double newIntensity) {

        intensity = intensity + newIntensity;

        // Intensity must belong to interval <0,1>
        if (intensity > 1) {
            intensity = 1;
        }

        // Update decay source which will be used for intensity decay calculation
        this.decaySourceIntensity = intensity;

        fireBeliefChange();

    }

    public void addEmotionalEventChecker(EmotionalEventChecker eventChecker) {
        eventCheckers.add(eventChecker);
    }

    public void addUserParameterValueCalculator(String parameter, Calculator calculator) {
        userParameterValueCalculators.put(parameter, calculator);
    }

    public void addSystemParameterValueCalculator(Integer parameter, Calculator calculator) {
        systemParameterValueCalculators.put(parameter, calculator);
    }

    /**
     * Check if emotion is active
     *
     * @return true if intensity is greater than zero
     */
    public boolean isActive() {
        return intensity > 0;
    }

    /**
     * Check if emotional event contains parameters, which
     * satisfy emotion's eliciting conditions checking
     *
     * @param event emotional event
     * @return true if eliciting conditions were satisfied by emotional event
     */
    protected boolean isCalculationTriggeredByEvent(EmotionalEvent event) {

        // Check if event checkers are provided
        if (!eventCheckers.isEmpty()) {

            // Iterate event checkers
            for (EmotionalEventChecker checker : eventCheckers) {

                // Check if emotional event satisfies event checkers assigned to emotion
                if (!checker.checkEmotionalEvent(event)) {

                    // Intensity calculation will not be triggered
                    return false;
                }
            }

            // If all event checkers were satisfied trigger intensity calculation
            return true;

        } else {
            return false;
        }

    }

    /**
     * Adjust values of user emotional event's parameters
     * by calculators, which were assigned to emotion
     *
     * @param event emotional event
     */
    protected void adjustUserParameterValues(EmotionalEvent event) {

        // Get calculator parameters (parameters for which calculator was assigned)
        Set<String> parameters = userParameterValueCalculators.keySet();

        // Check if calculator parameters are valid
        if (parameters != null && !parameters.isEmpty()) {

            // Iterate calculator parameter
            for (String parameterKey : parameters) {

                // Get user parameters from emotional event
                Map<String, Double> eventUserParameters = event.getUserParameters();

                // Check if emotional event user parameters contain calculator parameter
                if (eventUserParameters.containsKey(parameterKey)) {

                    // Get calculator
                    Calculator calculator = userParameterValueCalculators.get(parameterKey);

                    // Get original emotional event parameter element
                    Double eventParameterValue = eventUserParameters.get(parameterKey);

                    // Calculate new emotional event parameter element by using calculator
                    Double newEventParameterValue = calculator.calculateValue(eventParameterValue);

                    // Assign new emotional event parameter element back to emotional event for intensity calculation
                    eventUserParameters.put(parameterKey, newEventParameterValue);

                }
            }
        }
    }

    /**
     * Adjust values of system emotional event's parameters
     * by calculators, which were assigned to emotion
     *
     * @param event emotional event
     */
    protected void adjustSystemParameterValues(EmotionalEvent event) {

        // Get calculator parameters (parameters for which calculator was assigned)
        Set<Integer> parameters = systemParameterValueCalculators.keySet();

        // Check if calculator parameters are valid
        if (parameters != null && !parameters.isEmpty()) {

            // Iterate calculator parameter
            for (Integer parameterKey : parameters) {

                // Get system parameters from emotional event
                Map<Integer, Double> eventSystemParameters = event.getSystemParameters();

                // Check if emotional event system parameters contain calculator parameter
                if (eventSystemParameters.containsKey(parameterKey)) {

                    // Get calculator
                    Calculator calculator = systemParameterValueCalculators.get(parameterKey);

                    // Get original emotional event parameter element
                    Double eventParameterValue = eventSystemParameters.get(parameterKey);

                    // Calculate new emotional event parameter element by using calculator
                    Double newEventParameterValue = calculator.calculateValue(eventParameterValue);

                    // Assign new emotional event parameter element back to emotional event for intensity calculation
                    eventSystemParameters.put(parameterKey, newEventParameterValue);

                }
            }
        }
    }

    /**
     * Process emotional event. Method is called by parent element,
     * which received emotional event from ElementEventMonitor
     *
     * Method facilitates functionality in following order:
     *
     * 1. test if event satisfies eliciting conditions, if yes continue
     * 2. deep copy emotional event (to independently perform possible parameter calculations)
     * 3. calculate user parameter values
     * 4. calculate system parameter values
     * 5. calculate new emotional intensity element
     * 6. update emotions decay source with this new intensity element
     *
     * @param event emotional event
     */
    public void processEmotionalEvent(EmotionalEvent event) {

        // Check if intensity calculation is triggered by emotional event
        if (isCalculationTriggeredByEvent(event)) {

            EmotionalEvent localEventCopy = event.deepCopy();

            // Adjust user parameters values (if they have assigned calculator)
            adjustUserParameterValues(localEventCopy);

            // Adjust system parameter values (if they have assigned calculator)
            adjustSystemParameterValues(localEventCopy);

            // Calculate emotion intensity
            calculateIntensity(localEventCopy);

            // Update decay source for intensity decay
            updateDecaySource();

        }

    }


    private void updateDecaySource() {

        // Store newly calculated intensity as decay source
        decaySourceIntensity = intensity;

        // Reset decay step (input to decay function) to 0
        decayStep = 0;

    }

    /**
     * Method periodically called by Engine. Decays emotion's intensity
     * along parametrized inverse sigmoid function. Decay steps
     * parameter represents number of method calls needed for intensity
     * to drop to negligible element
     *
     * @param decaySteps parameter of inverse sigmoid function
     */
    public void decayIntensity(int decaySteps) {

        // Shifted inverse sigmoid function
        // Coefficients to shape function so it starts at (x = 0 -> y = caa 0.993)
        // and requires specified number of steps (decaySteps) (x element) where (y = cca 0.003)
        double coef1 = 0.5 * decaySteps;
        double coef2 = 0.1 * decaySteps;

        // 1 / (1 + (e ^ ((x - 0.5 * 100) / (0.1 * 100)))

        double decayedIntensity = decaySourceIntensity * (1 / (1 + (Math.pow(Math.E, (decayStep - coef1) / coef2))));


        // Fixed decrement used to kill emotions that are near the end of sigmoid function (y near 0)
        decayedIntensity = Math.max(0.0, decayedIntensity - 0.0005);

        intensity = MyMath.roundDouble(decayedIntensity, 4);

        decayStep++;

    }

    // Intensity calculation is implemented for every emotion (e.g. Hope, Fear, Joy, e.t.c ... in their classes)

    /**
     * Abstract method implemented by all specific emotions (e.g. FEAR, HOPE).
     * Specific implementations contain formula for new emotional intensity
     * element calculation (is calculated from emotional event adjusted parameters).
     * Intensity is set by calling of method (setIntensity()) not by return element.
     *
     * @param event local copy of emotional event (deepCopy())
     */
    public abstract void calculateIntensity(EmotionalEvent event);

}
