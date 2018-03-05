package sk.tuke.fei.bdi.emotionalengine.service;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.commons.future.IFuture;

import java.util.Map;

/**
 * @author Peter Zemianek
 */
public interface ICommunicationService {

    /**
     * Other agents can send message by calling this method on receiver's Communicaton Service
     * @param message received message
     * @return Future result of successful or unsuccessful receive
     */
    IFuture<Void> messageReceived(Map<String, String> message);

}
