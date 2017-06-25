package sk.tuke.fei.bdi.emotionalengine.component.emotionalevent;


import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalParameter;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
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

    public Map<String, Double> getUserParameterValues(EmotionalParameter[] parameters) {

        Map<String, Double> userParameters = new HashMap<String, Double>();

        for (EmotionalParameter parameter : parameters) {

            try {

                if (parameter.target().equals(R.SIMPLE_DOUBLE)) {

                    if (parameter.doubleValue() > 0) {
                        userParameters.put(parameter.parameter(), parameter.doubleValue());
                    }

                } else if (parameter.target().equals(R.FIELD)) {

                    Field field = agentClass.getDeclaredField(parameter.fieldValue());
                    field.setAccessible(true);
                    Double result = (Double) field.get(agentObject);
                    userParameters.put(parameter.parameter(), result);

                } else if (parameter.target().equals(R.METHOD)) {

                    Method method = agentClass.getDeclaredMethod(parameter.methodValue());
                    method.setAccessible(true);
                    Double result = (Double) method.invoke(agentObject);
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

     //   System.err.println("User parameters : " + userParameters);

        return userParameters;
    }

    /*public Map<String, Double> getPlanModelUserParameterValues(Plan parentPlan,MElement elementModel) {

        // Create user parameter objectValue map for emotional event
        Map<String, Double> userParameters = new HashMap<String, Double>();

        // Check if objectValue model contains any user parameter
        for (String parameterName : R.EMOTIONAL_PARAMETERS) {

            // Cast objectValue model to plan model
            MPlan plan = (MPlan) elementModel;

            try {

                // Model parameter expressions don't get final objectValue automatically
                // (e.g. Math.random() will be just String)

                // Get objectValue of objectValue model parameter as expression
                IExpression expressionModel; // = plan.getParameter(parameterName).getValue();

                // Create expression from expression model
                IExpression expression = null; //= parentPlan.createExpression(expressionModel.getText());

                // Get expression objectValue
                Object parameterValue = expression.execute();

                // If objectValue is valid store parameter and its objectValue
                if (parameterValue != null) {

                    double parameterValueDouble = Double.parseDouble(String.valueOf(parameterValue));
                    System.err.println("plan parameter objectValue : " + parameterName + " " + parameterValueDouble);
                    userParameters.put(parameterName, parameterValueDouble);
                }

            } catch (Exception e) {
//                Try catch used because it can't be tested otherwise because Jadex bug where you
//                can't get parameter array of plan but you can get parameter by name
            }
        }
        return  userParameters;
    }*/


    public Map<Integer, Double> getSystemParameterValues(Element element) {

        // Create user parameter objectValue map for emotional event
        Map<Integer, Double> systemParameters = new HashMap<Integer, Double>();

        // Check if objectValue is valid
        if (element != null) {

            // Check if objectValue instance contains any system parameter
            Set<Emotion> elementInstanceEmotions = element.getEmotions();

            if (elementInstanceEmotions != null && !elementInstanceEmotions.isEmpty()) {
                for (Emotion emotion : element.getEmotions()) {

                    // If objectValue instance contains valid system parameter get its objectValue
                    if (emotion.getIntensity() != 0) {

                        systemParameters.put(emotion.getEmotionId(), emotion.getIntensity());

                    }
                }
            }

        }

     //   System.err.println("System parameters : " + systemParameters);

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
