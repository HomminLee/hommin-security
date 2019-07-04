package com.hommin.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * 第三方授权登录成功后的处理
 *
 * @author Hommin
 * 2019年07月04日 4:37 PM
 */
public interface SocialAuthenticationFilterPostProcessor {

    /**
     * 配置filter
     * @param filter SocialAuthenticationFilter 第三方授权登录filter
     */
    void process(SocialAuthenticationFilter filter);

}
