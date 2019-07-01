package com.hommin.security.app;

import com.hommin.security.app.authentication.MyAuthenticationFailureHandler;
import com.hommin.security.app.authentication.MyAuthenticationSuccessHandler;
import com.hommin.security.app.authentication.UserDetailsServiceImpl;
import com.hommin.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 此项目主要Bean的配置
 *
 * @author Hommin
 * 2019年06月28日 5:22 PM
 */
@Configuration
public class AppSecurityBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnMissingBean(name = "myAuthenticationFailureHandler")
    public AuthenticationFailureHandler myAuthenticationFailureHandler(){
        return new MyAuthenticationFailureHandler(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "myAuthenticationSuccessHandler")
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl(passwordEncoder);
    }

}