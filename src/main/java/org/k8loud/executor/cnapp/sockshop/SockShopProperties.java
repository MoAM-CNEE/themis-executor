package org.k8loud.executor.cnapp.sockshop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sockshop")
public class SockShopProperties {
    private String loginUserEndpoint = "login";
    private String registerUserEndpoint = "register";
    private String customersEndpoint = "customers";
    private String addressesEndpoint = "addresses";
}
