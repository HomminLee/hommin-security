package com.hommin.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hommin
 */
@Data
@ConfigurationProperties(prefix = "hommin.security.browser")
public class BrowserProperties {

	private SecuritySessionProperties session = new SecuritySessionProperties();
	
	private String loginPage = SecurityConst.DEFAULT_LOGIN_PAGE;

	private String logoutSuccessUrl = SecurityConst.DEFAULT_LOGIN_PAGE;

	private String[] deleteCookie = SecurityConst.DEFAULT_DELETE_COOKIE_ARR;

	private LoginType loginType = LoginType.JSON;

	private int rememberMeSeconds = 3600;
	
}
