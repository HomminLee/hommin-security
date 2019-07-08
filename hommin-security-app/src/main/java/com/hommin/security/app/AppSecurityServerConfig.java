package com.hommin.security.app;

import com.hommin.security.core.properties.OauthClientProperties;
import com.hommin.security.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Hommin
 * 2019年06月28日 5:32 PM
 */
@Configuration
@EnableAuthorizationServer
public class AppSecurityServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private TokenStore redisTokenStore;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // clients.clients(new MyClientDetailsService())以使用数据库

        // 内存方式
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

        OauthClientProperties[] clientProp = securityProperties.getOauth().getClient();
        if(!ArrayUtils.isEmpty(clientProp)){
            for (OauthClientProperties properties : clientProp) {
                builder.withClient(properties.getClientId())
                        .secret(properties.getClientSecret())
                        .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                        .accessTokenValiditySeconds(properties.getAccessTokenValidateSeconds())
                        .refreshTokenValiditySeconds(2592000)
                        .scopes("all");
            }
        }
        super.configure(clients);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 在不配置AuthorizationServerConfigurerAdapter的情况下, spring的默认实现会去找下面两个类的实例
        // 自己实现配置AuthorizationServerConfigurerAdapter后, spring不会再去默认找, 所以需要自己配置
        endpoints.tokenStore(redisTokenStore)
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
        super.configure(endpoints);
    }
}
