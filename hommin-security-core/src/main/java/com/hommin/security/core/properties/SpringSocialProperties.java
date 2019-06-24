package com.hommin.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * spring social配置
 *
 * @author Hommin
 * 2019年06月24日 5:15 PM
 */
@Data
@ConfigurationProperties(prefix = "hommin.security.social")
public class SpringSocialProperties {

    private QQProperties qq = new QQProperties();

}
