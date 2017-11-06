package com.allinpay.ppcs.demo.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @version 1.0
 */
public class RopUtils {
	
	public static final String ENCODING = "UTF-8";

	private static final Logger logger = LoggerFactory
			.getLogger(RopUtils.class);

	/**
	 * 使用<code>secret</code>对paramValues按以下算法进行签名： <br/>
	 * uppercase(hex(sha1(secretkey1value1key2value2...secret))
	 * 
	 * @param paramValues
	 *            参数列表
	 * @param secret
	 * @return
	 */
	public static String sign(Map<String, String> paramValues, String secret) {
		return sign(paramValues, null, secret);
	}

	/**
	 * 对paramValues进行签名，其中ignoreParamNames这些参数不参与签名
	 * 
	 * @param paramValues
	 * @param ignoreParamNames
	 * @param secret
	 * @return
	 */
	public static String sign(Map<String, String> paramValues,
			List<String> ignoreParamNames, String secret) {
		try {
			StringBuilder sb = new StringBuilder();
			List<String> paramNames = new ArrayList<String>(paramValues.size());
			paramNames.addAll(paramValues.keySet());
			if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
				for (String ignoreParamName : ignoreParamNames) {
					paramNames.remove(ignoreParamName);
				}
			}
			Collections.sort(paramNames);

			sb.append(secret);
			for (String paramName : paramNames) {
				sb.append(paramName).append(paramValues.get(paramName));
			}
			sb.append(secret);

			// byte[] sha1Digest = getSHA1Digest(sb.toString());
			// return byte2hex(sha1Digest);

			byte[] md5Digest = getMD5Digest(sb.toString());
			return byte2hex(md5Digest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String utf8Encoding(String value, String sourceCharsetName) {
		try {
			return new String(value.getBytes(sourceCharsetName), ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static byte[] getSHA1Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			bytes = md.digest(data.getBytes(ENCODING));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse.getMessage());
		}
		return bytes;
	}

	private static byte[] getMD5Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes(ENCODING));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse.getMessage());
		}
		return bytes;
	}

	/**
	 * 二进制转十六进制字符串
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().toUpperCase();
	}

	public static String signMD5(long timeStamp) {
		if (timeStamp < System.currentTimeMillis() - 1800000L) {
			return null;
		}
		try {
			
			String signStr = "com.gsccs.www.smart" + timeStamp;
			// String signStr = secret + timeStamp;
			String signCode = byte2hex(getMD5Digest(signStr));
			return signCode.toLowerCase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DES加密
	 * 
	 * @param datasource
	 *            数据源
	 * @param password
	 *            数据密钥
	 * @return
	 */
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
	 * 加密
	 * 
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 */
	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String des(byte[] datasource, String password) {
		try {
			return new String(desCrypto(datasource, password), ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(signMD5(1479189732419L));
	}

}
