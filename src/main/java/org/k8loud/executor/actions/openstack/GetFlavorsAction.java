package org.k8loud.executor.actions.openstack;

import lombok.Builder;
import org.jetbrains.annotations.Nullable;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.CustomException;
import org.k8loud.executor.model.Params;
import org.k8loud.executor.openstack.OpenstackService;

import java.util.Map;
import java.util.Set;

public class GetFlavorsAction extends OpenstackAction {
    private String region;
    private @Nullable Set<String> idsFilter;

    public GetFlavorsAction(Params params, OpenstackService openstackService) throws ActionException {
        super(params, openstackService);
    }

    @Builder
    public GetFlavorsAction(OpenstackService openstackService, String region, @Nullable Set<String> idsFilter)
            throws ActionException {
        super(openstackService);
        this.region = region;
        this.idsFilter = idsFilter;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        region = params.getRequiredParam("region");
        idsFilter = params.getOptionalParamAsSetOfStrings("idsFilter", null);
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return openstackService.getFlavors(region, idsFilter);
    }
}
