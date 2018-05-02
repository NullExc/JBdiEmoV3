package sk.tuke.fei.bdi.emotionalengine.component.service;

import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import jadex.commons.future.IFuture;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;

import java.util.Map;

/**
 * This class provides functionality of communication service.
 *
 * @author Peter Zemianek
 */
@Service
public class CommunicationService implements ICommunicationService {

    /**
     * Instance of MessageCenter is called in messageReceived method to start receiving process.
     */
    private MessageCenter messageCenter;

    /**
     * Internal access of agent to provide this access to MessageCenter and get Engine Belief.
     */
    @ServiceComponent
    protected IInternalAccess access;

    @Override
    public IFuture<Void> messageReceived(Map<String, String> message) {

        if (messageCenter == null) messageCenter = new MessageCenter(access);

        messageCenter.receiveEmotionalMessage(message);

        return IFuture.DONE;
    }

    @Override
    public Element getElement(String elementName, int type) {
        return getEngine().getElement(elementName, type);
    }

    @Override
    public Element[] getElements(int type) {
        return getEngine().getElements(type);
    }

    @Override
    public Double getPositiveMood() {
        return getEngine().getPositiveMood().getIntensity();
    }

    @Override
    public Double getNegativeMood() {
        return getEngine().getNegativeMood().getIntensity();
    }

    @Override
    public Boolean isInitialized() {
        return getEngine().isInitialized();
    }

    private Engine getEngine() {
        return (Engine) access.getComponentFeature(IInternalBDIAgentFeature.class)
                .getBDIModel().getCapability().getBelief("engine").getValue(access);
    }
}