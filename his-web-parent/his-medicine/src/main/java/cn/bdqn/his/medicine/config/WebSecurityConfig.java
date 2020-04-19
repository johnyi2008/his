package cn.bdqn.his.medicine.config;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.bdqn.his.common.CusomCsrfMatcher;
/**
 * 如果需要自定义security配置，开启注释掉的注解，同时移除启动类上的@EnableOAuth2Sso
 */
//@EnableWebSecurity(debug = true)
//@EnableOAuth2Sso
//@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	private CusomCsrfMatcher cusomCsrfMatcher;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.anyRequest().authenticated();
		//对受信任的主机不进行csrf过滤
//		http.csrf().requireCsrfProtectionMatcher(cusomCsrfMatcher);
	}
}
