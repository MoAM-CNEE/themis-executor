package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteEntityActionRQ {
    private int changeId;
    private String query;
    private boolean triggerMirrorManager;

    public DeleteEntityActionRQ(int changeId, String query, boolean triggerMirrorManager) {
        this.changeId = changeId;
        this.query = query;
        this.triggerMirrorManager = triggerMirrorManager;
    }
}
