package sk.tuke.fei.bdi.emotionalengine.service;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage.MessageCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Peter Zemianek
 */
@Service
public class CommunicationService implements ICommunicationService {

    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>();

    private MessageCenter messageCenter;

    private IComponentIdentifier cid;

    public CommunicationService() {

    }

    @Override
    public IFuture<Void> initialize(IFuture<IInternalAccess> access, IComponentIdentifier cid) {

        this.cid = cid;

        access.addResultListener(new IResultListener<IInternalAccess>() {
            @Override
            public void exceptionOccurred(Exception exception) {
                //throw new JBDIEmoException(exception);
            }

            @Override
            public void resultAvailable(IInternalAccess result) {
                messageCenter = new MessageCenter(result);
            }
        });

        return IFuture.DONE;
    }

    public IFuture<Void> sendMessage(Map<String, String> message) {
        messages.add(message);
        return IFuture.DONE;
    }

    public IFuture<List<Map<String, String>>> getMessages() {
        return new Future<List<Map<String, String>>>(messages);
    }

    @Override
    public IFuture<Void> messageRecieved(Map<String, String> message) {
        messageCenter.recieveMessages(message);
        return IFuture.DONE;
    }

    @Override
    public IFuture<IComponentIdentifier> getComponentIdentifier() {
        return new Future<>(cid);
    }

}