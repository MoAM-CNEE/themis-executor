package org.k8loud.executor.moam.statemanager;

import org.k8loud.executor.exception.HTTPException;

import java.util.Map;

public interface StateManagerService {
    Map<String, Object> createEntity(Map<String, Object> definition, boolean triggerMirrorManager) throws HTTPException;

    Map<String, Object> updateEntity(String query, Map<String, String> lambdas, boolean triggerMirrorManager) throws HTTPException;

    Map<String, Object> deleteEntity(String query, boolean triggerMirrorManager) throws HTTPException;

    Map<String, Object> readEntity(String query) throws HTTPException;
}
