package com.hommin.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hommin
 * 2019年06月27日 5:05 PM
 */
@Getter
@Setter
public class SecuritySessionProperties {

    private int maximumSessions = 1;

    private boolean maxSessionsPreventsLogin = false;

    private String sessionInvalidUrl = SecurityConst.DEFAULT_SESSION_INVALID_URL;

}
