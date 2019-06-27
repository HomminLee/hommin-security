package com.hommin.security.core.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.social.SocialProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hommin
 * 2019年06月24日 5:16 PM
 */
@Data
@ConfigurationProperties(prefix = "hommin.security.social.qq")
public class QQProperties extends SocialProperties {

    private String providerId = "qq";

    private String filterProcessesUrl = "/auth";

    private String signUpUrl = "/hommin-sign-up.html";

}
