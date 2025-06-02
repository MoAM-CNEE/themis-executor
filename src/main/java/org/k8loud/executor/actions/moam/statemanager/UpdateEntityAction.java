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
public class UpdateEntityAction extends StateManagerAction {
    private String filterBy;
    private Map<String, String> lambdas;

    public UpdateEntityAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params, stateManagerService);
    }

    @Builder
    public UpdateEntityAction(StateManagerService stateManagerService, String filterBy, String lambdas) {
        this(stateManagerService, filterBy, parseLambdas(lambdas));
    }

    @Builder
    public UpdateEntityAction(StateManagerService stateManagerService, String filterBy, Map<String, String> lambdas) {
        super(stateManagerService);
        this.filterBy = filterBy;
        this.lambdas = lambdas;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        this.filterBy = params.getRequiredParam("filterBy");
        this.lambdas = parseLambdas(params.getRequiredParam("lambdas"));
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return stateManagerService.updateEntity(filterBy, lambdas);
    }

    private static Map<String, String> parseLambdas(String lambdasJsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(lambdasJsonString, Map.class);
        } catch (Exception e) {
            log.error(PARSING_PARAMS_FAILURE.name());
            return null;
        }
    }
}
