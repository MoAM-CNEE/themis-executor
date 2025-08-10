package org.k8loud.executor.cnapp.message;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.k8loud.executor.cnapp.http.HTTPService;
import org.k8loud.executor.exception.CNAppException;
import org.k8loud.executor.exception.HTTPException;
import org.k8loud.executor.exception.ValidationException;
import org.k8loud.executor.util.annotation.ThrowExceptionAndLogExecutionTime;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.k8loud.executor.exception.code.CNAppExceptionCode.MESSAGE_NOT_EXISTS;
import static org.k8loud.executor.util.Util.resultMap;

@Slf4j
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageProperties messageProperties;
    private final HTTPService httpService;

    @Override
    @ThrowExceptionAndLogExecutionTime(exceptionClass = "CNAppException",
            exceptionCode = "MESSAGE_CREATE_FAILED")
    public Map<String, Object> createMessage(String applicationUrl, String key, String content) throws CNAppException, ValidationException, HTTPException {
        log.info("Creating message with key '{}' and content '{}'", key, content);
        HttpResponse response = httpService.createSession().doPost(applicationUrl,
                messageProperties.getMessageEndpoint() + "?key=" + key + "&content=" + content,
                null);
        String responseContent = httpService.handleResponse(response);
        return createResultMap(String.format("Created message with key '%s' and content '%s'", key, content), responseContent);
    }

    @Override
    @ThrowExceptionAndLogExecutionTime(exceptionClass = "CNAppException",
            exceptionCode = "MESSAGE_UPDATE_FAILED")
    public Map<String, Object> updateMessage(String applicationUrl, String key, String content) throws CNAppException, ValidationException, HTTPException {
        log.info("Updating message with key '{}' with content '{}'", key, content);
        HttpResponse getMessageResponse = httpService.createSession().doGet(applicationUrl,
                messageProperties.getMessageEndpoint() + "?key=" + key,
                (Object) null);
        if (getMessageResponse.getStatusLine().getStatusCode() != 200) {
            throw new CNAppException(MESSAGE_NOT_EXISTS);
        }
        HttpResponse response = httpService.createSession().doPost(applicationUrl,
                messageProperties.getMessageEndpoint() + "?key=" + key + "&content=" + content,
                null);
        String responseContent = httpService.handleResponse(response);
        return createResultMap(String.format("Created message with key '%s' and content '%s'", key, content), responseContent);
    }

    @Override
    @ThrowExceptionAndLogExecutionTime(exceptionClass = "CNAppException",
            exceptionCode = "MESSAGE_GET_FAILED")
    public Map<String, Object> getMessage(String applicationUrl, String key) throws CNAppException, ValidationException, HTTPException {
        log.info("Getting message with key '{}'", key);
        HttpResponse response = httpService.createSession().doGet(applicationUrl,
                messageProperties.getMessageEndpoint() + "?key=" + key,
                (Object) null);
        String responseContent = httpService.handleResponse(response);
        return createResultMap(String.format("Got message with key '%s'", key), responseContent);
    }

    @Override
    @ThrowExceptionAndLogExecutionTime(exceptionClass = "CNAppException",
            exceptionCode = "MESSAGE_DELETE_FAILED")
    public Map<String, Object> deleteMessage(String applicationUrl, String key) throws CNAppException, ValidationException, HTTPException {
        log.info("Deleting message with key '{}'", key);
        HttpResponse response = httpService.createSession().doDelete(applicationUrl,
                messageProperties.getMessageEndpoint() + "?key=" + key,
                null);
        String responseContent = httpService.handleResponse(response);
        return createResultMap(String.format("Deleted message with key '%s'", key), responseContent);
    }

    private Map<String, Object> createResultMap(String result, String responseContent) throws ValidationException {
        return resultMap(result, Map.of("responseContent", responseContent));
    }
}
