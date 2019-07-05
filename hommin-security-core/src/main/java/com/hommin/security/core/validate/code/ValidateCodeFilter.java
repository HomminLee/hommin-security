package com.hommin.security.core.validate.code;

import com.hommin.security.core.properties.SecurityConst;
import com.hommin.security.core.properties.SecurityProperties;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private ValidateCodeProcessorHolder processorHolder;

    private Map<String, ValidateCodeType> urlMap = new HashMap<>();



    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.IMAGE);
        urlMap.put(SecurityConst.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);

        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
        urlMap.put(SecurityConst.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
    }

    private void addUrlToMap(String urlStr, ValidateCodeType type) {
        String[] urlArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlStr, ",");
        for (String url : urlArr) {
            urlMap.put(url, type);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 检查request中的路径是否和配置的urls中任一相匹配, 如匹配, 则找到对应的processor去验证验证码
        for (String url : urlMap.keySet()) {
            if (matcher.match(url, request.getRequestURI())) {
                try {
                    processorHolder.findValidateCodeProcessor(urlMap.get(url)).validate(new ServletWebRequest(request));
                } catch (ValidateCodeException e) {
                    authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);

    }


}
