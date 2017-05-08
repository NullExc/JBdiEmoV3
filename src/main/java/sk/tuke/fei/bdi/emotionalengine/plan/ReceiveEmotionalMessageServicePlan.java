package sk.tuke.fei.bdi.emotionalengine.plan;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.component.IProvidedServicesFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IResultListener;
import sk.tuke.fei.bdi.emotionalengine.res.R;
import sk.tuke.fei.bdi.emotionalengine.service.ICommunicationService;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

/**
 * @author Peter Zemianek
 */

@Plan
public class ReceiveEmotionalMessageServicePlan {

    @PlanAPI
    private IPlan plan;

    private IInternalAccess agent;

    private IComponentIdentifier cid;

    ICommunicationService service;

    public ReceiveEmotionalMessageServicePlan(IInternalAccess agent) {

        this.agent = agent;

        service = (ICommunicationService) agent.getComponentFeature(IProvidedServicesFeature.class)
                .getProvidedService(R.MESSAGE_SERVICE);

        service.initialize(new Future<>(agent), agent.getComponentIdentifier()).addResultListener(new IResultListener<Void>() {
            @Override
            public void exceptionOccurred(Exception exception) {
                exception.printStackTrace();
            }

            @Override
            public void resultAvailable(Void result) {
                JBDIEmo.MessageListeners.add(agent.getComponentIdentifier());
            }
        });


    }


    @PlanBody
    public void body() {

    }

}
