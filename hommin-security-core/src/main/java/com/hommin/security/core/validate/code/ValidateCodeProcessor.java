package com.hommin.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码执行器
 *
 * @author Hommin
 * 2019年06月19日 4:18 PM
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 根据请求创建校验码等相关流程
     *
     * @param request 请求
     * @throws Exception e
     */
    void create(ServletWebRequest request) throws Exception;

}
