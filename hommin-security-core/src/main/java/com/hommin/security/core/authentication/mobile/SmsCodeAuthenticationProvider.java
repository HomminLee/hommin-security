package com.hommin.security.core.authentication.mobile;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Hommin
 * 2019年06月20日 11:38 AM
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);

        if (loadedUser == null) {
            throw new UsernameNotFoundException("用户名[" + username + "]找不到");
        }

        SmsCodeAuthenticationToken token = new SmsCodeAuthenticationToken(username, authentication.getAuthorities());

        token.setDetails(authentication.getDetails());

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsCodeAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}
