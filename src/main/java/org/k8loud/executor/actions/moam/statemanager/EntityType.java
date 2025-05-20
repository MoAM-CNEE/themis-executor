package org.k8loud.executor.actions.moam.statemanager;

public enum EntityType {
    RULE,
    METRIC,
    ENVIRONMENT_ENTITY;

    public static EntityType fromName(String value) {
        for (EntityType type : EntityType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown EntityType: " + value);
    }
}
