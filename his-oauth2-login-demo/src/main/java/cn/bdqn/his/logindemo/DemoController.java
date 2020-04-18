package cn.bdqn.his.logindemo;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.bdqn.his.common.http.HttpClientHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DemoController {
    @Autowired private HttpClientHelper httpClientHelper;

    /**
     * 第一步，获取code（授权码）
     */
    @GetMapping("/getCode")
    public void getCode(HttpServletResponse response) throws Exception {
        String url = "http://localhost:9001/oauth/authorize?client_id=client3"
                + "&redirect_uri=http://localhost:8080/login"
                + "&response_type=code";
        response.sendRedirect(url);
    }

    /**
     * 第二步，回调地址(需要在授权服务器的oauth_client_details表中存在对应记录)
     * 通过授权码(code)换取access_token
     * @param code
     * @return
     */
    @GetMapping("/login")
    public String login(String code) throws Exception {
        String url = "http://localhost:9001/oauth/token"
                + "?client_id=client3"
                + "&client_secret=secret"
                + "&grant_type=authorization_code"
                + "&redirect_uri=http://localhost:8080/login"
                + "&code=" + code
                + "&scope=app";
        String result = httpClientHelper.post(url);
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = om.readValue(result, Map.class);
        String access_token = (String) map.get("access_token");
        return "redirect:get_user_info?accessToken=" + access_token;
    }

    /**
     * 第三步，通过access_token获取资源(调用资源服务器上的api)
     * @param accessToken
     */
    @GetMapping("get_user_info")
    @ResponseBody
    public Object getUserInfo(String accessToken) throws Exception {
        String url = "http://localhost:9002/content/list?categoryId=1" +
				"&access_token=" + accessToken;
        return httpClientHelper.post(url);
    }
}




