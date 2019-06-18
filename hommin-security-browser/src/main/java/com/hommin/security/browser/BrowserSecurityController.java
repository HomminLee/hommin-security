package com.hommin.security.browser;

import com.hommin.security.browser.support.SimpleResponse;
import com.hommin.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hommin
 * 2019年06月18日 3:40 PM
 */
@RestController
@Slf4j
public class BrowserSecurityController {

    @Autowired
    private SecurityProperties securityProperties;

    private final static String HTML_SUFFIX = ".html";


    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @RequestMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            log.info("引发的请求是:{}", redirectUrl);
            if(StringUtils.endsWithIgnoreCase(redirectUrl, HTML_SUFFIX)){
                // 若为html请求, 则跳转到登录页面
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage() );
            }
        }
        // 若为json请求, 则返回json格式提示
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }

}
