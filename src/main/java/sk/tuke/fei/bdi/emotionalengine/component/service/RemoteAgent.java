package sk.tuke.fei.bdi.emotionalengine.component.service;

import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;

import java.util.Map;

/**
 * Instance of RemoteAgent class acts as proxy object to provide restrictive functionality of ICommunicationService.
 *
 * Object of this class is injected into ICommunicationService variable, specified in agent class with annotation @EmotionalAgent.
 *
 * @author Peter Zemianek
 */
public class RemoteAgent implements ICommunicationService {

    private String agentName;
    private IInternalAccess access;
    private Engine engine;
    private ICommunicationService service;

    public RemoteAgent(String agentName, IInternalAccess access) {
        this.agentName = agentName;
        this.access = access;
        this.engine = (Engine) access.getComponentFeature(IInternalBDIAgentFeature.class)
                .getBDIModel().getCapability().getBelief("engine").getValue(access);
        initCommunicationService();
    }

    @Override
    public IFuture<Void> messageReceived(Map<String, String> message) {
        return Future.DONE;
    }

    @Override
    public Element getElement(String elementName, int type) {
        if (!initCommunicationService()) {
            return null;
        }
        return service.getElement(elementName, type);
    }

    @Override
    public Element[] getElements(int type) {
        if (!initCommunicationService()) {
            return new Element[0];
        }
        return service.getElements(type);
    }

    @Override
    public Double getPositiveMood() {
        if (!initCommunicationService()) {
            return 0d;
        }
        return service.getPositiveMood();
    }

    @Override
    public Double getNegativeMood() {
        if (!initCommunicationService()) {
            return 0d;
        }
        return service.getNegativeMood();
    }

    @Override
    public Boolean isInitialized() {
        if (!initCommunicationService()) {
            return false;
        }
        return service.isInitialized();
    }

    private boolean initCommunicationService() {

        if (service != null) return true;

        for (IComponentIdentifier cid: engine.getEmotionalOtherIds()) {
            if (cid.getLocalName().equals(agentName)) {
                service = SServiceProvider.getService(access.getExternalAccess(), cid, ICommunicationService.class).get();
                return true;
            }
        }
        return false;
    }
}
