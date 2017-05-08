package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent;

import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tomáš Herich
 */

public class EmotionalEvent {

    private String elementName;
    private int eventType;
    private int resultType;
    private Map<String, Double> userParameters = null;
    private Map<Integer, Double> systemParameters = null;

    private Boolean isBeliefFamiliar = null;
    private Boolean isBeliefAttractive = null;
    private Double beliefAttractionIntensity = null;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public Map<String, Double> getUserParameters() {
        return userParameters;
    }

    public void setUserParameters(Map<String, Double> userParameters) {
        this.userParameters = userParameters;
    }

    public Map<Integer, Double> getSystemParameters() {
        return systemParameters;
    }

    public void setSystemParameters(Map<Integer, Double> systemParameters) {
        this.systemParameters = systemParameters;
    }

    public Boolean isBeliefFamiliar() {
        return isBeliefFamiliar;
    }

    public void setBeliefFamiliar(Boolean beliefFamiliar) {
        isBeliefFamiliar = beliefFamiliar;
    }

    public Boolean isBeliefAttractive() {
        return isBeliefAttractive;
    }

    public void setBeliefAttractive(Boolean beliefAttractive) {
        isBeliefAttractive = beliefAttractive;
    }

    public Double getBeliefAttractionIntensity() {
        return beliefAttractionIntensity;
    }

    public void setBeliefAttractionIntensity(Double beliefAttractionIntensity) {
        this.beliefAttractionIntensity = beliefAttractionIntensity;
    }

    public void printEventInfoToConsole() {

        System.out.println("");
        System.out.println("----- EMOTIONAL EVENT -----");
        System.out.println("");
        System.out.println("objectValue: " + elementName);
        System.out.println("event type: " + R.EMOTIONAL_EVENT_TYPE_NAMES.get(eventType));
        System.out.println("result type: " + R.EMOTIONAL_RESULT_TYPE_NAMES.get(resultType));
        System.out.println("");

        if (userParameters != null && !userParameters.isEmpty()) {
            Set<String> keys = userParameters.keySet();

            for (String key : keys) {
                System.out.println("user - " + key + ": " + userParameters.get(key));
            }
        }

        System.out.println("");

        if (systemParameters != null && !systemParameters.isEmpty()) {
            Set<Integer> sysKeys = systemParameters.keySet();

            for (Integer key : sysKeys) {
                System.out.println("system - " + R.EMOTIONAL_IDS_NAMES.get(key) + ": " + systemParameters.get(key));
            }
        }

        System.out.println("");
        System.out.println("familiar: " + isBeliefFamiliar);
        System.out.println("attractive: " + isBeliefAttractive);
        System.out.println("attraction intensity: " + beliefAttractionIntensity);
        System.out.println("");
        System.out.println("---------------------------");
        System.out.println("");

    }

    public EmotionalEvent deepCopy() {

        Map<String, Double> userParamsHelper;
        Map<Integer, Double> systemParamsHelper;

        if (userParameters != null) {
            userParamsHelper = new HashMap<String, Double>(userParameters);
        } else {
            userParamsHelper = null;
        }

        if (systemParameters != null) {
            systemParamsHelper = new HashMap<Integer, Double>(systemParameters);
        } else {
            systemParamsHelper = null;
        }

        EmotionalEvent copy = new EmotionalEvent();

        copy.setElementName(getElementName());
        copy.setEventType(getEventType());
        copy.setResultType(getResultType());
        copy.setUserParameters(userParamsHelper);
        copy.setSystemParameters(systemParamsHelper);
        copy.setBeliefFamiliar(isBeliefFamiliar());
        copy.setBeliefAttractive(isBeliefAttractive());
        copy.setBeliefAttractionIntensity(getBeliefAttractionIntensity());

        return copy;

    }

}
