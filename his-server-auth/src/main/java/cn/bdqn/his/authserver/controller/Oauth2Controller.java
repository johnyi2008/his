package cn.bdqn.his.authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
//必须配置
@SessionAttributes("authorizationRequest")
public class Oauth2Controller {

	@RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation() throws Exception {
        return "oauth/confirm_access";
    }
	
	@GetMapping("/custom/login")
	public String login() {
		return "oauth/login";
	}
}
