package com.hommin.security.core.properties;

import lombok.Data;

/**
 * @author Hommin
 */
@Data
public class BrowserProperties {
	
	private String loginPage = "/hommin-sign-in.html";

	private LoginType loginType = LoginType.JSON;
	
}
