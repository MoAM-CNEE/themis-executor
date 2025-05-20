package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;
import org.k8loud.executor.actions.moam.statemanager.EntityType;

import java.util.Map;

@Setter
@Getter
public class CreateEntityActionRQ {
    private int changeId;
    private EntityType type;
    private Map<String, Object> definition;

    public CreateEntityActionRQ(int changeId, EntityType type, Map<String, Object> definition) {
        this.changeId = changeId;
        this.type = type;
        this.definition = definition;
    }
}
