package org.k8loud.executor.cnapp.http;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.k8loud.executor.exception.HTTPException;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.k8loud.executor.exception.code.HTTPExceptionCode.FAILED_TO_CONVERT_RESPONSE_ENTITY;
import static org.k8loud.executor.exception.code.HTTPExceptionCode.HTTP_RESPONSE_STATUS_CODE_NOT_SUCCESSFUL;

@Service
public class HTTPServiceImpl implements HTTPService {
    @Override
    public HTTPSession createSession() {
        return new HTTPSession();
    }

    @Override
    public boolean isStatusCodeSuccessful(int statusCode) {
        return 200 <= statusCode && statusCode < 300;
    }

    @Override
    public String getResponseEntityAsString(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String handleResponse(HttpResponse response) throws HTTPException {
        final int statusCode = response.getStatusLine().getStatusCode();
        if (!isStatusCodeSuccessful(statusCode)) {
            throw new HTTPException(String.valueOf(statusCode), HTTP_RESPONSE_STATUS_CODE_NOT_SUCCESSFUL);
        }
        try {
            return getResponseEntityAsString(response);
        } catch (IOException e) {
            throw new HTTPException(e.toString(), FAILED_TO_CONVERT_RESPONSE_ENTITY);
        }
    }
}
