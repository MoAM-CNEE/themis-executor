package org.k8loud.executor.actions.statemanager;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.CustomException;
import org.k8loud.executor.model.Params;
import org.k8loud.executor.statemanager.StateManagerService;

import java.util.Map;

public class CreateEntityAction extends StateManagerAction {
    private EntityType type;
    private String definition;

    public CreateEntityAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params, stateManagerService);
    }

    @Builder
    public CreateEntityAction(StateManagerService stateManagerService, EntityType type, String definition) {
        super(stateManagerService);
        this.stateManagerService = stateManagerService;
        this.type = type;
        this.definition = definition;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        type = EntityType.fromRepresentation(params.getRequiredParam("type"));
        definition = params.getRequiredParam("definition");
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return stateManagerService.createEntity(type, definition);
    }
}
