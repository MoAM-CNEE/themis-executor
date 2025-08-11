package org.k8loud.executor.cnapp.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "message")
public class MessageProperties {
    private String messageEndpoint = "message";
    private String messagesEndpoint = "messages";
}
