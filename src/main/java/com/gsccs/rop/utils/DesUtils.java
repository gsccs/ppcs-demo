package com.gsccs.rop.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class DesUtils {

	public static byte[] desCrypto(byte[] datasource, String password) {
		try {
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(password.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, String password) throws Exception {
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(password.getBytes());
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(password.getBytes());
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
 
        return cipher.doFinal(data);
    }

	
	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp= df.format(new Date());
		//待加密内容  
		String str = timestamp+"aop123456";  
		//密码，长度要是8的倍数  
		String password = "abcdefgh";  
		byte[] result = DesUtils.desCrypto(str.getBytes(),password);  
		String strs = Base64.encodeBase64String(result);
				//Base64.getEncoder().encodeToString(result);
		
		System.out.println("加密后内容为："+strs);  
		  
		try {  
		   byte[] decryResult = DesUtils.decrypt(result, password);  
		   System.out.println("解密后内容为："+new String(decryResult));  
		} catch (Exception e1) {  
		        e1.printStackTrace();  
		} 
	}
}
