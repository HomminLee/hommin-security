package com.hommin.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hommin
 */
@Getter
@Setter
public class ImageCodeProperties extends SmsCodeProperties{

    private int width = 67;
    private int height = 23;

}
