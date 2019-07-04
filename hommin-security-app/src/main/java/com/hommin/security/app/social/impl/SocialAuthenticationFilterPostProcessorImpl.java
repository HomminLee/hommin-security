package com.hommin.security.app.social.impl;

import com.hommin.security.core.social.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 *
 * 第三方登录完成后的处理
 *
 * @author Hommin
 * 2019年07月04日 5:00 PM
 */
@Component
public class SocialAuthenticationFilterPostProcessorImpl implements SocialAuthenticationFilterPostProcessor {

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter filter) {
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
    }
}
