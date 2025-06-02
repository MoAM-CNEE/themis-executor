package org.k8loud.executor.actions.moam.statemanager;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.CustomException;
import org.k8loud.executor.moam.statemanager.StateManagerService;
import org.k8loud.executor.model.Params;

import java.util.Map;

@Slf4j
public class ReadEntityAction extends StateManagerAction {
    private String query;

    public ReadEntityAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params, stateManagerService);
    }


    @Builder
    public ReadEntityAction(StateManagerService stateManagerService, String query) {
        super(stateManagerService);
        this.stateManagerService = stateManagerService;
        this.query = query;
    }

    @Override
    public void unpackParams(Params params) throws ActionException {
        query = params.getRequiredParam("query");
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return stateManagerService.readEntity(query);
    }
}
