package com.example.foscore.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class OAuthProperties {

    private String jwtSetUri;

}
