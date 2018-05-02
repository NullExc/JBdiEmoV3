package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bdiv3.features.impl.IInternalBDIAgentFeature;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.starter.JBDIEmo;

import java.util.Set;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   16. 02. 2013
   10:19 AM

*/

/**
 *
 * Periodically looks for other agents in platform. If finds someone, then it adds its identifier into Engine.
 * This identifiers are used in communication process.
 *
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class PlatformOtherMapper {

    private Set<String> emotionalOtherNames;

    private boolean isRunning = false;
    private final Engine engine;
    private final IExecutionFeature executionFeature;

    public PlatformOtherMapper(IInternalAccess access) {
        this.engine = (Engine) access.getComponentFeature(IInternalBDIAgentFeature.class)
                .getBDIModel().getCapability().getBelief("engine").getValue(access);
        this.executionFeature = access.getComponentFeature(IExecutionFeature.class);

        map();
    }

    public void map() {

        executionFeature.repeatStep(0, 10000, new IComponentStep<Void>() {
            @Override
            public IFuture<Void> execute(IInternalAccess internalAccess) {

                if (isRunning()) {

                    IFuture<IComponentManagementService> cms = SServiceProvider.getService(JBDIEmo.PLATFORM, IComponentManagementService.class); // (IComponentManagementService) internalAccess.getComponentFeature(IRequiredServicesFeature.class).getRequiredService(R.COMPONENT_SERVICE).get();

                    IFuture<IComponentIdentifier[]> identifiersFuture = cms.get().getComponentIdentifiers();

                    IComponentIdentifier[] identifiers = identifiersFuture.get();

                    engine.getEmotionalOtherIds().clear();

                    for (IComponentIdentifier cid : identifiers) {

                        String componentName = cid.getLocalName();

                        boolean isComponentEmotionalOther = false;

                        for (String emotionalOtherName : getEmotionalOtherNames()) {

                            if (componentName.matches(emotionalOtherName)) {
                                isComponentEmotionalOther = true;
                            }
                        }

                        if (isComponentEmotionalOther) {
                            if (!internalAccess.getComponentIdentifier().getName().equals(cid.getName()) ) {
                                engine.getEmotionalOtherIds().add(cid);
                            }
                        }
                    }
                }
                return IFuture.DONE;
            }
        });
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public Set<String> getEmotionalOtherNames() {
        return emotionalOtherNames;
    }

    public void setEmotionalOtherNames(Set<String> emotionalOtherNames) {
        this.emotionalOtherNames = emotionalOtherNames;
    }

}
