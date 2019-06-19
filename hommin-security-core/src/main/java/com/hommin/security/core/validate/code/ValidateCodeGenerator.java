package com.hommin.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 图形验证码生成器
 *
 * @author Hommin
 * 2019年06月19日 1:53 PM
 */
public interface ValidateCodeGenerator<V extends ValidateCode> {
    /**
     * 根据request生成ImageCode
     * @param request 请求
     * @return
     */
    V generate(ServletWebRequest request);
}
