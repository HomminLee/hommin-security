package com.hommin.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hommin.security.core.properties.SecurityProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Hommin
 */
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    public MyAuthenticationSuccessHandler(SecurityProperties securityProperties, ClientDetailsService clientDetailsService, AuthorizationServerTokenServices tokenServices) {
        this.securityProperties = securityProperties;
        this.clientDetailsService = clientDetailsService;
        this.tokenServices = tokenServices;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    private SecurityProperties securityProperties;

    private final ClientDetailsService clientDetailsService;

    private final AuthorizationServerTokenServices tokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        logger.info("登录成功");
        // 获得header
        String header = request.getHeader("Authorization");
        String basicHead = "Basic ";
        if (header == null || !header.startsWith(basicHead)) {
            if(authentication instanceof SocialAuthenticationToken){
                // 是第三方登录完成, 说明是直接使用spring social完成了整个第三方认证流程, 因为进过了跳转用户授权页面, 所有header会丢失
                // 返回给前段openId, 让前段再以openId的方式进行认证并发放token
                // todo 目前是直接写回一个token, 而应该是一个跳转页面, 跳转参数上携带openId
                String openId = ((SocialAuthenticationToken) authentication).getConnection().getKey().getProviderUserId();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(openId));
                return;
            }
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        OAuth2Request oAuth2Request = getOauth2Request(header);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(accessToken));

    }

    private OAuth2Request getOauth2Request(String header) throws IOException {
        String[] tokens = extractAndDecodeHeader(header);
        assert tokens.length == 2;

        String clientId = tokens[0];
        String clientSecret = tokens[1];

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if(clientDetails == null){
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在:"+clientId);
        } else if (!clientSecret.equals(clientDetails.getClientSecret())){
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配:"+clientId);
        }

        TokenRequest tokenRequest = new TokenRequest(Collections.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");
        return tokenRequest.createOAuth2Request(clientDetails);
    }

    private String[] extractAndDecodeHeader(String header)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

}
