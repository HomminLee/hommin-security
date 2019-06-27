package com.hommin.security.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * 当第三方用户登录, 而该用户没有新建账号时, 添加新账号并认为其登录
 *
 * @author Hommin
 * 2019年06月26日 4:13 PM
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp{

    @Override
    public String execute(Connection<?> connection) {
        return connection.getKey().getProviderUserId();
    }
}
