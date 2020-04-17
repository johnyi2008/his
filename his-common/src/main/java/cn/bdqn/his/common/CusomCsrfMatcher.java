package cn.bdqn.his.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "csrf.allowed.server")
public class CusomCsrfMatcher implements RequestMatcher {
	public static final String HEADER_NAME = "remoteHost";
	private final HashSet<String> allowedMethods = new HashSet<>(
			Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

	private Set<String> urls;
	
	@Override
	public boolean matches(HttpServletRequest request) {
		boolean returnValue = !this.allowedMethods.contains(request.getMethod());
		if(returnValue) {
			String remoteHost = request.getHeader(HEADER_NAME);
			if(remoteHost!=null) {
				returnValue = !urls.contains(remoteHost);
			}
		}
		return returnValue;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
	
	
}
