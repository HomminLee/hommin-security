package com.hommin.security.core.validate.code.sms;

import com.hommin.security.core.properties.SecurityConst;
import com.hommin.security.core.validate.code.AbstractValidateCodeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 图形验证码处理器
 *
 * @author Hommin
 * 2019年06月19日 4:30 PM
 */
@Component("smsValidateCodeProcessor")
@Slf4j
public class SmsValidateCodeProcessor extends AbstractValidateCodeProcessor<SmsCode> {

    @Override
    protected void sendValidateCode(ServletWebRequest request, SmsCode smsCode) throws Exception {
        String mobile = ServletRequestUtils.getStringParameter(request.getRequest(), SecurityConst.DEFAULT_PARAMETER_NAME_MOBILE);
        // 发送验证码
        log.info("向手机号{}发送短信验证码, code={}", mobile, smsCode.getCode());
    }
}
