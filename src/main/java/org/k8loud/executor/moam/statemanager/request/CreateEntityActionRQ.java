package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CreateEntityActionRQ {
    private int change_id;
    private String type;
    private Map<String, Object> definition;

    public CreateEntityActionRQ(int change_id, String type, Map<String, Object> definition) {
        this.change_id = change_id;
        this.type = type;
        this.definition = definition;
    }
}
