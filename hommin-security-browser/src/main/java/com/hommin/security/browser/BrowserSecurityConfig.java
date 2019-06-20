/**
 *
 */
package com.hommin.security.browser;

import com.hommin.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.hommin.security.core.properties.SecurityConst;
import com.hommin.security.core.properties.SecurityProperties;
import com.hommin.security.core.validate.code.SmsCodeFilter;
import com.hommin.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author Hommin
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

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
    private UserDetailsService userDetailsService;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        // 只有初次使用: tokenRepository.setCreateTableOnStartup(true)
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private SmsCodeFilter smsCodeFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 添加额外的config
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                // 添加filter
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 登录配置
                .formLogin()
                .loginPage(SecurityConst.DEFAULT_LOGIN_UNAUTEHNTICATION_URL)
                .loginProcessingUrl(SecurityConst.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                // remember me
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .and()
                // 路径登录要求配置
                .authorizeRequests()
                .antMatchers(SecurityConst.DEFAULT_LOGIN_UNAUTEHNTICATION_URL
                        , SecurityConst.DEFAULT_LOGIN_PROCESSING_URL_MOBILE
                        , securityProperties.getBrowser().getLoginPage()
                        , SecurityConst.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                // csrf
                .csrf().disable();

    }

}
