package com.hommin.security.core.social;

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

	@Bean
	public SpringSocialConfigurer homminSocialSecurityConfig() {
		return new SpringSocialConfigurer();
	}
	
}
