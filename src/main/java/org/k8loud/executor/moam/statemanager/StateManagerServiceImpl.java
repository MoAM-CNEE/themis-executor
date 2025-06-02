package org.k8loud.executor.moam.statemanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.gson.FieldNamingPolicy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.k8loud.executor.cnapp.http.HTTPService;
import org.k8loud.executor.exception.HTTPException;
import org.k8loud.executor.moam.statemanager.request.CreateEntityActionRQ;
import org.k8loud.executor.moam.statemanager.request.DeleteEntityActionRQ;
import org.k8loud.executor.moam.statemanager.request.ReadEntityActionRQ;
import org.k8loud.executor.moam.statemanager.request.UpdateEntityActionRQ;
import org.k8loud.executor.moam.statemanager.response.ReadEntityActionRS;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.k8loud.executor.exception.code.HTTPExceptionCode.FAILED_TO_CONVERT_RESPONSE_ENTITY;
import static org.k8loud.executor.util.Util.resultMap;

@Slf4j
@Service
@AllArgsConstructor
public class StateManagerServiceImpl implements StateManagerService {
    private static final String ENTITY_CREATE = "/create";
    private static final String ENTITY_UPDATE = "/update";
    private static final String ENTITY_DELETE = "/delete";
    private static final String ENTITY_READ = "/read";
    private final StateManagerProperties stateManagerProperties;
    private final HTTPService httpService;

    @Override
    public Map<String, Object> createEntity(Map<String, Object> definition) throws HTTPException {
        int changeId = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        CreateEntityActionRQ request = new CreateEntityActionRQ(changeId, definition);
        HttpResponse response = httpService.createSession().doPost(
                stateManagerProperties.getUrl(),
                stateManagerProperties.getEntityEndpoint() + ENTITY_CREATE,
                request,
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        );
        String responseText = httpService.handleResponse(response);
        return resultMap(String.format("Change %d (create) processed, response: %s", changeId, responseText));
    }

    @Override
    public Map<String, Object> updateEntity(String query, Map<String, String> lambdas) throws HTTPException {
        int changeId = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        UpdateEntityActionRQ request = new UpdateEntityActionRQ(changeId, query, lambdas);
        HttpResponse response = httpService.createSession().doPut(
                stateManagerProperties.getUrl(),
                stateManagerProperties.getEntityEndpoint() + ENTITY_UPDATE,
                request,
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        );
        String responseText = httpService.handleResponse(response);
        return resultMap(String.format("Change %d (update) processed, response: %s", changeId, responseText));
    }

    @Override
    public Map<String, Object> deleteEntity(String query) throws HTTPException {
        int changeId = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        DeleteEntityActionRQ request = new DeleteEntityActionRQ(changeId, query);
        HttpResponse response = httpService.createSession().doDelete(
                stateManagerProperties.getUrl(),
                stateManagerProperties.getEntityEndpoint() + ENTITY_DELETE,
                request,
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        );
        String responseText = httpService.handleResponse(response);
        return resultMap(String.format("Change %d (delete) processed, response: %s", changeId, responseText));
    }

    @Override
    public Map<String, Object> readEntity(String query) throws HTTPException {
        ReadEntityActionRQ request = new ReadEntityActionRQ(query);
        HttpResponse response = httpService.createSession().doGet(
                stateManagerProperties.getUrl(),
                stateManagerProperties.getEntityEndpoint() + ENTITY_READ,
                request,
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        );
        String responseText = httpService.handleResponse(response);
        ReadEntityActionRS readEntityActionRS;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            readEntityActionRS = objectMapper.readValue(responseText, ReadEntityActionRS.class);
        } catch (JsonProcessingException e) {
            throw new HTTPException(e.toString(), FAILED_TO_CONVERT_RESPONSE_ENTITY);
        }
        return new HashMap<>(Map.of("entities", readEntityActionRS.getEntities()));
    }
}
