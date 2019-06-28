package com.hommin.security.browser.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hommin.security.core.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hommin
 */
public class AbstractSessionStrategy {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private boolean concurrency = false;
	/**
	 * 跳转的url
	 */
	private String destinationUrl;
	/**
	 * 重定向策略
	 */
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 跳转前是否创建新的session
	 */
	private boolean createNewSession = true;

	public AbstractSessionStrategy(String invalidSessionUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
		Assert.isTrue(StringUtils.endsWithIgnoreCase(invalidSessionUrl, ".html"), "url must end with '.html'");
		this.destinationUrl = invalidSessionUrl;
	}

	protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.info("session失效");

		if (createNewSession) {
			request.getSession();
		}

		String sourceUrl = request.getRequestURI();
		String targetUrl= processRedirectUrl(destinationUrl);

		if (StringUtils.endsWithIgnoreCase(sourceUrl, ".html")) {
			logger.info("跳转到:"+targetUrl);
			redirectStrategy.sendRedirect(request, response, targetUrl);
		} else {
			Object result = buildResponseContent(request);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(result));
		}
	}

	private Object buildResponseContent(HttpServletRequest request) {
		String message = "session已失效";
		if (isConcurrency()) {
			message = message + "，有可能是并发登录导致的";
		}
		return new SimpleResponse(message);
	}

	protected String processRedirectUrl(String targetUrl) {
		return targetUrl;
	}

	/**
	 * Determines whether a new session should be created before redirecting (to
	 * avoid possible looping issues where the same session ID is sent with the
	 * redirected request). Alternatively, ensure that the configured URL does
	 * not pass through the {@code SessionManagementFilter}.
	 *
	 * @param createNewSession
	 *            defaults to {@code true}.
	 */
	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}

	private boolean isConcurrency() {
		return concurrency;
	}

	protected void setConcurrency(boolean concurrency) {
		this.concurrency = concurrency;
	}
}
