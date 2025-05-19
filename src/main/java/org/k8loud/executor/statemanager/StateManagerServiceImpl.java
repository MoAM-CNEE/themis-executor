package org.k8loud.executor.statemanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k8loud.executor.actions.statemanager.EntityType;
import org.k8loud.executor.exception.HTTPException;
import org.k8loud.executor.exception.StateManagerException;
import org.k8loud.executor.statemanager.request.CreateEntityActionRQ;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.k8loud.executor.exception.code.HTTPExceptionCode.*;
import static org.k8loud.executor.util.Util.resultMap;

@Slf4j
@Service
@AllArgsConstructor
public class StateManagerServiceImpl implements StateManagerService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final StateManagerProperties stateManagerProperties;

    @Override
    public Map<String, Object> createEntity(EntityType type, String definition) throws HTTPException {
        int changeId = ThreadLocalRandom.current().nextInt();
        CreateEntityActionRQ requestPayload = new CreateEntityActionRQ(changeId, type.getRepresentation(), definition);
        String requestBody = null;
        try {
            requestBody = OBJECT_MAPPER.writeValueAsString(requestPayload);
        } catch (JsonProcessingException e) {
            throw new HTTPException(e, OBJECT_MAPPER_ERROR);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(stateManagerProperties.getUrl() + stateManagerProperties.getEntityEndpoint()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = null;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HTTPException(e, HTTP_REQUEST_FAILED_TO_COMPLETE);
        }
        if (isNotSuccessful(response.statusCode())) {
            throw new HTTPException(String.valueOf(response.statusCode()), HTTP_RESPONSE_STATUS_CODE_NOT_SUCCESSFUL);
        }

        return resultMap(String.format("Change %d processed", changeId));
    }

    private boolean isNotSuccessful(int statusCode) {
        return statusCode < 200 || statusCode >= 300;
    }
}
