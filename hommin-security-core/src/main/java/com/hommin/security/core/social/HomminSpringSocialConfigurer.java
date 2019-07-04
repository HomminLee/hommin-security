package com.hommin.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 继承{@link SpringSocialConfigurer}类, 目的是获得{@link SocialAuthenticationFilter}并自定义{@code filterProcessesUrl}的值.
 *
 * @author Hommin
 */
public class HomminSpringSocialConfigurer extends SpringSocialConfigurer {

    private final String filterProcessesUrl;

    private final SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    public HomminSpringSocialConfigurer(String filterProcessesUrl, SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor) {
        this.filterProcessesUrl = filterProcessesUrl;
        this.socialAuthenticationFilterPostProcessor = socialAuthenticationFilterPostProcessor;
    }

    @Override
    protected <T> T postProcess(T object) {
        // 可以获得SocialAuthenticationFilter, 其控制了整个第三方账号的登录流程(包括code, openid, connect)
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);

        // 在第三方账号登录完成后进行处理(配置filter以配置filter的流程)
        if (socialAuthenticationFilterPostProcessor != null) {
            socialAuthenticationFilterPostProcessor.process(filter);
        }
        return (T) filter;
    }

}
