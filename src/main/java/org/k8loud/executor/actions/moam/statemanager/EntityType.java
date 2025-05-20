package org.k8loud.executor.actions.moam.statemanager;

public enum EntityType {
    RULE("RULE"),
    METRIC("METRIC"),
    ENVIRONMENT_ENTITY("ENVIRONMENT_ENTITY");

    private final String representation;

    EntityType(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    public static EntityType fromRepresentation(String value) {
        for (EntityType type : EntityType.values()) {
            if (type.getRepresentation().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown EntityType: " + value);
    }
}
