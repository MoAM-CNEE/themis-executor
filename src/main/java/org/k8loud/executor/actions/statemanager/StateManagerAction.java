package org.k8loud.executor.actions.statemanager;

import lombok.AllArgsConstructor;
import org.k8loud.executor.actions.Action;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.model.Params;
import org.k8loud.executor.statemanager.StateManagerService;

@AllArgsConstructor
public abstract class StateManagerAction extends Action {
    protected StateManagerService stateManagerService;

    public StateManagerAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params);
        this.stateManagerService = stateManagerService;
    }
}
