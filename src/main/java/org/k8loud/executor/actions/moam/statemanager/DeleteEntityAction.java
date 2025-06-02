package org.k8loud.executor.actions.moam.statemanager;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.CustomException;
import org.k8loud.executor.moam.statemanager.StateManagerService;
import org.k8loud.executor.model.Params;

import java.util.Map;

@Slf4j
public class DeleteEntityAction extends StateManagerAction {
    private String query;
    private boolean triggerMirrorManager;

    public DeleteEntityAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params, stateManagerService);
    }

    @Builder
    public DeleteEntityAction(StateManagerService stateManagerService, String query, Boolean triggerMirrorManager) {
        super(stateManagerService);
        this.stateManagerService = stateManagerService;
        this.query = query;
        this.triggerMirrorManager = triggerMirrorManager != null ? triggerMirrorManager : true;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        query = params.getRequiredParam("query");
        triggerMirrorManager = params.getOptionalParamAsBoolean("triggerMirrorManager", "true");
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return stateManagerService.deleteEntity(query, triggerMirrorManager);
    }
}
