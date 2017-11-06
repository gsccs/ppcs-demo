package com.allinpay.ppcs.demo.echo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.allinpay.ppcs.demo.utils.RopUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 卡云服务
 * @author x.d zhang
 *
 */
public class CloudService {

    
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    
    
    public static void main(String[] args) {
    	CloudService apiService = new CloudService();
    	//apiService.cardOpen();
    	//apiService.addFeeSingle("50000");
    	
    	apiService.getCardInfo();
    	apiService.cardPay("200", "", "1000");
    	apiService.getCardInfo();
    	apiService.cardPay("200", "100", "");
    	apiService.getCardInfo();
    	
    	//apiService.getCardInfoPwd();
    	
    	//apiService.cardLogSearch("20170620","20170621");
	}

    /**
     * 开卡
     */
    public void cardOpen() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        
        //系统级请求参数
        form.add("method", "allinpay.ppcs.cloud.card.open");
        form.add("app_key", ClientUtil.APPKEY);
        form.add("v", "1.0");
        form.add("format", "json");
        form.add("timestamp", df.format(new Date()));
        form.add("sign_v", "1");
        
        //业务参数
        String orderid = String.valueOf(System.currentTimeMillis());
        form.add("order_id", orderid);
        form.add("brh_id", ClientUtil.BRH_ID);
        form.add("brand_no", ClientUtil.BRAND_NO);
        form.add("phone_num", "15393155616");
        form.add("password", "123456");
        form.add("chan_no", "4000000002");
        
     	//对请求参数列表进行签名
        String sign = RopUtils.sign(form.toSingleValueMap(), ClientUtil.SECRET);
        form.add("sign", sign);
        
        
        String response = restTemplate.postForObject(
        		ClientUtil.SERVER_URL, form, String.class);
        System.out.println("response:\n" + response);
        
