package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteEntityActionRQ {
    private int changeId;
    private String query;

    public DeleteEntityActionRQ(int changeId, String query) {
        this.changeId = changeId;
        this.query = query;
    }
}
