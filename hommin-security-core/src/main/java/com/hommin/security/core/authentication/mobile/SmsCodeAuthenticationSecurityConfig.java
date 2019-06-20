package com.hommin.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 短信验证码登录安全配置
 *
 * @author Hommin
 * 2019年06月20日 2:22 PM
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        SmsCodeAuthenticationFilter smsFilter = new SmsCodeAuthenticationFilter();
        smsFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        smsFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        smsFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        SmsCodeAuthenticationProvider smsProvider = new SmsCodeAuthenticationProvider();
        smsProvider.setUserDetailsService(userDetailsService);

        builder.authenticationProvider(smsProvider)
                .addFilterAfter(smsFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
