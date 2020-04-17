package cn.bdqn.his.medicine.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.bdqn.his.common.CusomCsrfMatcher;

import javax.annotation.Resource;

@EnableOAuth2Sso
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	private CusomCsrfMatcher cusomCsrfMatcher;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().requireCsrfProtectionMatcher(cusomCsrfMatcher);
		super.configure(http);
	}
}
