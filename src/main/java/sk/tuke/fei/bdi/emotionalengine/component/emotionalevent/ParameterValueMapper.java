package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent;


import jadex.bdiv3.runtime.impl.RGoal;
import jadex.bdiv3.runtime.impl.RPlan;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps emotional parameter values which are used inside of Emotional Events to fire emotions.
 *
 * User parameters are these which user specifies in plan of goal definition (e.g. .
 *
 * System parameters are intensities of emotions from specific BDI element
 *
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class ParameterValueMapper {

    private Object agentObject;
    private Class<?> agentClass;

    public ParameterValueMapper(Object agent) {
        this.agentObject = agent;
        this.agentClass = agent.getClass();
    }

    /**
     *
     * @param plan
     * @param parameters
     * @return Set of user parameter values
     */
    public Map<String, Double> getUserParameterValues(RPlan plan, EmotionalParameter[] parameters) {

        Map<String, Double> userParameters = new HashMap<String, Double>();

        Object object = null;

        Class clazz = null;

        for (EmotionalParameter parameter : parameters) {

            if (!parameter.agentClass() && plan != null && plan.getPojoPlan() != null) {
                object = plan.getPojoPlan();
                clazz = object.getClass();
            } else {
                object = agentObject;
                clazz = agentClass;
            }

            try {

                if (parameter.target().equals(R.DOUBLE)) {

                    if (parameter.doubleValue() > 0) {
                        userParameters.put(parameter.parameter(), parameter.doubleValue());
                    }

                } else if (parameter.target().equals(R.FIELD)) {

                    Field field = clazz.getDeclaredField(parameter.fieldName());
                    field.setAccessible(true);
                    Double result = (Double) field.get(object);
                    userParameters.put(parameter.parameter(), result);

                } else if (parameter.target().equals(R.METHOD)) {

                    Method method = clazz.getDeclaredMethod(parameter.methodName());
                    method.setAccessible(true);
                    Double result = (Double) method.invoke(object);
                    userParameters.put(parameter.parameter(), result);
                }

            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return userParameters;
    }

    /**
     * Retrieves
     *
     * @param goal Runtime representation of Goal
     * @param parameters Array of emotional parameters
     * @return Set of system parameter values
     */
    public Map<String, Double> getGoalUserParameterValues(RGoal goal, EmotionalParameter[] parameters) {

        Map<String, Double> userParameters = new HashMap<String, Double>();

        Object object = null;

        Class clazz = null;

        for (EmotionalParameter parameter : parameters) {

            if (!parameter.agentClass()) {
                object = goal.getPojoElement();
                clazz = object.getClass();
            } else {
                object = agentObject;
                clazz = agentClass;
            }

            try {

                if (parameter.target().equals(R.DOUBLE)) {

                    if (parameter.doubleValue() > 0) {
                        userParameters.put(parameter.parameter(), parameter.doubleValue());
                    }

                } else if (parameter.target().equals(R.FIELD)) {

                    Field field = clazz.getDeclaredField(parameter.fieldName());
                    field.setAccessible(true);
                    Double result = (Double) field.get(object);
                    userParameters.put(parameter.parameter(), result);

                } else if (parameter.target().equals(R.METHOD)) {

                    Method method = clazz.getDeclaredMethod(parameter.methodName());
                    method.setAccessible(true);
                    Double result = (Double) method.invoke(object);
                    userParameters.put(parameter.parameter(), result);
                }

            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return userParameters;
    }

    public Map<Integer, Double> getSystemParameterValues(Element element) {

        // Create user parameter objectValue map for emotional event
        Map<Integer, Double> systemParameters = new HashMap<Integer, Double>();

        // Check if objectValue is valid
        if (element != null) {

            // Check if objectValue instance contains any system parameter
            Set<Emotion> elementInstanceEmotions = element.getEmotions();

            if (elementInstanceEmotions != null && !elementInstanceEmotions.isEmpty()) {
                for (Object emotion : element.getEmotions()) {

                    // If objectValue instance contains valid system parameter get its objectValue
                    if (((Emotion) emotion).getIntensity() != 0) {

                        systemParameters.put(((Emotion) emotion).getEmotionId(), ((Emotion) emotion).getIntensity());
                    }
                }
            }
        }
        return systemParameters;
    }

    public void addSystemParameterValuesForGoalPlanEmotions(Engine engine, Map<Integer, Double> parameterValueMap, String reason) {

        // Plan reason is goal
        if (reason != null) {

            // Goals emotion needed for plan emotions are joy and distress
            // Get goals joy, and distress emotion intensities
            double joy = engine.getElement(reason, R.GOAL).getEmotion(R.JOY).getIntensity();
            double distress = engine.getElement(reason, R.GOAL).getEmotion(R.DISTRESS).getIntensity();

            // If values is valid store parameters and their values
            if (joy > 0) {
                parameterValueMap.put(R.JOY, joy);
            }
            if (distress > 0) {
                parameterValueMap.put(R.DISTRESS, distress);
            }
        }
    }
}
