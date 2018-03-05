package sk.tuke.fei.bdi.emotionalengine.service;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;

import java.util.Map;

/**
 * @author Peter Zemianek
 */
@Service
public class CommunicationService implements ICommunicationService {

    private MessageCenter messageCenter;

    @ServiceComponent
    protected IInternalAccess access;

    @Override
    public IFuture<Void> messageReceived(Map<String, String> message) {

        if (messageCenter == null) messageCenter = new MessageCenter(access);

        messageCenter.recieveMessages(message);

        return IFuture.DONE;
    }
}