package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class UpdateEntityActionRQ {
    private int changeId;
    private String query;
    private Map<String, String> lambdas;
    private boolean triggerMirrorManager;

    public UpdateEntityActionRQ(int changeId, String query, Map<String, String> lambdas, boolean triggerMirrorManager) {
        this.changeId = changeId;
        this.query = query;
        this.lambdas = lambdas;
        this.triggerMirrorManager = triggerMirrorManager;
    }
}
