package com.hommin.security.core.properties;

import lombok.Data;

/**
 * @author Hommin
 */
@Data
public class SmsCodeProperties {

    private int length = 4;
    private int expireIn = 60;

    private String url = "";

}
