package sk.tuke.fei.bdi.emotionalengine.service;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Service;
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

    private IComponentIdentifier cid;

    @Override
    public IFuture<Void> initialize(IFuture<IInternalAccess> access, IComponentIdentifier cid) {

        this.cid = cid;

        access.addResultListener(new IResultListener<IInternalAccess>() {
            @Override
            public void exceptionOccurred(Exception exception) {

            }

            @Override
            public void resultAvailable(IInternalAccess result) {

                messageCenter = new MessageCenter(result);
            }
        });

        return IFuture.DONE;
    }

    @Override
    public IFuture<Void> messageReceived(Map<String, String> message) {
        messageCenter.recieveMessages(message);
        return IFuture.DONE;
    }

    @Override
    public IFuture<IComponentIdentifier> getComponentIdentifier() {
        return new Future<>(cid);
    }


}