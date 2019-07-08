package com.hommin.security.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hommin.security.core.properties.SecurityProperties;
import com.hommin.security.dto.User;
import com.hommin.security.dto.UserQueryCondition;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hommin
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	@Autowired
	private SecurityProperties securityProperties;

	@GetMapping("/me")
	public Object getCurrentUser(Authentication user, HttpServletRequest request) throws UnsupportedEncodingException {
		String authorization = request.getHeader("Authorization");
		String token = StringUtils.substringAfter(authorization, "Bearer ");

		// 注意: 必须使用UTF-8进行解码
		Claims claims = Jwts.parser()
				.setSigningKey(securityProperties.getOauth().getSigningKey().getBytes("UTF-8"))
				.parseClaimsJws(token).getBody();

		return user;
	}

	@PostMapping("/register")
	public void register(User user, HttpServletRequest request){
		String username = user.getUsername();
		// 注册过程
		String userId = "userId:"+username;
		providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
	}

	@PostMapping
	@ApiOperation(value = "创建用户")
	public User create(@Valid @RequestBody User user) {

		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());

		user.setId("1");
		return user;
	}

	@PutMapping("/{id:\\d+}")
	public User update(@Valid @RequestBody User user, BindingResult errors) {

		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());

		user.setId("1");
		return user;
	}

	@DeleteMapping("/{id:\\d+}")
	public void delete(@PathVariable String id) {
		System.out.println(id);
	}

	@GetMapping
	@JsonView(User.UserSimpleView.class)
	@ApiOperation(value = "用户查询服务")
	public List<User> query(UserQueryCondition condition,
			@PageableDefault(page = 2, size = 17, sort = "username,asc") Pageable pageable) {

		System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));

		System.out.println(pageable.getPageSize());
		System.out.println(pageable.getPageNumber());
		System.out.println(pageable.getSort());

		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());
		users.add(new User());
		return users;
	}

	@GetMapping("/{id:\\d+}")
	@JsonView(User.UserDetailView.class)
	public User getInfo(@ApiParam("用户id") @PathVariable String id) {
//		throw new RuntimeException("user not exist");
		System.out.println("进入getInfo服务");
		User user = new User();
		user.setUsername("tom");
		return user;
	}

}
