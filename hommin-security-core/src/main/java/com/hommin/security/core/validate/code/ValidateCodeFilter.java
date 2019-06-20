package com.hommin.security.core.validate.code;

import com.hommin.security.core.properties.SecurityConst;
import com.hommin.security.core.properties.SecurityProperties;
import com.hommin.security.core.validate.code.image.ImageCode;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hommin
 */
@Data
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private SecurityProperties securityProperties;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private Set<String> urls = new HashSet<>();

    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String urlStr = securityProperties.getCode().getImage().getUrl();
        String[] urlArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlStr, ",");
        for (String url : urlArr) {
            urls.add(url);
        }
        urls.add(SecurityConst.DEFAULT_LOGIN_PROCESSING_URL_FROM);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 检查request中的路径是否和配置的urls中任一相匹配
        boolean action = false;
        for (String url : urls) {
            if (matcher.match(url, request.getRequestURI())) {
                action = true;
            }
        }
        if (action) {
            // 如匹配, 则验证验证码
            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }

        }

        filterChain.doFilter(request, response);

    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        String sessionKey = ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE";
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,
                sessionKey);

        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), SecurityConst.DEFAULT_PARAMETER_NAME_CODE_IMAGE);

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }


}
