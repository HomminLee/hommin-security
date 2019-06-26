package com.hommin.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.binding.StringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

/**
 * @author Hommin
 * 2019年06月21日 5:04 PM
 */
@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    private final String appId;

    private final String openId;

    private final String accessToken;


    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

    private static ObjectMapper objectMapper = new ObjectMapper();

    public QQImpl(String appId, String accessToken) {
        super(accessToken);
        this.appId = appId;
        this.accessToken = accessToken;
        // 构造获取openId的url
        String openIdUrl = StringFormatter.format(URL_GET_OPENID, accessToken).getValue();
        // 请求openId
        String result = getRestTemplate().getForObject(openIdUrl, String.class);
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    @Override
    public QQUserInfo getUserInfo() {
        // 构造获取user info的url
        String userInfoUrl = String.format(URL_GET_USERINFO, accessToken, appId, openId);
        // 请求openId
        String userInfoStr = getRestTemplate().getForObject(userInfoUrl, String.class);
        QQUserInfo userInfo;
        try {
            userInfo = objectMapper.readValue(userInfoStr, QQUserInfo.class);
            if ("-1".equals(userInfo.getRet())) {
                throw new RuntimeException("获取用户信息失败");
            }
            userInfo.setOpenId(openId);
            log.info("QQ登录, 解析到user:{}", userInfo);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
