package org.k8loud.executor.statemanager;

import org.k8loud.executor.actions.statemanager.EntityType;
import org.k8loud.executor.exception.HTTPException;

import java.util.Map;

public interface StateManagerService {
    Map<String, Object> createEntity(EntityType type, String definition) throws HTTPException;
}
