package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;
import org.k8loud.executor.actions.moam.statemanager.EntityType;

import java.util.Map;

@Setter
@Getter
public class DeleteEntityActionRQ {
    private int changeId;
    private EntityType type;
    private String filterBy;

    public DeleteEntityActionRQ(int changeId, EntityType type, String filterBy) {
        this.changeId = changeId;
        this.type = type;
        this.filterBy = filterBy;
    }
}
