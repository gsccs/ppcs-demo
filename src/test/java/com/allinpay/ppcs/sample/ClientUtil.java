package com.allinpay.ppcs.sample;

import java.net.InetAddress;
import java.text.SimpleDateFormat;

public class ClientUtil {

	public static final String SERVER_URL = "http://116.228.64.55:8080/aop/rest";
    
    public static final String APPKEY = "test";
    public static final String SECRET = "test";
    public static final String BRH_ID = "0229000003";				//发卡机构
    public static final String PRDT_NO = "0001";					//产品
    public static final String BRAND_NO = "0015";					//品牌
    public static final String MER_ID = "999581054623298";			//商户
    public static final String DES_KEY = "abcdefgh";
    
    
    
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public static void main(String[] args) {
    	//ClientUtil clientService = new ClientUtil();
    	//clientService.getCardInfo();
    	
    	InetAddress address;
    	  try {
    	   address =InetAddress.getByName("www.weibo.com");
    	   System.out.println("Name:" + address.getHostName());
    	   System.out.println("Addr:" + address.getHostAddress());
    	   System.out.println("Reach:" + address.isReachable(3000)); //是否能通信 返回true或false
    	   System.out.println(address.toString());
    	  } catch (Exception e) {
    	   e.printStackTrace();
    	  }
	}
    
    
}

