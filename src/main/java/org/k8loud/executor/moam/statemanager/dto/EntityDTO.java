package org.k8loud.executor.moam.statemanager.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class EntityDTO {
    public int id;
    public String apiVersion;
    public String kind;
    public String name;
    public String namespace;
    public JsonNode definition;
}
