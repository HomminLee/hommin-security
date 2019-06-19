package com.hommin.security.core.validate.code.sms;

import com.hommin.security.core.validate.code.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;


/**
 * @author Hommin
 */
@Data
public class SmsCode extends ValidateCode {
    public SmsCode(String code, LocalDateTime expireTime) {
        super(code, expireTime);
    }

    public SmsCode(String code, int expireTime) {
        super(code, expireTime);
    }
}
