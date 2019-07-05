package com.hommin.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hommin
 * 2019年07月05日 3:46 PM
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "hommin.security.oauth.client")
public class OauthClientProperties {

    private String clientId;

    private String clientSecret;

    private int accessTokenValidateSeconds = 7200;

}