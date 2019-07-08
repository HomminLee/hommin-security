package com.hommin.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hommin
 * 2019年07月05日 3:47 PM
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "hommin.security.oauth")
public class OauthProperties {

    private OauthClientProperties[] client;

    private String tokeStore;

    private String signingKey = "hommin";

}