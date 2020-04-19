package cn.bdqn.his.prescription.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return httpClientHelper.postForResponse(serverMedicineUrl + "/api/medicines/findBy", params, CURRENT_SERVER_URL);
    }
    
  

   
}
