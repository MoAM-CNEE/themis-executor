package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteEntityActionRQ {
    private int changeId;
    private String filterBy;

    public DeleteEntityActionRQ(int changeId, String filterBy) {
        this.changeId = changeId;
        this.filterBy = filterBy;
    }
}
