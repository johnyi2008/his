package cn.bdqn.his.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CsrfFilterConfig {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean filterRegist() {
		FilterRegistrationBean frBean = new FilterRegistrationBean();
		frBean.setFilter(new CsrfHeaderFilter());
		frBean.addUrlPatterns("/*");
		log.debug("注册CSRFCOOKIE过滤器");
		return frBean;
	}
}
