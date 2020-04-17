package cn.bdqn.his.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

public class CsrfHeaderFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//把csrfToken设定到cookie中发送到前台
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		if (csrf != null) {
			//Cookie cookie = WebUtils.getCookie(request, "CSRF-TOKEN");
			String token = csrf.getToken();
			Cookie cookie = new Cookie("X-CSRF-TOKEN", token);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		filterChain.doFilter(request, response);
	}
}
