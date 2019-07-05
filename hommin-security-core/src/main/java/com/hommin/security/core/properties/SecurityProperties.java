package com.hommin.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hommin
 *
 */
@Data
@ConfigurationProperties(prefix = "hommin.security")
public class SecurityProperties {
	
	private BrowserProperties browser = new BrowserProperties();

	private ValidateCodeProperties code = new ValidateCodeProperties();

	private SpringSocialProperties social = new SpringSocialProperties();

	private OauthProperties oauth = new OauthProperties();

}
