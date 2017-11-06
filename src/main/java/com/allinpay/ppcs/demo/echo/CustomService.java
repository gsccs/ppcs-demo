package com.allinpay.ppcs.demo.echo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.allinpay.ppcs.demo.utils.RopUtils;

/**
 * 客户服务API
 * @author x.d zhang
 *
 */
public class CustomService {

    public static final String SERVER_URL = "http://116.228.64.55:8080/aop/rest";
    
    public static final String SERVER_APPKEY = "test";
    public static final String SERVER_SECRET = "test";
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    
    
    public static void main(String[] args) {
    	CustomService apiService = new CustomService();
    	apiService.cardOpen();
	}

    /**
     * 卡信息查询
     */
    public void cardOpen() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        
        //系统级请求参数
        form.add("method", "allinpay.ppcs.cloud.card.open");
        form.add("app_key", SERVER_APPKEY);
        form.add("v", "1.0");
        form.add("format", "xml");
        form.add("timestamp", df.format(new Date()));
        

        //对请求参数列表进行签名
        String sign = RopUtils.sign(form.toSingleValueMap(), SERVER_SECRET);
        form.add("sign", sign);
        form.add("sign_v", "1");
        
        //业务参数
        String orderid = String.valueOf(System.currentTimeMillis());
        form.add("order_id", orderid);
        form.add("brh_id", "0229000003");
        form.add("brand_no", "0015");
        form.add("phone_num", "15393155606");
        form.add("password", "123456");
        form.add("chan_no", "json");
        

        String response = restTemplate.postForObject(
                SERVER_URL, form, String.class);
        System.out.println("response:\n" + response);
//        assertTrue(response.indexOf("<logonResponse sessionId=\"mockSessionId1\"/>") > -1);
    }
}

