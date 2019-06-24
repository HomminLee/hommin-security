package com.hommin.security.core.social.qq.connect;

import com.hommin.security.core.social.qq.api.QQ;
import com.hommin.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @author Hommin
 * 2019年06月21日 5:18 PM
 */
public class QQOAuth2ServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
    private String appId;

    private static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize";

    private static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    public QQOAuth2ServiceProvider(String appId, String appSecret) {
        super(new OAuth2Template(appId, appSecret, AUTHORIZE_URL, ACCESS_TOKEN_URL));
        this.appId = appId;
    }

    @Override
    public QQImpl getApi(String accessToken) {
        return new QQImpl(appId, accessToken);
    }
}
