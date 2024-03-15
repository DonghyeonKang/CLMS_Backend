package com.example.clms.config.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.clms.common.CookieUtil;
import com.example.clms.entity.user.User;
import com.example.clms.repository.user.UserRepository;
import com.example.clms.service.auth.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	private final UserRepository userRepository;
	private final JwtService jwtService;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	// 인증이나 권한이 필요한 요청을 거치는 필터
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// request 에서 토큰 가져오기
		String accessToken = getAccessToken(request);
		String refreshToken = getRefreshToken(request);

		if(accessToken == null || refreshToken == null) {
			chain.doFilter(request, response);
			return;
		}

		// token 유효성 검증 및 디코딩
		DecodedJWT decodedAccess = jwtService.checkTokenValid(accessToken);
		DecodedJWT decodedRefresh = jwtService.checkTokenValid(refreshToken);

		if(decodedAccess == null && decodedRefresh == null) {
			chain.doFilter(request, response);
			return;
		}

		String username;
		if(decodedAccess != null) {
			username = decodedAccess.getClaim("username").asString();
		} else {
			username = decodedRefresh.getClaim("username").asString();

			String newAccessToken = jwtService.createAccessToken(
					decodedRefresh.getClaim("userId").asLong(),
					username);

			response.setHeader("Authorization", newAccessToken);
		}

		// 세션에 authentication 저장
		Authentication authentication = createAuthentication(username);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(request, response);	// 필터 반환
	}

	private Authentication createAuthentication(String username) {
		User user = userRepository.findByUsername(username)
				.stream()
				.findFirst()
				.orElse(null); // Optional<Object> 를 Object 로 변경

		// authentication 생성
		PrincipalDetails principalDetails = new PrincipalDetails(user);
		return new UsernamePasswordAuthenticationToken(
						principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
						null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
						principalDetails.getAuthorities());
	}

	private String getAccessToken(HttpServletRequest req) {
		String tmp = req.getHeader(JwtProperties.ACCESS_HEADER_PREFIX);
		if(tmp == null) {
			return null;
		}

		return tmp.replace(JwtProperties.TOKEN_PREFIX, "");
	}

	private String getRefreshToken(HttpServletRequest req) {
		CookieUtil cookieUtil = new CookieUtil(req);
		return cookieUtil.getValue("refresh_token");
	}
}
