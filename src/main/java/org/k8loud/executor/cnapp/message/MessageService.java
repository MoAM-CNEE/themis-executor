package org.k8loud.executor.cnapp.message;

import org.k8loud.executor.exception.CNAppException;
import org.k8loud.executor.exception.HTTPException;
import org.k8loud.executor.exception.ValidationException;

import java.util.Map;

public interface MessageService {
    Map<String, Object> createMessage(String applicationUrl, String key, String content)
            throws CNAppException, ValidationException, HTTPException;

    Map<String, Object> updateMessage(String applicationUrl, String key, String content)
            throws CNAppException, ValidationException, HTTPException;

    Map<String, Object> getMessage(String applicationUrl, String key)
            throws CNAppException, ValidationException, HTTPException;

    Map<String, Object> deleteMessage(String applicationUrl, String key)
            throws CNAppException, ValidationException, HTTPException;
}