        //<?xml version="1.0" encoding="UTF-8" standalone="yes"?><ppcs_cloud_card_open_response><res_timestamp>20170828124011</res_timestamp><res_sign>B4BECE1E05F0F0894C9B4276CAE87910</res_sign><brh_id>0229000003</brh_id><card_id>2222220010002108259</card_id><order_id>1503894639168</order_id><phone_num>15393155606</phone_num><result>0</result><trans_no>0004184776</trans_no></ppcs_cloud_card_open_response>
        //{"ppcs_cloud_card_open_response":{"res_timestamp":20170828182438,"res_sign":"F60236692836B395E6830BAA7E137388","result":0,"trans_no":"0004185094","phone_num":15393155616,"brh_id":"0229000003","order_id":1503915315841,"card_id":2222220010002108267}}
    }

    /**
     * 单卡充值
     */
    public void addFeeSingle(String amount) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
      //系统级请求参数
        form.add("method", "allinpay.ppcs.cloud.cardsingletopup.add");
        form.add("app_key", ClientUtil.APPKEY);
        form.add("v", "1.0");
        form.add("format", "json");
        form.add("timestamp", df.format(new Date()));
        form.add("sign_v", "1");
        
      //业务参数
        String orderid = String.valueOf(System.currentTimeMillis());
        form.add("order_id", orderid);
        form.add("brh_id", ClientUtil.BRH_ID);
        form.add("brand_no", ClientUtil.BRAND_NO);
        form.add("card_id", "2222220010002108267");
        form.add("prdt_no", ClientUtil.PRDT_NO);	//产品号
        form.add("amount", amount);	//充值金额 以分为单位
        form.add("chan_no", "4000000002");
        form.add("top_up_way", "1");
        form.add("desn", "15393155606");
        
        //对请求参数列表进行签名
        String sign = RopUtils.sign(form.toSingleValueMap(), ClientUtil.SECRET);
        form.add("sign", sign);

        String response = restTemplate.postForObject(
        		ClientUtil.SERVER_URL, form, String.class);
        System.out.println("response:\n" + response);
        //assertTrue(response.indexOf("<createUserResponse") > -1);
    }

    /**
     * 免密卡信息
     */
    public void getCardInfo() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        //系统级请求参数
        form.add("method", "allinpay.ppcs.cardinfo.get");
        form.add("app_key", ClientUtil.APPKEY);
        form.add("v", "1.0");
        form.add("format", "json");
        form.add("timestamp", df.format(new Date()));
        form.add("sign_v", "1");
        
        //业务参数
        String orderid = String.valueOf(System.currentTimeMillis());
        form.add("order_id", orderid);
        form.add("card_id", "2222220010002108267");
        //对请求参数列表进行签名
        String sign = RopUtils.sign(form.toSingleValueMap(), ClientUtil.SECRET);
        form.add("sign", sign);
        
        String response = restTemplate.postForObject(
        		ClientUtil.SERVER_URL, form, String.class);
        JsonObject cardinfoObj = new JsonParser().parse(response).getAsJsonObject()
        		.get("ppcs_cardinfo_get_response").getAsJsonObject()
        		.get("card_info").getAsJsonObject();
        JsonArray cardProductArrays = cardinfoObj.getAsJsonObject("card_product_info_arrays").getAsJsonArray("card_product_info");
        for(int i=0;i<cardProductArrays.size();i++){
        	JsonObject cardProduct = cardProductArrays.get(i).getAsJsonObject();
        	System.out.println("product_id="+cardProduct.get("product_id").getAsString());
        	System.out.println("account_balance="+cardProduct.get("account_balance").getAsString());
        	
        }
        System.out.println("response:\n" + response);
    }


    /**
     * 卡密支付
     */
    public void cardPay(String amount,String user_fee,String discount_rate) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
      //系统级请求参数
        form.add("method", "allinpay.card.cloud.paywithpassword.add");
        form.add("app_key", ClientUtil.APPKEY);
        form.add("v", "1.0");
        form.add("format", "json");
        
        String timestamp= df.format(new Date());
        form.add("timestamp", timestamp);
        form.add("sign_v", "1");
        
      //业务参数
        String orderid = String.valueOf(System.currentTimeMillis());
        form.add("order_id", orderid);
        form.add("type", "01");
        form.add("chan_no", "4000000002");
        form.add("mer_id", ClientUtil.MER_ID);							//商户号
        form.add("mer_tm", df.format(new Date()));			//商户上送交易时间
        form.add("mer_order_id", String.valueOf(System.currentTimeMillis()));			//商户订单号
        form.add("pay_cur", "CNY");
        //CNY-
        form.add("payment_id", "0000000002");				//支付活动
        form.add("amount", amount);							//交易金额
        
        byte[] card_id_bytes = RopUtils.desCrypto((timestamp+"aop"+"2222220010002108267").getBytes(), ClientUtil.DES_KEY);
        String card_id_str =Base64.encodeBase64String(card_id_bytes); 
        		//Base64.getEncoder().encodeToString(card_id_bytes);
        
        byte[] pwd_bytes = RopUtils.desCrypto((timestamp+"aop"+"123456").getBytes(), ClientUtil.DES_KEY);
        String pwd_str = Base64.encodeBase64String(pwd_bytes);
        		//Base64.getEncoder().encodeToString(pwd_bytes);
        form.add("card_id", card_id_str);
        form.add("password", pwd_str);
       
        //form.add("misc", "商户上送的自定义信息");
        //form.add("extra_order_id", orderid);					//商品订单号1
        
        //form.add("ori_amount", "4000000002");					//折扣前金额
        if(StringUtils.isNotEmpty(discount_rate)){
        	//form.add("discount_rate", discount_rate);			//折扣率	单位为千分之一
        }
        
        if (StringUtils.isNotEmpty(user_fee)){
        	//form.add("user_fee", user_fee);					//用户手续费
        }
        
        //form.add("user_name", "用户名称");						//商户上送的用户名称
        //form.add("goods_name", "4000000002");					//商品名称
        //form.add("goods_type", "商品名称");
        //form.add("goods_id", "100000001");
        //form.add("goods_misc", "商品备注");
        
        //对请求参数列表进行签名
        String sign = RopUtils.sign(form.toSingleValueMap(), ClientUtil.SECRET);
        form.add("sign", sign);
        System.out.println("request:\n"+form.toString());
        String response = restTemplate.postForObject(
        		ClientUtil.SERVER_URL, form, String.class);
        System.out.println("response:\n" + response);
        //assertTrue(response.indexOf("{\"userId\":\"1\",") > -1);
    }

    /**
     * 消费交易查询
     */
    public void cardLogSearch(String begin_date,String end_date) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
      //系统级请求参数
        form.add("method", "allinpay.ppcs.txnlog.search");
        form.add("app_key", ClientUtil.APPKEY);
        form.add("v", "1.0");
        form.add("format", "json");
        form.add("timestamp", df.format(new Date()));
        form.add("sign_v", "1");
        
      //业务参数
        form.add("begin_date", begin_date);
        form.add("end_date", end_date);
        form.add("card_id", "2222220010002108267");
        form.add("page_no", "1");
        form.add("page_size", "5");		
        //form.add("ext_id", "");					//外部客户号
        //form.add("cust_id", "");					//客户号

        //对请求参数列表进行签名
        String sign = RopUtils.sign(form.toSingleValueMap(), ClientUtil.SECRET);
        form.add("sign", sign);
        System.out.println("request:\n"+form.toString());
        String response = restTemplate.postForObject(
        		ClientUtil.SERVER_URL, form, String.class);
        System.out.println("response:\n" + response);
        //assertTrue(response.indexOf("<createUserResponse ") > -1);
    }
    
    
    /**
     * 卡信息
     */
    public void getCardInfoPwd() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        //系统级请求参数
        form.add("method", "allinpay.card.cardinfo.get");
        form.add("app_key", ClientUtil.APPKEY);
        form.add("v", "1.0");
        form.add("format", "json");
        String timestamp= df.format(new Date());
        form.add("timestamp", timestamp);
        form.add("sign_v", "1");
        
        //业务参数
        form.add("card_id", "2222220010002108267");
        
        byte[] pwd_bytes = RopUtils.desCrypto((timestamp+"aop"+"123456").getBytes(), ClientUtil.DES_KEY);
        String pwd_str = Base64.encodeBase64String(pwd_bytes);
        		//Base64.getEncoder().encodeToString(pwd_bytes);
        
        form.add("password", pwd_str);
        
        //对请求参数列表进行签名
        String sign = RopUtils.sign(form.toSingleValueMap(), ClientUtil.SECRET);
        form.add("sign", sign);

        String response = restTemplate.postForObject(
        		ClientUtil.SERVER_URL, form, String.class);
        System.out.println("response:\n" + response);
    }

    
}

