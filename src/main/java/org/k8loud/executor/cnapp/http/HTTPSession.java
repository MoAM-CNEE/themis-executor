package org.k8loud.executor.cnapp.http;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.k8loud.executor.exception.HTTPException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

import static org.k8loud.executor.exception.code.HTTPExceptionCode.*;

@Slf4j
public class HTTPSession {
    private static final String ENCODING = "UTF-8";
    private final HttpClient httpClient;

    public HTTPSession() {
        httpClient = HttpClientBuilder.create().build();
    }

    public HttpResponse doGet(String urlBase, String endpoint) throws HTTPException {
        return doGet(urlBase, endpoint, Collections.emptyMap());
    }

    public HttpResponse doGet(String urlBase, String endpoint, Map<String, String> headers) throws HTTPException {
        final String url = getUrl(urlBase, endpoint);
        HttpGet request = new HttpGet(url);
        for (var header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }
        return sendRequest(request);
    }

    public HttpResponse doGet(String urlBase, String endpoint, Object paramsObj) throws HTTPException {
        return doGet(urlBase, endpoint, paramsObj, FieldNamingPolicy.IDENTITY);
    }

    public HttpResponse doGet(String urlBase, String endpoint, Object paramsObj, FieldNamingPolicy fieldNamingPolicy)
            throws HTTPException {
        HttpEntityEnclosingRequestBase httpGetWithBody = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return "GET";
            }
        };
        httpGetWithBody.setURI(URI.create(getUrl(urlBase, endpoint)));
        return doEntityRequest(httpGetWithBody, paramsObj, fieldNamingPolicy);
    }

    public HttpResponse doPost(String urlBase, String endpoint, Object paramsObj) throws HTTPException {
        return doPost(urlBase, endpoint, paramsObj, FieldNamingPolicy.IDENTITY);
    }

    public HttpResponse doPost(String urlBase, String endpoint, Object paramsObj,
                               FieldNamingPolicy fieldNamingPolicy) throws HTTPException {
        return doEntityRequest(new HttpPost(getUrl(urlBase, endpoint)), paramsObj, fieldNamingPolicy);
    }

    public HttpResponse doPut(String urlBase, String endpoint, Object paramsObj) throws HTTPException {
        return doPut(urlBase, endpoint, paramsObj, FieldNamingPolicy.IDENTITY);
    }

    public HttpResponse doPut(String urlBase, String endpoint, Object paramsObj,
                              FieldNamingPolicy fieldNamingPolicy) throws HTTPException {
        return doEntityRequest(new HttpPut(getUrl(urlBase, endpoint)), paramsObj, fieldNamingPolicy);
    }

    public HttpResponse doDelete(String urlBase, String endpoint) throws HTTPException {
        final String url = getUrl(urlBase, endpoint);
        HttpDelete request = new HttpDelete(url);
        return sendRequest(request);
    }

    public HttpResponse doDelete(String urlBase, String endpoint, Object paramsObj) throws HTTPException {
        return doDelete(urlBase, endpoint, paramsObj, FieldNamingPolicy.IDENTITY);
    }

    public HttpResponse doDelete(String urlBase, String endpoint, Object paramsObj,
                                 FieldNamingPolicy fieldNamingPolicy) throws HTTPException {
        HttpEntityEnclosingRequestBase httpDeleteWithBody = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return "DELETE";
            }
        };
        httpDeleteWithBody.setURI(URI.create(getUrl(urlBase, endpoint)));
        return doEntityRequest(httpDeleteWithBody, paramsObj, fieldNamingPolicy);
    }

    private HttpResponse doEntityRequest(HttpEntityEnclosingRequestBase request, Object paramsObj,
                                         FieldNamingPolicy fieldNamingPolicy) throws HTTPException {
        try {
            StringEntity params = new StringEntity(new GsonBuilder()
                    .setFieldNamingPolicy(fieldNamingPolicy)
                    .create()
                    .toJson(paramsObj));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            return sendRequest(request);
        } catch (UnsupportedEncodingException e) {
            throw new HTTPException(e, UNSUPPORTED_ENCODING);
        }
    }

    public HttpResponse sendRequest(HttpRequestBase request) throws HTTPException {
        try {
            log.info("Sending {}", request);
            return httpClient.execute(request);
        } catch (IOException e) {
            throw new HTTPException(e, HTTP_REQUEST_FAILED_TO_COMPLETE);
        }
    }

    // Not used for now but it will eventually be
    private String convertObjectToUrlParams(Object paramsObj) throws HTTPException {
        Class<?> clazz = paramsObj.getClass();
        StringBuilder params = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                final boolean isFieldAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    Object value = field.get(paramsObj);
                    if (value != null) {
                        if (params.length() > 0) {
                            params.append("&");
                        }
                        params.append(URLEncoder.encode(field.getName(), ENCODING))
                                .append("=")
                                .append(URLEncoder.encode(value.toString(), ENCODING));
                    }
                } catch (IllegalAccessException | UnsupportedEncodingException e) {
                    throw new HTTPException(e, CONVERT_PARAMS_OBJECT_TO_URL_PARAMS_FAILURE);
                }
                field.setAccessible(isFieldAccessible);
            }
        }
        return params.toString();
    }

    private String getUrl(String urlBase, String endpoint) {
        return String.format("%s/%s", urlBase, endpoint);
    }
}
