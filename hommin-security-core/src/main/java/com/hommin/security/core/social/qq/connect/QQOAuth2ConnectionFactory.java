package com.hommin.security.core.social.qq.connect;

import com.hommin.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @author Hommin
 * 2019年06月21日 5:16 PM
 */
public class QQOAuth2ConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    /**
     * Create a {@link OAuth2ConnectionFactory}.
     */
    public QQOAuth2ConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQOAuth2ServiceProvider(appId, appSecret), new QQApiAdapter());
    }
}
