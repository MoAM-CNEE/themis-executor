package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class UpdateEntityActionRQ {
    private int changeId;
    private String filterBy;
    private Map<String, String> lambdas;

    public UpdateEntityActionRQ(int changeId, String filterBy, Map<String, String> lambdas) {
        this.changeId = changeId;
        this.filterBy = filterBy;
        this.lambdas = lambdas;
    }
}
