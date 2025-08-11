package org.k8loud.executor.actions.kubernetes;

import lombok.Builder;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.KubernetesException;
import org.k8loud.executor.kubernetes.KubernetesService;
import org.k8loud.executor.model.Params;

import java.util.Map;

public class GetReadinessAction extends KubernetesAction {
    private String resourceName;
    private String resourceType;
    private Long gracePeriodSeconds;

    public GetReadinessAction(Params params, KubernetesService kubernetesService) throws ActionException {
        super(params, kubernetesService);
    }

    @Builder
    public GetReadinessAction(KubernetesService kubernetesService, String namespace,
                              String resourceName, String resourceType, Long gracePeriodSeconds) {
        super(kubernetesService, namespace);
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.gracePeriodSeconds = gracePeriodSeconds;
    }

    @Override
    public void unpackAdditionalParams(Params params) {
        resourceName = params.getRequiredParam("resourceName");
        resourceType = params.getRequiredParam("resourceType");
        gracePeriodSeconds = params.getOptionalParamAsLong("gracePeriodSeconds", 0L);
    }

    @Override
    public Map<String, Object> executeBody() throws KubernetesException {
        return kubernetesService.getReadiness(namespace, resourceName, resourceType, gracePeriodSeconds);
    }
}
