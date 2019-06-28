/**
 *
 */
package com.hommin.security.browser;

import com.hommin.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.hommin.security.core.properties.SecurityConst;
import com.hommin.security.core.properties.SecurityProperties;
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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

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
    @Autowired
    private SpringSocialConfigurer homminSocialSecurityConfig;
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 添加额外的config
                .apply(smsCodeAuthenticationSecurityConfig)
                    .and()
                // social config
                .apply(homminSocialSecurityConfig)
                    .and()
                // 添加filter
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
                .logout()
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .deleteCookies(securityProperties.getBrowser().getDeleteCookie())
                    .and()
                .sessionManagement()
                    .invalidSessionStrategy(invalidSessionStrategy)
                    .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
                    .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                    .and()
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
