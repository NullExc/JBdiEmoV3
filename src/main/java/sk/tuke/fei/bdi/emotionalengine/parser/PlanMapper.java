package sk.tuke.fei.bdi.emotionalengine.parser;

import jadex.bdiv3.model.BDIModel;
import jadex.bdiv3.model.MPlan;
import jadex.bridge.IInternalAccess;

/**
 * @author Peter Zemianek
 */
public class PlanMapper {

    private final IInternalAccess access;

    public PlanMapper(IInternalAccess access) {
        this.access = access;
    }

    public void mapPlans() {
        BDIModel model = (BDIModel) access.getExternalAccess().getModel().getRawModel();

        MPlan plan;



    }


}
