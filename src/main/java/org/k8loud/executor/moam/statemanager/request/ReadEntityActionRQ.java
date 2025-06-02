package org.k8loud.executor.moam.statemanager.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ReadEntityActionRQ {
    private String query;

    public ReadEntityActionRQ(String query) {
        this.query = query;
    }
}
