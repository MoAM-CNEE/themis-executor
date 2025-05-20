package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CreateEntityActionRQ {
    private int changeId;
    private String type;
    private Map<String, Object> definition;

    public CreateEntityActionRQ(int changeId, String type, Map<String, Object> definition) {
        this.changeId = changeId;
        this.type = type;
        this.definition = definition;
    }
}
