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
    private EntityType type;
    private String filterBy;

    public DeleteEntityAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params, stateManagerService);
    }


    @Builder
    public DeleteEntityAction(StateManagerService stateManagerService, EntityType type, String filterBy) {
        super(stateManagerService);
        this.stateManagerService = stateManagerService;
        this.type = type;
        this.filterBy = filterBy;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        type = EntityType.fromName(params.getRequiredParam("type"));
        filterBy = params.getRequiredParam("filterBy");
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return stateManagerService.deleteEntity(type, filterBy);
    }
}
