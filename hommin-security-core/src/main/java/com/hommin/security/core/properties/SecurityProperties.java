package com.hommin.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hommin
 *
 */
@ConfigurationProperties(prefix = "hommin.security")
@Data
public class SecurityProperties {
	
	private BrowserProperties browser = new BrowserProperties();

}
