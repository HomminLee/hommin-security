package com.hommin.security.app;

import com.hommin.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.hommin.security.core.properties.SecurityConst;
import com.hommin.security.core.properties.SecurityProperties;
import com.hommin.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @author Hommin
 * 2019年06月28日 5:32 PM
 */
@Configuration
@EnableAuthorizationServer
public class AppSecurityServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;
    @SuppressWarnings("all")
    @Autowired
    private DataSource dataSource;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private SpringSocialConfigurer homminSocialSecurityConfig;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 添加额外的config
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                // social config
                .apply(homminSocialSecurityConfig)
                .and()
                // 添加filter
//                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 登录配置
                .formLogin()
                .loginPage(SecurityConst.DEFAULT_LOGIN_UNAUTEHNTICATION_URL)
                .loginProcessingUrl(SecurityConst.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                // 路径登录要求配置
                .authorizeRequests()
                .antMatchers(SecurityConst.DEFAULT_LOGIN_UNAUTEHNTICATION_URL
                        , SecurityConst.DEFAULT_LOGIN_PROCESSING_URL_MOBILE
                        , securityProperties.getBrowser().getLoginPage()
                        , SecurityConst.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*"
                        , "/auth/*", "/user/register"
                        , securityProperties.getSocial().getQq().getFilterProcessesUrl() + "/*"
                        , securityProperties.getSocial().getQq().getSignUpUrl()
                        , securityProperties.getBrowser().getSession().getSessionInvalidUrl()
                        , securityProperties.getBrowser().getLogoutSuccessUrl()
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                // csrf
                .csrf().disable();
    }
}
