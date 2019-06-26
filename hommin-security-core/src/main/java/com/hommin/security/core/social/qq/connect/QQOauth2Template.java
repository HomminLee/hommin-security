package com.hommin.security.core.social.qq.connect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * QQ登录请求模板
 *
 * @author Hommin
 * 2019年06月26日 11:36 AM
 */
@Slf4j
public class QQOauth2Template extends OAuth2Template{

    public QQOauth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        // QQ使用code换取token时, 需要携带appId和appSecret, 默认是不携带的, 所以需要将UseParametersForClientAuthentication设置为true
        // 使用之处可查看OAuth2Template.exchangeForAccess(...)
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        RestTemplate restTemplate = super.getRestTemplate();
        // 一般oauth2是期望json格式的返回, 但QQ返回的是html格式, spring social无法处理html返回, 所以需要添加额外的MessageConverter.
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        //  一般oauth2是期望json格式的返回, 可以使用Map直接解析.
        //  但QQ返回的是html格式: access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
        String result = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        log.info("获取accessToke的响应："+result);

        try {
            String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");

            String accessToken = StringUtils.substringAfterLast(items[0], "=");
            Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
            String refreshToken = StringUtils.substringAfterLast(items[2], "=");

            return new AccessGrant(accessToken, null, refreshToken, expiresIn);
        } catch (Exception e) {
            log.info("code换取token失败, QQ响应:{}", result);
            return null;
        }
    }


}
