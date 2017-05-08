package sk.tuke.fei.bdi.emotionalengine.service;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.commons.future.IFuture;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;

import java.util.List;
import java.util.Map;

/**
 * @author Peter Zemianek
 */
public interface ICommunicationService {

    public IFuture<Void> initialize(IFuture<IInternalAccess> access, IComponentIdentifier cid);

    public IFuture<Void> sendMessage(Map<String, String> message);

    public IFuture<List<Map<String, String>>> getMessages();

    public IFuture<Void> messageRecieved(Map<String, String> message);

    public IFuture<IComponentIdentifier> getComponentIdentifier();
}
