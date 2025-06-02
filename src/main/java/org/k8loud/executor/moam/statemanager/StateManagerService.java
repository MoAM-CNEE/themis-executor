package org.k8loud.executor.moam.statemanager;

import org.k8loud.executor.exception.HTTPException;

import java.util.Map;

public interface StateManagerService {
    Map<String, Object> createEntity(Map<String, Object> definition) throws HTTPException;

    Map<String, Object> updateEntity(String filterBy, Map<String, String> lambdas) throws HTTPException;

    Map<String, Object> deleteEntity(String filterBy) throws HTTPException;
}
