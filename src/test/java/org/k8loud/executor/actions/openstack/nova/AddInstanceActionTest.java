package org.k8loud.executor.actions.openstack.nova;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.k8loud.executor.actions.openstack.AddInstanceAction;
import org.k8loud.executor.actions.openstack.OpenstackActionBaseTest;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.OpenstackException;
import org.k8loud.executor.exception.ValidationException;
import org.k8loud.executor.model.ExecutionRS;
import org.k8loud.executor.model.Params;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AddInstanceActionTest extends OpenstackActionBaseTest {
    private static final String NAME = "name";
    private static final String SECURITY_GROUP = "sec-group";
    private static final String USER_DATA = "user-data";
    private static final String COUNT = "3";
    private static final String WAIT_ACTIVE = "120";
    private static final String NETWORK_IDS = "network-id-1,network-id-2";

    @ParameterizedTest
    @MethodSource
    void testSuccess(Params params) throws ActionException, OpenstackException, ValidationException {
        // given
        AddInstanceAction addInstanceAction = new AddInstanceAction(
                params, openstackServiceMock);
        when(openstackServiceMock.createServers(anyString(), anyString(), anyString(), anyString(),
                nullable(String.class), nullable(String.class), anyList(), nullable(String.class), anyInt(), anyInt()))
                .thenReturn(resultMap);

        // when
        ExecutionRS response = addInstanceAction.execute();

        // then
        String keypair = params.getOrDefault("keypairName", "default");
        String securityGroup = params.get("securityGroup");
        List<String> networkIds = params.getOptionalParamAsListOfStrings("networkIds", List.of());
        String userData = params.get("userData");
        int count = Integer.parseInt(params.getOrDefault("count", "1"));
        int waitActiveSec = Integer.parseInt(params.getOrDefault("waitActiveSec", "300"));
        verify(openstackServiceMock).createServers(eq(REGION), eq(NAME), eq(IMAGE_ID), eq(FLAVOR_ID), eq(keypair),
                eq(securityGroup), eq(networkIds), eq(userData), eq(count), eq(waitActiveSec));
        assertSuccessResponse(response);
    }

    private static Stream<Params> testSuccess() {
        return Stream.of(
                new Params(Map.of("region", REGION, "name", NAME, "imageId", IMAGE_ID, "flavorId", FLAVOR_ID)),
                new Params(Map.of("region", REGION, "name", NAME, "imageId", IMAGE_ID, "flavorId", FLAVOR_ID,
                        "securityGroup", SECURITY_GROUP)),
                new Params(
                        Map.of("region", REGION, "name", NAME, "imageId", IMAGE_ID, "flavorId", FLAVOR_ID,
                                "userData", USER_DATA)),
                new Params(Map.of("region", REGION, "name", NAME, "imageId", IMAGE_ID, "flavorId", FLAVOR_ID,
                        "count", COUNT)),
                new Params(Map.of("region", REGION, "name", NAME, "imageId", IMAGE_ID, "flavorId", FLAVOR_ID,
                        "waitActiveSec", WAIT_ACTIVE)),
                new Params(Map.of("region", REGION, "name", NAME, "imageId", IMAGE_ID, "flavorId", FLAVOR_ID,
                        "networkIds", NETWORK_IDS))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testWrongParams(Params invalidParams, String missingParam) {
        // when
        Throwable throwable = catchThrowable(
                () -> new AddInstanceAction(invalidParams, openstackServiceMock));

        // then
        assertMissingParamException(throwable, missingParam);
    }

    private static Stream<Arguments> testWrongParams() {
        return Stream.of(
                Arguments.of(
                        new Params(Map.of("name", NAME)), "region"),
                Arguments.of(
                        new Params(Map.of("region", REGION)), "name"),
                Arguments.of(
                        new Params(Map.of("region", REGION, "name", NAME)), "imageId"),
                Arguments.of(
                        new Params(Map.of("region", REGION, "name", NAME, "imageId", IMAGE_ID)), "flavorId")
        );
    }
}
