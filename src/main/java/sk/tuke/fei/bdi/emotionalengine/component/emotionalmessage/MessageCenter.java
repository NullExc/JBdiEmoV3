package sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage;

import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bdiv3.model.MCapability;
import jadex.bdiv3.model.MPlan;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import sk.tuke.fei.bdi.emotionalengine.annotation.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.ParameterValueMapper;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.component.service.ICommunicationService;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * MessageCenter class provides functionality to send and receive emotional messages.
 *
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class MessageCenter {

    private IInternalAccess access;
    private final Engine engine;
    private final MCapability capability;
    private final ParameterValueMapper params;

    public MessageCenter(IInternalAccess access) {

        this.access = access;

        this.engine = (Engine) access.getComponentFeature(IInternalBDIAgentFeature.class).getBDIModel().getCapability().getBelief("engine").getValue(access);

        capability = access.getComponentFeature(IInternalBDIAgentFeature.class).getBDIModel().getCapability();

        params = new ParameterValueMapper(engine.getAgentObject());
    }

    /**
     * Represents functionality of sending emotional message. Creates message in form of Map object with specific
     * attributes for receiver. Retrieves instance of ICommunicationService service for each receiver and calls its method
     * to start receiving process.
     *
     * @param planName Name of plan which has triggered a event.
     * @param eventType Type of triggered event. Possible values: R.EVT_PLAN_CREATED, R.EVT_PLAN_FINISHED
     * @param resultType Information about result of plan execution. Result can be 'success' or 'failure' in case of 'finished' event
     *                   or NULL in case of 'created' event.
     */
    public void sendEmotionalMessage(final String planName, int eventType, int resultType) {

        final String messageEventType;
        if (eventType == R.EVT_PLAN_CREATED) {
            messageEventType = R.MESSAGE_PLAN_CREATED;
        } else {
            messageEventType = R.MESSAGE_PLAN_FINISHED;
        }

        // Convert engine result info to message result info format
        final String messageResultType;
        if (resultType == R.RESULT_NULL) {
            messageResultType = R.MESSAGE_RESULT_NULL;
        } else if (resultType == R.RESULT_SUCCESS) {
            messageResultType = R.MESSAGE_RESULT_SUCCESS;
        } else {
            messageResultType = R.MESSAGE_RESULT_FAILURE;
        }

        for (final IComponentIdentifier serviceCid : engine.getEmotionalOtherIds()) {

            final IFuture<ICommunicationService> service = SServiceProvider.getService(access.getExternalAccess(), serviceCid, ICommunicationService.class);;

            service.addResultListener(new IResultListener<ICommunicationService>() {
                @Override
                public void exceptionOccurred(Exception exception) {
                    //exception.printStackTrace();
                    try {
                        throw new JBDIEmoException("ICommunicationService is not provided by '"
                                + serviceCid.getLocalName() + "'");
                    } catch (JBDIEmoException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void resultAvailable(ICommunicationService result) {

                    Map<String, String> message = new HashMap<>();

                    message.put(R.KEY_MESSAGE_EMOTIONAL, R.MESSAGE_EMOTIONAL);
                    message.put(R.KEY_MESSAGE_PLAN, messageEventType);
                    message.put(R.KEY_MESSAGE_RESULT, messageResultType);
                    message.put(R.KEY_SENDER_ID, access.getComponentIdentifier().getLocalName());
                    message.put(R.KEY_PLAN_NAME, planName);

                    IFuture<Void> recieve = result.messageReceived(message);
                    recieve.addResultListener(new IResultListener<Void>() {
                        @Override
                        public void exceptionOccurred(Exception exception) {
                            exception.printStackTrace();
                        }

                        @Override
                        public void resultAvailable(Void result) {

                        }
                    });

                }
            });
        }
    }

    /**
     * Represents functionality of receiving emotional message. Uses MessageParser class to retrieve its attributes and tries
     * to find plan of a receiver, which reacts on sender plan. If it finds a match, Emotional Event is created and processed.
     *
     * @param receivedMessage Represents emotional message to receive. Contains this attributes : Name of plan, event type,
     * result type and sender name
     */
    public void receiveEmotionalMessage(Map<String, String> receivedMessage) {

        // Assign message event to message parser
        MessageParser message = new MessageParser(receivedMessage);

        // Add received message to engine set (used as data source for GUI)
        engine.addReceivedMessage(message.getMessageContent());

        Element element = null;
        String otherName = null;
        String otherPlanName = null;
        MPlan otherPlanModel = null;

        // Get plan models from agent definition file (ADF)
        List<MPlan> planModels = capability.getPlans();

        // Iterate plan models
        for (MPlan planModel : planModels) {

            try {

                // Get other group parameter
                // This parameter decides whether emotional event is triggered only by exact agent
                // or all agents of the same type (e.g. EmotionalAgent can be triggered only by EmotionalAgent
                // or by all of EmotionalAgent1, EmotionalAgent2, ...)

                String description = planModel.getDescription();

                EmotionalPlan emotionalPlan = JBDIEmo.UserPlanParams.get(engine.getAgentName()).get(description);

                if (emotionalPlan == null) continue;

                boolean otherGroup = false;

                if (JBDIEmo.findPlanParameter(emotionalPlan, R.PARAM_EMOTIONAL_OTHER_GROUP) != null) {
                    otherGroup = JBDIEmo.findPlanParameter(emotionalPlan, R.PARAM_EMOTIONAL_OTHER_GROUP).booleanValue();
                }
                if (JBDIEmo.findPlanParameter(emotionalPlan, R.PARAM_EMOTIONAL_OTHER) != null) {
                    otherName = JBDIEmo.findPlanParameter(emotionalPlan, R.PARAM_EMOTIONAL_OTHER).stringValue();
                }
                if (JBDIEmo.findPlanParameter(emotionalPlan, R.PARAM_EMOTIONAL_OTHER_PLAN) != null) {
                    otherPlanName = JBDIEmo.findPlanParameter(emotionalPlan, R.PARAM_EMOTIONAL_OTHER_PLAN).stringValue();
                }

                // Get name of emotional other from message (sender name) and get only name part, discard platform
                String senderName = message.getSender();

                // Based on other group parameter
                if (otherName != null && otherPlanName != null) {
                    if (otherGroup) {

                        // If sender name satisfies name type of emotional other specified in ADF
                        if (senderName.matches(otherName + ".*") && message.getPlan().equals(otherPlanName)) {

                            // Get plan model of emotional other agent type specified in this agent ADF
                            otherPlanModel = planModel;
                            break;
                        }
                    } else {

                        // If sender name satisfies exact name of emotional other specified in ADF
                        if (senderName.matches(otherName) && message.getPlan().equals(otherPlanName)) {

                            // Get plan model of exact emotional other agent specified in this agent ADF
                            otherPlanModel = planModel;
                            break;
                        }
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
//                Try catch used because it can't be tested otherwise because Jadex bug where you
//                can't get parameter array of plan but you can get parameter by name
            }
        }

        // Check if message contained valid information about other agent defined in this agent ADF
        if (otherName != null && otherPlanName != null && otherPlanModel != null) {

            // Get element of emotional other agent plan
            element = engine.getElement(otherPlanModel.getDescription(), R.PLAN);

        }

        // Check if element is valid
        if (element != null) {

            // Create emotional event
            EmotionalEvent emotionalEvent = new EmotionalEvent();

            // Convert message event info to engine event info format
            int eventType;
            if (message.getEvent().equals(R.MESSAGE_PLAN_CREATED)) {
                eventType = R.EVT_PLAN_CREATED;
            } else {
                eventType = R.EVT_PLAN_FINISHED;
            }

            // Convert message result info to engine result info format
            int resultType;
            if (message.getResult().equals(R.MESSAGE_RESULT_NULL)) {
                resultType = R.RESULT_NULL;
            } else if (message.getResult().equals(R.MESSAGE_RESULT_SUCCESS)) {
                resultType = R.RESULT_SUCCESS;
            } else {
                resultType = R.RESULT_FAILURE;
            }

            // Map emotional other agent plan defined in this agent ADF for user parameters
            Map<String, Double> userParameters = params
                    .getUserParameterValues(null, JBDIEmo.UserPlanParams.get(engine.getAgentName()).get(otherPlanModel.getDescription()).value());  //params.getPlanModelUserParameterValues(otherPlanModel);

            // Add special user parameters which signify that this is plan of emotional other agent
            userParameters.put(R.PARAM_EMOTIONAL_OTHER, 1.0d);
            userParameters.put(R.PARAM_EMOTIONAL_OTHER_PLAN, 1.0d);

            // Add prepared data to emotional event
            emotionalEvent.setElementName(element.getName());
            emotionalEvent.setEventType(eventType);
            emotionalEvent.setResultType(resultType);
            emotionalEvent.setUserParameters(userParameters);
            emotionalEvent.setSystemParameters(params.getSystemParameterValues(element));

            // Send emotional event to its respective element
            element.processEmotionalEvent(emotionalEvent);
        }
    }
}
