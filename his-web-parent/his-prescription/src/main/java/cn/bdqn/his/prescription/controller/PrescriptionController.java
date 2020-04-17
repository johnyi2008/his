package cn.bdqn.his.prescription.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.bdqn.his.common.CusomCsrfMatcher;
import cn.bdqn.his.common.http.HttpClientHelper;
import cn.bdqn.his.common.response.Response;
import cn.bdqn.his.common.response.ResponseEnum;
import cn.bdqn.his.prescription.dto.DemoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api
@RestController
@Slf4j
@RequestMapping("/api")
public class PrescriptionController {
    @Value("${server.medicine.url}")
    private String serverMedicineUrl;
    @Value("${server.masterdata.url}")
    private String serverMasterdataUrl;
    private static final String CURRENT_SERVER_URL = "http://localhost:9005";
    @Resource
    private HttpClientHelper httpClientHelper;
    

    @PostMapping("add")
    public Response add(DemoDto dto, HttpServletRequest request) {
    	log.info(dto.toString());
    	return new Response(ResponseEnum.SUCCESS).setResponseBody("success");
    }
    @ApiOperation(value = "调用药品模块api,分页查询药品列表",response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码，查询第几页数据,必填",name = "pageNum", required = true),
            @ApiImplicitParam(value = "每页显示几条,必填",name = "pageSize", required = true)
    })
    @GetMapping("getMedicines")
    public Response getMedicines(Integer pageNum, Integer pageSize) {
        return httpClientHelper.getForResponse(serverMedicineUrl + "/api/medicines?pageNum="+pageNum+"&pageSize="+pageSize);
    }
    @ApiOperation(value = "调用药品模块api,分页查询药品列表",response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码，查询第几页数据,必填",name = "pageNum", required = true),
            @ApiImplicitParam(value = "每页显示几条,必填",name = "pageSize", required = true),
            @ApiImplicitParam(value = "药品分类ID",name = "typeId"),
            @ApiImplicitParam(value = "药品名称，模糊匹配",name = "name")
    })
    @GetMapping(value = "getMedicines/findBy")
    public Response getMedicines(Integer pageNum, Integer pageSize, Integer typeId, String name) throws Exception {
        if(pageNum == null) pageNum = 1;
        if(pageSize == null) pageSize = 5;
        StringBuffer buffer = new StringBuffer("?").append("pageNum=").append(pageNum).append("&pageSize=").append(pageSize);
        if(typeId!=null) {
            buffer.append("&typeId=").append(typeId);
        }
        if(!StringUtils.isEmpty(name)) {
            buffer.append("&name=").append(URLEncoder.encode(name, "UTF-8"));
        }
        log.debug("params:{}", buffer);
        return httpClientHelper.getForResponse(serverMedicineUrl + "/api/medicines/findBy"+ buffer);
    }
    
    @GetMapping(value = "getMedicines/findByPost")
    public Response findByPost(Integer pageNum, Integer pageSize, Integer typeId, String name, HttpServletRequest request) throws Exception {
        if(pageNum == null) pageNum = 1;
        if(pageSize == null) pageSize = 5;
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("typeId", typeId);
        params.put("name", name);
        //这个没有用,这是保护自己不被跨域攻击的csrftoken,发送到目标服务器还是会被报csrf无效
        //除非先发一个get请求到目标服务器获取对应的csrftoken再传回去
//        CsrfToken _csrf = (CsrfToken) request.getAttribute("_csrf");
//        params.put(_csrf.getParameterName(), _csrf.getToken());
        log.debug("params:{}", params);
        CloseableHttpClient  httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse  response = null;
		try {
			String ssoCookies = (String) request.getAttribute("ssoCookies");
			HttpPost httpPost = new HttpPost(serverMedicineUrl + "/api/medicines/findBy");
			httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
			httpPost.setHeader("Cookie", ssoCookies);
			//被调用的服务器默认spring security会阻止post跨站访问
            //在被调用的服务器security配置中加入了允许的主机地址
			httpPost.setHeader(CusomCsrfMatcher.HEADER_NAME, CURRENT_SERVER_URL);
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			params.forEach((k,v) -> {
				if(v != null) {
					nameValuePairs.add(new BasicNameValuePair(k, v.toString()));
				}
			});
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,StandardCharsets.UTF_8));
			response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			String result = null;
			if(httpEntity != null) {
				result = EntityUtils.toString(httpEntity,StandardCharsets.UTF_8);
				if(log.isDebugEnabled()) {
					log.debug("响应内容：{}",result);
				}
				if(response.getStatusLine().getStatusCode() == 200) {
					return new ObjectMapper().readValue(result,Response.class);
				}
			}
		} catch(Exception ex) {
			log.error(ex.getMessage(),ex);
		} finally {
			HttpClientUtils.closeQuietly(httpClient);
			HttpClientUtils.closeQuietly(response);
		}
		return new Response(ResponseEnum.ERROR).setResponseBody("出错了");
    }
    
  

   
}
