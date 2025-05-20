package org.k8loud.executor.moam.statemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.k8loud.executor.actions.moam.statemanager.EntityType;
import org.k8loud.executor.cnapp.http.HTTPService;
import org.k8loud.executor.cnapp.http.HTTPSession;
import org.k8loud.executor.exception.HTTPException;
import org.k8loud.executor.moam.statemanager.request.CreateEntityActionRQ;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.k8loud.executor.exception.code.HTTPExceptionCode.HTTP_RESPONSE_STATUS_CODE_NOT_SUCCESSFUL;
import static org.k8loud.executor.util.Util.resultMap;

@Slf4j
@Service
@AllArgsConstructor
public class StateManagerServiceImpl implements StateManagerService {
    private static final String ENTITY_CREATE = "/create";
    private final StateManagerProperties stateManagerProperties;
    private final HTTPService httpService;

    @Override
    public Map<String, Object> createEntity(EntityType type, Map<String, Object> definition) throws HTTPException {
        int changeId = ThreadLocalRandom.current().nextInt();
        CreateEntityActionRQ createEntityActionRQ = new CreateEntityActionRQ(changeId, type.getRepresentation(), definition);

        HTTPSession session = httpService.createSession();
        HttpResponse response = session.doPost(
                stateManagerProperties.getUrl(),
                stateManagerProperties.getEntityEndpoint() + ENTITY_CREATE,
                createEntityActionRQ
        );

        if (!httpService.isStatusCodeSuccessful(response.getStatusLine().getStatusCode())) {
            throw new HTTPException(
                    String.valueOf(response.getStatusLine().getStatusCode()),
                    HTTP_RESPONSE_STATUS_CODE_NOT_SUCCESSFUL
            );
        }

        return resultMap(String.format("Change %d processed", changeId));
    }
}
