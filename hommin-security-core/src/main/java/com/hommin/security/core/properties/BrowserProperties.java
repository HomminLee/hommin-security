package com.hommin.security.core.properties;

import lombok.Data;

/**
 * @author Hommin
 */
@Data
public class BrowserProperties {
	
	private String loginPage = SecurityConst.DEFAULT_LOGIN_PAGE;

	private LoginType loginType = LoginType.JSON;

	private int rememberMeSeconds = 3600;
	
}
