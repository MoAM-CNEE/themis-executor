package org.k8loud.executor.exception.code;

import org.k8loud.executor.exception.OpenstackException;
import org.openstack4j.model.compute.Action;

public enum OpenstackExceptionCode {
    TOKEN_INVALID,
    AUTHENTICATION_ERROR,
    SERVER_NOT_EXISTS,
    FLAVOR_NOT_EXITS,
    VOLUME_NOT_EXITS,
    IMAGE_NOT_EXISTS,
    SECURITY_GROUP_NOT_EXIST,
    SECURITY_GROUP_RULE_NOT_EXIST,
    RESIZE_SERVER_FAILED,
    GET_SERVER_NAMES_FAILED,
    FLAVORS_DISKS_NOT_SAME,
    FLAVORS_COMPARISON,
    COPY_SERVER_FAILED,
    ATTACH_VOLUME_FAILED,
    VOLUME_ERROR,
    DETACH_VOLUME_FAILED,
    CREATE_SERVER_SNAPSHOT_FAILED,
    DELETE_SERVER_SNAPSHOT_FAILED,
    CREATE_VOLUME_SNAPSHOT_FAILED,
    DELETE_VOLUME_SNAPSHOT_FAILED,
    CREATE_SECURITY_GROUP_FAILED,
    REMOVE_SECURITY_GROUP_FAILED,
    CREATE_SERVER_FAILED,
    DELETE_SERVER_FAILED,
    ADD_SECURITY_GROUP_FAILED,
    REMOVE_SECURITY_GROUP_FROM_INSTANCE_FAILED,
    ADD_RULE_FAILED,
    REMOVE_RULE_FAILED,
    IP_THROTTLE_FAILED,
    PAUSE_SERVER_FAILED,
    UNPAUSE_SERVER_FAILED,
    STOP_SERVER_FAILED,
    START_SERVER_FAILED,
    UNSUPPORTED_ACTION,
    GET_FLAVORS_FAILED;

    public static OpenstackExceptionCode getNovaExceptionCode(Action action) throws OpenstackException {
        return switch (action){
            case PAUSE -> PAUSE_SERVER_FAILED;
            case UNPAUSE -> UNPAUSE_SERVER_FAILED;
            case STOP -> STOP_SERVER_FAILED;
            case START -> START_SERVER_FAILED;
            default -> throw new OpenstackException(action.name() + " not supported", UNSUPPORTED_ACTION);
        };
    }
}
