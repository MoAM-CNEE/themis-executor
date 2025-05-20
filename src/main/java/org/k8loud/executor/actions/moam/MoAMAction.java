package org.k8loud.executor.actions.moam;

import lombok.AllArgsConstructor;
import org.k8loud.executor.actions.Action;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.model.Params;

@AllArgsConstructor
public abstract class MoAMAction extends Action {
    // Aggregates MoAM actions

    protected MoAMAction(Params params) throws ActionException {
        super(params);
    }
}
