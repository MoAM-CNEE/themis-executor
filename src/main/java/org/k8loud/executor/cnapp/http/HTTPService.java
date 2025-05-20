package org.k8loud.executor.cnapp.http;

import org.apache.http.HttpResponse;
import org.k8loud.executor.exception.HTTPException;

import java.io.IOException;

public interface HTTPService {
    HTTPSession createSession();

    boolean isStatusCodeSuccessful(int statusCode);

    String getResponseEntityAsString(HttpResponse response) throws IOException;

    String handleResponse(HttpResponse response) throws HTTPException;
}
