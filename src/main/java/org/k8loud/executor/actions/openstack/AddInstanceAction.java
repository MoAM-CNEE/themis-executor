package org.k8loud.executor.actions.openstack;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.OpenstackException;
import org.k8loud.executor.exception.ValidationException;
import org.k8loud.executor.model.Params;
import org.k8loud.executor.openstack.OpenstackService;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode
public class AddInstanceAction extends OpenstackAction {
    private String region;
    private String name;
    private String imageId;
    private String flavorId;
    private String keypairName;
    private String securityGroup;
    private List<String> networkIds;
    private String userData;
    private int count;
    private int waitActiveSec;

    public AddInstanceAction(Params params, OpenstackService openstackService) throws ActionException {
        super(params, openstackService);
    }

    @Builder
    public AddInstanceAction(OpenstackService openstackService,
                             String region, String name, String imageId, String flavorId, String keypairName,
                             String securityGroup, List<String> networkIds, String userData, int count, int waitActiveSec) {
        super(openstackService);
        this.region = region;
        this.name = name;
        this.imageId = imageId;
        this.flavorId = flavorId;
        this.keypairName = keypairName;
        this.securityGroup = securityGroup;
        this.networkIds = networkIds;
        this.userData = userData;
        this.count = count;
        this.waitActiveSec = waitActiveSec;
    }

    @Override
    public void unpackParams(Params params) {
        region = params.getRequiredParam("region");
        name = params.getRequiredParam("name");
        imageId = params.getRequiredParam("imageId");
        flavorId = params.getRequiredParam("flavorId");
        keypairName = params.getOptionalParam("keypairName", "default");
        securityGroup = params.getOptionalParam("securityGroup", null);
        networkIds = params.getOptionalParamAsListOfStrings("networkIds", List.of());
        userData = params.getOptionalParam("userData", null);
        count = params.getOptionalParamAsInt("count", 1);
        waitActiveSec = params.getOptionalParamAsInt("waitActiveSec", 300);
    }

    @Override
    protected Map<String, Object> executeBody() throws OpenstackException, ValidationException {
        return openstackService.createServers(region, name, imageId, flavorId, keypairName, securityGroup, networkIds,
                userData, count, waitActiveSec);
    }

}
