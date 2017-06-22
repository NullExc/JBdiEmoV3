package sk.tuke.fei.bdi.emotionalengine.component.engineinitialization;

import jadex.bridge.IInternalAccess;
import jadex.bridge.service.component.IRequiredServicesFeature;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.plan.InitializeEmotionalEnginePlan;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   ---------------------------
   16. 02. 2013
   10:19 AM

*/

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import sk.tuke.fei.bdi.emotionalengine.res.R;

import java.util.Set;

/**
 * @author Tomáš Herich
 * @author Peter Zemianek
 */

public class PlatformOtherMapper implements Runnable {

    private Set<String> emotionalOtherNames;

    private boolean isRunning = false;
    private final IInternalAccess access;
    private final Engine engine;

    public PlatformOtherMapper(Engine engine, IInternalAccess access) {
        this.engine = engine;
        this.access = access;

        new Thread(this).start();
    }

    public void run() {

        while (true) {

            if (isRunning()) {

                IComponentManagementService cms = (IComponentManagementService) access.getComponentFeature(IRequiredServicesFeature.class).getRequiredService(R.COMPONENT_SERVICE).get();

                IFuture<IComponentIdentifier[]> identifiersFuture = cms.getComponentIdentifiers();

                IComponentIdentifier[] identifiers = identifiersFuture.get();

                engine.getEmotionalOtherIds().clear();

                for (IComponentIdentifier cid : identifiers) {

                    String componentName = cid.getLocalName();

                    boolean isComponentEmotionalOther = false;

                    for (String emotionalOtherName : getEmotionalOtherNames()) {

                        if (componentName.matches(emotionalOtherName + ".*")) {
                          //  System.out.println(emotionalOtherName + componentName);
                            isComponentEmotionalOther = true;
                        }
                    }

                    if (isComponentEmotionalOther) {

                        if (!access.getComponentIdentifier().getName().equals(cid.getName()) ) {

                            engine.getEmotionalOtherIds().add(cid);

                        }
                    }
                }
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("sleep exception: " + e.getMessage());
            }
        }
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
