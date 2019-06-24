package com.hommin.security.core.social.qq.config;

import com.hommin.security.core.properties.QQProperties;
import com.hommin.security.core.properties.SecurityProperties;
import com.hommin.security.core.social.qq.connect.QQOAuth2ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @author Hommin
 * 2019年06月24日 5:11 PM
 */
@Configuration
@ConditionalOnProperty(prefix = "hommin.security.social.qq", name = "app-id")
public class SocialBeanConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ConnectionFactory<?> createConnectionFactory() {
        QQProperties qqProperties = securityProperties.getSocial().getQq();
        return new QQOAuth2ConnectionFactory(qqProperties.getProviderId(), qqProperties.getAppId(), qqProperties.getAppSecret());
    }
}
