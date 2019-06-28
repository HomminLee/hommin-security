package com.hommin.security.core.validate.code;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码
 *
 * @author Hommin
 * 2019年06月19日 4:11 PM
 */
@Data
public class ValidateCode implements Serializable{
    private static final long serialVersionUID = -6482231418968354764L;

    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public ValidateCode(String code, int expireTime) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public boolean isExpried(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
