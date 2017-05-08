package sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage;

import jadex.bdiv3.model.BDIModel;
import jadex.bdiv3.model.MCapability;
import jadex.bdiv3.model.MPlan;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.EmotionalEvent;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalevent.ParameterValueMapper;
import sk.tuke.fei.bdi.emotionalengine.component.exception.JBDIEmoException;
import sk.tuke.fei.bdi.emotionalengine.parser.annotations.EmotionalPlan;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class MessageCenter {

    private IInternalAccess access = null;
    private final Engine engine;
    private final BDIModel model;
    private final MCapability capability;

    private ParameterValueMapper params;

    public MessageCenter(Object agent) {


        if (agent instanceof IInternalAccess) {

            this.access = (IInternalAccess) agent;

        } else {

            try {
                this.access = JBDIEmo.findAgentComponent(agent, IInternalAccess.class);
            } catch (JBDIEmoException e) {
                e.printStackTrace();
            }

        }

        IArgumentsResultsFeature argumentFeature = access.getComponentFeature(IArgumentsResultsFeature.class);
        engine = (Engine) argumentFeature.getArguments().get(R.ENGINE);

        model = (BDIModel) access.getExternalAccess().getModel().getRawModel();
        capability = model.getCapability();

        params = new ParameterValueMapper(agent);
    }

    public void sendEmotionalMessage(String planName, int eventType, int resultType) {

        String messageEventType;
        if (eventType == R.EVT_PLAN_CREATED) {
            messageEventType = R.MESSAGE_PLAN_CREATED;
        } else {
            messageEventType = R.MESSAGE_PLAN_FINISHED;
        }

        // Convert engine result info to message result info format
        String messageResultType;
        if (resultType == R.RESULT_NULL) {
            messageResultType = R.MESSAGE_RESULT_NULL;
        } else if (resultType == R.RESULT_SUCCESS) {
            messageResultType = R.MESSAGE_RESULT_SUCCESS;
        } else {
            messageResultType = R.MESSAGE_RESULT_FAILURE;
        }

        // Get component identifiers of emotional others stored in engine
        Set<IComponentIdentifier> componentIds = engine.getEmotionalOtherIds();

        System.err.println("<<<SENDING IN PROGRES ... " + componentIds + " " + JBDIEmo.MessageListeners);

        for (IComponentIdentifier serviceCid : JBDIEmo.MessageListeners) {

           // IComponentIdentifier listenerCid = service.getComponentIdentifier().get();

            IFuture<ICommunicationService> service = SServiceProvider.getService(access.getExternalAccess(), serviceCid, ICommunicationService.class);

            service.addResultListener(new IResultListener<ICommunicationService>() {
                @Override
                public void exceptionOccurred(Exception exception) {
                    exception.printStackTrace();
                }

                @Override
                public void resultAvailable(ICommunicationService result) {
                    IComponentIdentifier cid = result.getComponentIdentifier().get();

                    if (componentIds.contains(cid)) {

                        Map<String, String> message = new HashMap<>();

                        message.put(R.KEY_MESSAGE_EMOTIONAL, R.MESSAGE_EMOTIONAL);
                        message.put(R.KEY_MESSAGE_PLAN, messageEventType);
                        message.put(R.KEY_MESSAGE_RESULT, messageResultType);
                        message.put(R.KEY_SENDER_ID, access.getComponentIdentifier().getLocalName());
                        message.put(R.KEY_PLAN_NAME, planName);

                        System.err.println("<<< MESSAGE " + message + " SENT FROM " + access.getComponentIdentifier().getLocalName());

                        IFuture<Void> recieve = result.messageRecieved(message);
                        recieve.addResultListener(new IResultListener<Void>() {
                            @Override
                            public void exceptionOccurred(Exception exception) {
                                exception.printStackTrace();
                            }

                            @Override
                            public void resultAvailable(Void result) {
                                System.err.println("<<< RECIEVE DONE");
                            }
                        });
                    }
                }
            });
        }
    }

    public void recieveMessages(Map<String, String> receivedMessage) {

        System.err.println("<<<< MESSAGE RECIEVED! " + receivedMessage + " TO : " + access.getComponentIdentifier().getLocalName());

        // Assign message event to message parser
        MessageParser message = new MessageParser(receivedMessage);

        // Add received message to engine set (used as data source for GUI)
        engine.addReceivedMessage(message.getMessageContent());

        Element objectValue = null;
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

                EmotionalPlan emotionalPlan = JBDIEmo.UserPlanParams.get(description);

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

            } catch (Exception e) {
//                Try catch used because it can't be tested otherwise because Jadex bug where you
//                can't get parameter array of plan but you can get parameter by name
            }
        }

        // Check if message contained valid information about other agent defined in this agent ADF
        if (otherName != null && otherPlanName != null && otherPlanModel != null) {

            // Get objectValue of emotional other agent plan
            objectValue = engine.getElement(otherPlanModel.getDescription(), R.PLAN);

        }

        // Check if objectValue is valid
        if (objectValue != null) {

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
                    .getUserParameterValues(JBDIEmo.UserPlanParams.get(otherPlanModel.getDescription()).value());  //params.getPlanModelUserParameterValues(otherPlanModel);

            // Add special user parameters which signify that this is plan of emotional other agent
            userParameters.put(R.PARAM_EMOTIONAL_OTHER, 1.0d);
            userParameters.put(R.PARAM_EMOTIONAL_OTHER_PLAN, 1.0d);

            // Add prepared data to emotional event
            emotionalEvent.setElementName(objectValue.getName());
            emotionalEvent.setEventType(eventType);
            emotionalEvent.setResultType(resultType);
            emotionalEvent.setUserParameters(userParameters);
            emotionalEvent.setSystemParameters(params.getSystemParameterValues(objectValue));

            // Send emotional event to its respective objectValue
            objectValue.processEmotionalEvent(emotionalEvent);

        }

    }


}
