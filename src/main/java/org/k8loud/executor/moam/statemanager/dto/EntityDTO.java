package org.k8loud.executor.moam.statemanager.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude = "definition")
public class EntityDTO {
    public int id;
    public String apiVersion;
    public String kind;
    public String name;
    public String namespace;
    public JsonNode definition;
}
