package com.hommin.security.core.social;

import com.hommin.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author Hommin
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Bean
	public SpringSocialConfigurer homminSocialSecurityConfig() {
		String filterProcessesUrl = securityProperties.getSocial().getQq().getFilterProcessesUrl();
		HomminSpringSocialConfigurer configurer = new HomminSpringSocialConfigurer(filterProcessesUrl);
		return configurer;
	}
	
}
