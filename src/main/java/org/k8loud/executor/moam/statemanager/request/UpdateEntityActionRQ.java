package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;
import org.k8loud.executor.actions.moam.statemanager.EntityType;

import java.util.Map;

@Setter
@Getter
public class UpdateEntityActionRQ {
    private int changeId;
    private EntityType type;
    private String filterBy;
    private Map<String, String> lambdas;

    public UpdateEntityActionRQ(int changeId, EntityType type, String filterBy, Map<String, String> lambdas) {
        this.changeId = changeId;
        this.type = type;
        this.filterBy = filterBy;
        this.lambdas = lambdas;
    }
}
