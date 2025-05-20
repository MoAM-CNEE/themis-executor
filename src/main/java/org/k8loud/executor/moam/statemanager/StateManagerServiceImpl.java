package org.k8loud.executor.moam.statemanager;

import com.google.gson.FieldNamingPolicy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.k8loud.executor.actions.moam.statemanager.EntityType;
import org.k8loud.executor.cnapp.http.HTTPService;
import org.k8loud.executor.cnapp.http.HTTPSession;
import org.k8loud.executor.exception.HTTPException;
import org.k8loud.executor.moam.statemanager.request.CreateEntityActionRQ;
import org.k8loud.executor.moam.statemanager.request.UpdateEntityActionRQ;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.k8loud.executor.util.Util.resultMap;

@Slf4j
@Service
@AllArgsConstructor
public class StateManagerServiceImpl implements StateManagerService {
    private static final String ENTITY_CREATE = "/create";
    private static final String ENTITY_UPDATE = "/update";
    private final StateManagerProperties stateManagerProperties;
    private final HTTPService httpService;

    @Override
    public Map<String, Object> createEntity(EntityType type, Map<String, Object> definition) throws HTTPException {
        int changeId = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        CreateEntityActionRQ request = new CreateEntityActionRQ(changeId, type.getRepresentation(), definition);
        HTTPSession session = httpService.createSession();
        HttpResponse response = session.doPost(
                stateManagerProperties.getUrl(),
                stateManagerProperties.getEntityEndpoint() + ENTITY_CREATE,
                request,
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        );
        String responseText = httpService.handleResponse(response);
        return resultMap(String.format("Change %d (create) processed, response: %s", changeId, responseText));
    }

    @Override
    public Map<String, Object> updateEntity(EntityType type, String filterBy, Map<String, String> lambdas) throws HTTPException {
        int changeId = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        UpdateEntityActionRQ request = new UpdateEntityActionRQ(changeId, type, filterBy, lambdas);
        HTTPSession session = httpService.createSession();
        HttpResponse response = session.doPut(
                stateManagerProperties.getUrl(),
                stateManagerProperties.getEntityEndpoint() + ENTITY_UPDATE,
                request,
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        );
        String responseText = httpService.handleResponse(response);
        return resultMap(String.format("Change %d (update) processed, response: %s", changeId, responseText));
    }
}
