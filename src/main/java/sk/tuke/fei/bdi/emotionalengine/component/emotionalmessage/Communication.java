package sk.tuke.fei.bdi.emotionalengine.component.emotionalmessage;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInputConnection;
import jadex.bridge.IOutputConnection;
import jadex.bridge.IResourceIdentifier;
import jadex.bridge.service.IServiceIdentifier;
import jadex.bridge.service.types.message.*;
import jadex.commons.IFilter;
import jadex.commons.future.IFuture;

import java.util.Map;

/**
 * Created by Peter on 23.3.2017.
 */
public class Communication implements IMessageService {



    public IFuture<Void> sendMessage(Map<String, Object> message, MessageType msgtype, IComponentIdentifier sender, IResourceIdentifier rid, IComponentIdentifier realrec, byte[] codecids) {
        return null;
    }

    public void deliverMessage(Object msg) {

    }

    public IFuture<IOutputConnection> createOutputConnection(IComponentIdentifier sender, IComponentIdentifier receiver, Map<String, Object> nonfunc) {
        return null;
    }

    public IFuture<IInputConnection> createInputConnection(IComponentIdentifier sender, IComponentIdentifier receiver, Map<String, Object> nonfunc) {
        return null;
    }

    public IFuture<Void> addMessageListener(IMessageListener listener, IFilter filter) {
        return null;
    }

    public IFuture<Void> removeMessageListener(IMessageListener listener) {
        return null;
    }

    public IFuture<Void> addContentCodec(IContentCodec codec) {
        return null;
    }

    public IFuture<Void> removeContentCodec(IContentCodec codec) {
        return null;
    }

    public IFuture<Void> addMessageCodec(Class codec) {
        return null;
    }

    public IFuture<Void> removeMessageCodec(Class codec) {
        return null;
    }

    public IFuture<Void> refreshAddresses() {
        return null;
    }

    public IFuture<String[]> getAddresses() {
        return null;
    }

    public String[] getAddressSchemes() {
        return new String[0];
    }

    public MessageType getMessageType(String type) {
        return null;
    }

    public IFuture<Map<Byte, ICodec>> getAllCodecs() {
        return null;
    }

    public IFuture<ICodec[]> getDefaultCodecs() {
        return null;
    }

    public IServiceIdentifier getServiceIdentifier() {
        return null;
    }

    public IFuture<Boolean> isValid() {
        return null;
    }

    public Map<String, Object> getPropertyMap() {
        return null;
    }
}
