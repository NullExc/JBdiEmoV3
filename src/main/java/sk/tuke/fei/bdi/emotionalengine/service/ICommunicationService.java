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
     * Initialization of Communication Service for receiving Emotional Messages from other agents
     *
     * @param access Internal Access for accessing agent's Communication Service
     * @param cid Other agents are able to send message to agent via his Component identification
     * @return Future result of successful or unsuccessful initialization
     */
    IFuture<Void> initialize(IFuture<IInternalAccess> access, IComponentIdentifier cid);

    /**
     * Other agents can send message by calling this method on receiver's Communicaton Service
     * @param message received message
     * @return Future result of successful or unsuccessful receive
     */
    IFuture<Void> messageReceived(Map<String, String> message);

    /**
     *
     * @return Agent's Component identification
     */
    IFuture<IComponentIdentifier> getComponentIdentifier();


}
