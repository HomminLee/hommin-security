package com.hommin.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hommin.security.core.support.SimpleResponse;
import com.hommin.security.core.properties.LoginType;
import com.hommin.security.core.properties.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hommin
 *
 */
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	public MyAuthenticationFailureHandler(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private SecurityProperties securityProperties;

	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		logger.info("登录失败");
		
		if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
		}else{
			super.onAuthenticationFailure(request, response, exception);
		}
		
		
	}

}
