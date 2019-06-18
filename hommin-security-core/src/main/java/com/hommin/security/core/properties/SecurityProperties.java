/**
 * 
 */
package com.hommin.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hommin
 *
 */
@ConfigurationProperties(prefix = "hommin.security")
public class SecurityProperties {
	
	private BrowserProperties browser = new BrowserProperties();

	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

}
