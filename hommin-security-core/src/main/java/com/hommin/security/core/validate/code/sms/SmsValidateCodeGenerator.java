package com.hommin.security.core.validate.code.sms;

import com.hommin.security.core.properties.SecurityProperties;
import com.hommin.security.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 接口{@link ValidateCodeGenerator}的默认实现类
 *
 * @author Hommin
 * 2019年06月19日 1:54 PM
 */
public class SmsValidateCodeGenerator implements ValidateCodeGenerator<SmsCode> {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public SmsCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getImage().getLength());
        return new SmsCode(code, securityProperties.getCode().getImage().getExpireIn());
    }

}
