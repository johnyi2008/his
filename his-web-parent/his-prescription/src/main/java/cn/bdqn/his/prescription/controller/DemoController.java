package cn.bdqn.his.prescription.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.bdqn.his.common.http.HttpClientHelper;
import cn.bdqn.his.common.response.Response;
import cn.bdqn.his.common.response.ResponseEnum;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * api调用demo
 * @author Administrator
 *
 */
@Api
@RestController
@Slf4j
public class DemoController {
	@Value("${server.medicine.url}")
	private String serverMedicineUrl;
	@Value("${server.masterdata.url}")
	private String serverMasterdataUrl;

	@Autowired private HttpClientHelper httpClientHelper;

	@GetMapping("api/medicine/findBy")
	public Response medicineFindBy(Integer pageNum,Integer pageSize, Integer typeId, String name, HttpServletRequest request) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			StringBuffer params = new StringBuffer("?");
			params.append("pageNum=").append(pageNum==null?1:pageNum);
			params.append("&pageSize=").append(pageSize==null?2:pageSize);
			params.append("&typeId=").append(typeId==null?1:typeId);
			params.append("&name=").append(name==null?"":name.trim());
			String ssoCookies = (String) request.getAttribute("ssoCookies");
			HttpGet httpGet = new HttpGet(serverMedicineUrl + "/api/medicines/findBy" + params);
			httpGet.addHeader("Cookie",ssoCookies);//带上cookie，表明身份
			response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				return new ObjectMapper().readValue(result, Response.class);
			}
		} catch (Exception e) {
			log.error("调用药品模块错误",e);
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return new Response(ResponseEnum.ERROR).setResponseBody("出错了");
	}
	
	@GetMapping("api/masterdata/users")
	public Response getMasterdataUsers() {
		return httpClientHelper.getForResponse(serverMasterdataUrl + "/api/masterdata/users");
	}

	@GetMapping("api/medicine/users")
	public Response getUsers() {
		return httpClientHelper.getForResponse(serverMedicineUrl + "/api/medicine/users");
	}
	
	@GetMapping("api/prescription/user")
	public Response user() {
		 Authentication user = SecurityContextHolder.getContext().getAuthentication();
		return new Response(ResponseEnum.SUCCESS).setResponseBody(user);
	}
}
