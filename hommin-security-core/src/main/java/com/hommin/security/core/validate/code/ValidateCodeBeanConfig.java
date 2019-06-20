package com.hommin.security.core.validate.code;

import com.hommin.security.core.validate.code.image.ImageValidateCodeGenerator;
import com.hommin.security.core.validate.code.sms.SmsValidateCodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置
 *
 * @author Hommin
 * 2019年06月19日 1:56 PM
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator(){
        return new ImageValidateCodeGenerator();
    }
    @Bean
    @ConditionalOnMissingBean(name = "smsValidateCodeGenerator")
    public ValidateCodeGenerator smsValidateCodeGenerator(){
        return new SmsValidateCodeGenerator();
    }

}
