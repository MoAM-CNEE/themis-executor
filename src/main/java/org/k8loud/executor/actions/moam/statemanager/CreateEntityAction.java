package org.k8loud.executor.actions.moam.statemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.CustomException;
import org.k8loud.executor.moam.statemanager.StateManagerService;
import org.k8loud.executor.model.Params;

import java.util.Map;

import static org.k8loud.executor.exception.code.ActionExceptionCode.PARSING_PARAMS_FAILURE;

@Slf4j
public class CreateEntityAction extends StateManagerAction {
    private EntityType type;
    private Map<String, Object> definition;

    public CreateEntityAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params, stateManagerService);
    }

    @Builder
    public CreateEntityAction(StateManagerService stateManagerService, EntityType type, String definition) {
        this(stateManagerService, type, parseDefinition(definition));
    }

    @Builder
    public CreateEntityAction(StateManagerService stateManagerService, EntityType type, Map<String, Object> definition) {
        super(stateManagerService);
        this.stateManagerService = stateManagerService;
        this.type = type;
        this.definition = definition;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        type = EntityType.fromName(params.getRequiredParam("type"));
        definition = parseDefinition(params.getRequiredParam("definition"));
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return stateManagerService.createEntity(type, definition);
    }

    private static Map<String, Object> parseDefinition(String definitionJsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(definitionJsonString, Map.class);
        } catch (Exception e) {
            log.error(PARSING_PARAMS_FAILURE.name());
            return null;
        }
    }
}
