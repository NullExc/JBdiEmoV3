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
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;
import sk.tuke.fei.bdi.emotionalengine.starter.JadexStarter;

import java.util.Set;

public class PlatformOtherMapper implements Runnable {

    private IComponentManagementService cms;
    private Engine engine;
    private Set<String> emotionalOtherNames;
    private InitializeEmotionalEnginePlan parentPlan;
    private boolean isRunning = false;
    private final IInternalAccess access;

    public PlatformOtherMapper(Class agent, Engine engine, IInternalAccess access) {

        //this.parentPlan = (InitializeEmotionalEnginePlan) parentPlan;
        this.engine = engine;
        this.access = access;

        new Thread(this).start();

    }

    public void run() {

        while (true) {

            // Don't run search before initialize emotional engine plan mapped possible emotional other names
            if (isRunning) {

                IComponentManagementService cms = (IComponentManagementService) access.getComponentFeature(IRequiredServicesFeature.class).getRequiredService("CMS").get();

                IFuture<IComponentIdentifier[]> identifiersFuture = cms.getComponentIdentifiers();

                IComponentIdentifier[] identifiers = identifiersFuture.get();

                // Remove identifiers stored in emotional engine
                engine.getEmotionalOtherIds().clear();

                // Iterate identifiers
                for (IComponentIdentifier cid : identifiers) {
                    // Get first part of component name (discard platform information)
                    String componentName = cid.getLocalName();
                    //String componentName = cid.getName().split("\\@")[0];

                    boolean isComponentEmotionalOther = false;

                    //Test for every emotional other defined as parameter in agent ADF (initialize_emotions_plan)
                    for (String emotionalOtherName : emotionalOtherNames) {
                        //Condition that ensures that agent can have emotional others of the same type as himself
                        //if (componentName.equals(emotionalOtherName)) {
                        if (componentName.matches(emotionalOtherName + ".*")) {
                            isComponentEmotionalOther = true;
                        }
                    }

                    if (isComponentEmotionalOther) {

                        //Condition that ensures that agent will not add himself as emotional other
                        if (!access.getComponentIdentifier().getName().equals(cid.getName()) ) {

                            // Add identifier of emotional other to emotional engine
                            System.err.println("Adding emotional other cid : " + cid.getLocalName() + " from : " + access.getComponentIdentifier().getLocalName());
                            engine.getEmotionalOtherIds().add(cid);

                        }
                    }
                }
            }

            // Check platform for possible emotional other agents every 10 seconds
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
