package com.hommin.security.core.authentication.openid;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Hommin
 * 2019年06月20日 11:38 AM
 */
@Data
public class OpenIdAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private UsersConnectionRepository usersConnectionRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;
        String openId = (authenticationToken.getPrincipal() == null) ? "NONE_PROVIDED" : authenticationToken.getName();
        String providerId = (authenticationToken == null) ? "NONE_PROVIDED" : authenticationToken.getProviderId();
        // 通过openId和providerId找到userId(在UserConnection表中存在)
        Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(providerId, new HashSet<String>(1) {{
            add(openId);
        }});


        if (userIds.size() == 0) {
            throw new UsernameNotFoundException("找不到该用户");
        }
        // 通过userId找到UserDetails(在系统用户表中存在)
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userIds.iterator().next());
        if(userDetails == null){
            throw new UsernameNotFoundException("找不到该用户");
        }
        // 生成token并认为其登录成功
        OpenIdAuthenticationToken token = new OpenIdAuthenticationToken(openId, providerId, authenticationToken.getAuthorities());

        token.setDetails(authentication.getDetails());

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (OpenIdAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}
