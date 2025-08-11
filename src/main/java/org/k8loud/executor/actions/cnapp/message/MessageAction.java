package org.k8loud.executor.actions.cnapp.message;

import lombok.AllArgsConstructor;
import org.k8loud.executor.actions.cnapp.CNAppAction;
import org.k8loud.executor.cnapp.message.MessageService;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.model.Params;

@AllArgsConstructor
public abstract class MessageAction extends CNAppAction {
    protected MessageService messageService;
    protected String applicationUrl;

    public MessageAction(Params params, MessageService messageService) throws ActionException {
        super(params);
        this.messageService = messageService;
    }

    @Override
    public void unpackParams(Params params) {
        applicationUrl = params.getRequiredParam("applicationUrl");
        unpackAdditionalParams(params);
    }

    protected abstract void unpackAdditionalParams(Params params);
}
