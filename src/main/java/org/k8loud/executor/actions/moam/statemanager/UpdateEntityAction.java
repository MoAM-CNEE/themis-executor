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
    private String query;
    private Map<String, String> lambdas;

    public UpdateEntityAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params, stateManagerService);
    }

    @Builder
    public UpdateEntityAction(StateManagerService stateManagerService, String query, String lambdas) {
        this(stateManagerService, query, parseLambdas(lambdas));
    }

    @Builder
    public UpdateEntityAction(StateManagerService stateManagerService, String query, Map<String, String> lambdas) {
        super(stateManagerService);
        this.query = query;
        this.lambdas = lambdas;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        this.query = params.getRequiredParam("query");
        this.lambdas = parseLambdas(params.getRequiredParam("lambdas"));
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return stateManagerService.updateEntity(query, lambdas);
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
