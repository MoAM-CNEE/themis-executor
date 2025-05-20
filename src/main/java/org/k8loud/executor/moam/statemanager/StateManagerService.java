package org.k8loud.executor.moam.statemanager;

import org.k8loud.executor.actions.moam.statemanager.EntityType;
import org.k8loud.executor.exception.HTTPException;

import java.util.Map;

public interface StateManagerService {
    Map<String, Object> createEntity(EntityType type, Map<String, Object> definition) throws HTTPException;

    Map<String, Object> updateEntity(EntityType type, String filterBy, Map<String, String> lambdas) throws HTTPException;

    Map<String, Object> deleteEntity(EntityType type, String filterBy) throws HTTPException;
}
