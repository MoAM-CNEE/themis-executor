package org.k8loud.executor.actions.moam.statemanager;

import lombok.AllArgsConstructor;
import org.k8loud.executor.actions.moam.MoAMAction;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.moam.statemanager.StateManagerService;
import org.k8loud.executor.model.Params;

@AllArgsConstructor
public abstract class StateManagerAction extends MoAMAction {
    protected StateManagerService stateManagerService;

    public StateManagerAction(Params params, StateManagerService stateManagerService) throws ActionException {
        super(params);
        this.stateManagerService = stateManagerService;
    }
}
