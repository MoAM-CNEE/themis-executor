package org.k8loud.executor.actions.cnapp.message;

import org.k8loud.executor.cnapp.message.MessageService;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.CustomException;
import org.k8loud.executor.model.Params;

import java.util.Map;

public class CreateMessageAction extends MessageAction {
    private String key;
    private String content;

    public CreateMessageAction(Params params, MessageService messageService) throws ActionException {
        super(params, messageService);
    }

    @Override
    protected void unpackAdditionalParams(Params params) {
        key = params.getRequiredParam("key");
        content = params.getRequiredParam("content");
    }

    @Override
    protected Map<String, Object> executeBody() throws CustomException {
        return messageService.createMessage(applicationUrl, key, content);
    }
}
