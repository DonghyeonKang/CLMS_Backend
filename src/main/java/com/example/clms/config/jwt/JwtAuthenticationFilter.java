package com.example.clms.config.jwt;

import com.example.clms.common.exception.ArgumentNotValidException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.exception.MemberAuthenticationException;
import com.example.clms.dto.user.LoginRequest;
import com.example.clms.service.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		// request body to loginRequest
		ObjectMapper om = new ObjectMapper();
		LoginRequest loginRequest = null;
		try {
			loginRequest = om.readValue(request.getInputStream(), LoginRequest.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// 데이터 검증
		validateArgs(loginRequest);

		// authenticationToken 생성
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword());
		
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication authResult) {
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

		// jwt access token 생성
		String accessToken = jwtService.createAccessToken(principalDetails.getUser().getId(), principalDetails.getUser().getUsername());

		// jwt refresh token 생성
		String refreshToken = jwtService.createRefreshToken();

		// refresh token db 에 저장
		jwtService.setRefreshToken(refreshToken, principalDetails.getUser());

		// authorization 헤더에 access, 쿠키에 refresh 토큰 전달
		ResponseCookie refreshCookie = ResponseCookie.from(JwtProperties.REFRESH_TOKEN, refreshToken)
				.domain("localhost")
				.maxAge(7 * 24 * 60 * 60) // 7일
				.path("/")	// 다른 엔드포인트로 가도 쿠키를 가져다닐 수 있도록
				.sameSite("Lax")	// 다른 사이트로 쿠키를 보낼 수 있는 지 여부. none 이면 보낼 수 있음 그런데 none 이면 secure 속성도 설정해야함 아니면 경고 메시지와 함께 쿠키를 사용하지 않음. secure 속성은 https 에서만 쿠키를 보내겠다는 속성이다. Strict 옵션은 절대로 보내지 않겠다는 옵션, Lax 옵션은 Strict 에 일부 예외를 두어 적용되는 설정. post delete 요청에서는 쿠키가 전달되지 않는다.
				.httpOnly(true)	// httponly option. 프론트에서 js로 쿠키를 뜯어볼 수 없음
				.build();

		response.setHeader("Authorization", accessToken);
		response.setHeader("Set-Cookie", refreshCookie.toString());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											  AuthenticationException failed) {
		throw new MemberAuthenticationException(ErrorCode.MEMBER_ERROR);
	}

	private void validateArgs(LoginRequest loginRequest) {
		if(loginRequest.getUsername() == null ||
				loginRequest.getPassword() == null ||
				loginRequest.getUsername().isEmpty() ||
				loginRequest.getPassword().isEmpty() ||
				!isValidEmail(loginRequest.getUsername())) {
			throw new ArgumentNotValidException(ErrorCode.NOT_VALID_ARGUMENT);
		}
	}

	private boolean isValidEmail(String email) {
		// 이메일 형식을 검사하는 정규 표현식 패턴
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(email).matches();
	}
}
