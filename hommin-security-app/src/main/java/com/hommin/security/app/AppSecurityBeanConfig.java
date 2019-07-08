package com.hommin.security.app;

import com.hommin.security.app.authentication.MyAuthenticationFailureHandler;
import com.hommin.security.app.authentication.MyAuthenticationSuccessHandler;
import com.hommin.security.app.authentication.UserDetailsServiceImpl;
import com.hommin.security.app.jwt.MyJwtTokenEnhancer;
import com.hommin.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
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


    /**
     * 在RedisAutoConfiguration.class中自动配置
     */
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private  ClientDetailsService clientDetailsService;
    @Autowired
    private  AuthorizationServerTokenServices tokenServices;

    @Bean
    @ConditionalOnMissingBean(name = "myAuthenticationFailureHandler")
    public AuthenticationFailureHandler myAuthenticationFailureHandler(){
        return new MyAuthenticationFailureHandler(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "myAuthenticationSuccessHandler")
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler(securityProperties, clientDetailsService, tokenServices);
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl(passwordEncoder);
    }


    @Bean
    @ConditionalOnProperty(prefix = "hommin.security.oauth", name = "tokeStore", havingValue = "redis")
    public RedisTokenStore redisTokenStore(){
        return new RedisTokenStore(connectionFactory);
    }

    @Configuration
    @ConditionalOnProperty(prefix = "hommin.security.oauth", name = "tokeStore", havingValue = "jwt",matchIfMissing = true)
    public static class JwtTokenConfig{

        @Autowired
        private SecurityProperties securityProperties;

        @Bean
        public TokenStore jwtTokenStore(){
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setSigningKey(securityProperties.getOauth().getSigningKey());
            return converter;
        }

        @Bean
        @ConditionalOnBean(TokenEnhancer.class)
        public TokenEnhancer jwtTokenEnhancer(){
            return new MyJwtTokenEnhancer();
        }

    }

}
