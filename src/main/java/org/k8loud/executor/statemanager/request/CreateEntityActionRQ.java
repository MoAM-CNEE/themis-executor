package org.k8loud.executor.statemanager.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateEntityActionRQ {
    private int change_id;
    private String type;
    private String definition;

    public CreateEntityActionRQ(int change_id, String type, String definition) {
        this.change_id = change_id;
        this.type = type;
        this.definition = definition;
    }
}
