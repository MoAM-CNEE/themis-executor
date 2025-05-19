package org.k8loud.executor.statemanager;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "statemanager")
public class StateManagerProperties {
    private String url = "http://state-manager-svc.state-layer.svc.cluster.local:8000";
    private String entityEndpoint = "/entity";
}
