package org.k8loud.executor.cnapp.sockshop;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.k8loud.executor.cnapp.http.HTTPService;
import org.k8loud.executor.cnapp.http.HTTPSession;
import org.k8loud.executor.cnapp.mail.MailService;
import org.k8loud.executor.datastorage.DataStorageService;
import org.k8loud.executor.exception.HTTPException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.k8loud.executor.exception.code.HTTPExceptionCode.FAILED_TO_CONVERT_RESPONSE_ENTITY;
import static org.k8loud.executor.exception.code.HTTPExceptionCode.HTTP_RESPONSE_STATUS_CODE_NOT_SUCCESSFUL;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SockShopBaseTest {
    protected static final String APPLICATION_URL = "http://localhost:8082";
    protected static final String SOCKSHOP_LOGIN_ENDPOINT = "login";
    protected static final String REGISTER_USER_ENDPOINT = "register";
    protected static final String SOCKSHOP_CUSTOMERS_ENDPOINT = "customers";
    protected static final String SOCKSHOP_ADDRESSES_ENDPOINT = "addresses";

    protected static final String USERNAME = "user994";
    protected static final String PASSWORD = "pass994";
    protected static final String ID = "655b6dfacb8de600019db115";
    protected static final int UNSUCCESSFUL_RESPONSE_STATUS_CODE = 500;
    protected static final String RESPONSE_CONTENT = "responseContentVal";

    @Mock
    SockShopProperties sockShopPropertiesMock;
    @Mock
    HTTPService httpServiceMock;
    @Mock
    HTTPSession httpSessionMock;
    @Mock
    HttpResponse successfulResponseMock;
    @Mock
    HttpResponse unsuccessfulResponseMock;
    @Mock
    MailService mailServiceMock;
    @Mock
    DataStorageService dataStorageServiceMock;

    SockShopServiceImpl sockShopService;

    @BeforeEach
    public void setUp() throws IOException, HTTPException {
        sockShopService = new SockShopServiceImpl(sockShopPropertiesMock, httpServiceMock, mailServiceMock,
                dataStorageServiceMock);
        // Copy-paste implementations from HTTPServiceImpl
        doAnswer(i -> {
            int statusCode = i.getArgument(0);
            return 200 <= statusCode && statusCode < 300;
        }).when(httpServiceMock).isStatusCodeSuccessful(anyInt());
        doAnswer(i -> {
            HttpResponse response = i.getArgument(0);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (!httpServiceMock.isStatusCodeSuccessful(statusCode)) {
                throw new HTTPException(String.valueOf(statusCode), HTTP_RESPONSE_STATUS_CODE_NOT_SUCCESSFUL);
            }
            try {
                return httpServiceMock.getResponseEntityAsString(response);
            } catch (IOException e) {
                throw new HTTPException(e.toString(), FAILED_TO_CONVERT_RESPONSE_ENTITY);
            }
        }).when(httpServiceMock).handleResponse(any(HttpResponse.class));
        when(httpServiceMock.createSession()).thenReturn(httpSessionMock);
        additionalSetUp();
    }

    protected void additionalSetUp() throws HTTPException, IOException {
        // empty
    }

    protected void assertResponseContent(Map<String, Object> resultMap) {
        assertEquals(RESPONSE_CONTENT, resultMap.get("responseContent"));
    }

    protected void mockAuth() throws HTTPException, IOException {
        when(sockShopPropertiesMock.getLoginUserEndpoint()).thenReturn(SOCKSHOP_LOGIN_ENDPOINT);
        when(httpSessionMock.doGet(eq(APPLICATION_URL), eq(SOCKSHOP_LOGIN_ENDPOINT), any(Map.class)))
                .thenReturn(successfulResponseMock);
        mockSuccessfulResponse();
    }

    protected void mockSuccessfulResponse() throws IOException {
        mockResponse(successfulResponseMock, 200);
        // Copy-paste implementation from HTTPServiceImpl
        doAnswer(i -> {
            HttpResponse response = i.getArgument(0);
            return EntityUtils.toString(response.getEntity());
        }).when(httpServiceMock).getResponseEntityAsString(any(HttpResponse.class));
    }

    protected void mockUnsuccessfulResponse() {
        mockResponse(unsuccessfulResponseMock, UNSUCCESSFUL_RESPONSE_STATUS_CODE);
    }

    private void mockResponse(HttpResponse response, int statusCode) {
        StatusLine statusLineMock = mock(StatusLine.class);
        when(statusLineMock.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(statusLineMock);
        mockResponseContent(response);
    }

    // Recreating the input stream each time getEntity() is called is required; the input stream is consumed in our usage
    private void mockResponseContent(HttpResponse response) {
        lenient().doAnswer(i -> {
            BasicHttpEntity httpEntity = new BasicHttpEntity();
            InputStream is = new ByteArrayInputStream(RESPONSE_CONTENT.getBytes());
            httpEntity.setContent(is);
            return httpEntity;
        }).when(response).getEntity();
    }
}
