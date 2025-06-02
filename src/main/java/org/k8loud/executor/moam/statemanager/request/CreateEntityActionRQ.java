package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CreateEntityActionRQ {
    private int changeId;
    private Map<String, Object> definition;
    private boolean triggerMirrorManager;

    public CreateEntityActionRQ(int changeId, Map<String, Object> definition, boolean triggerMirrorManager) {
        this.changeId = changeId;
        this.definition = definition;
        this.triggerMirrorManager = triggerMirrorManager;
    }
}
