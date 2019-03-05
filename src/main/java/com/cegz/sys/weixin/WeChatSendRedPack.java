package com.cegz.sys.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.ssl.SSLContexts;

import com.cegz.sys.weixin.domain.SendRedPack;
import com.cegz.sys.weixin.util.Tool;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Platform;

/**
 * 微信红包
 */
public class WeChatSendRedPack {

	public static void main(String[] args) throws Exception {

		// 具体参数查看具体实体类，实体类中的的参数参考微信的红包发放接口，这里你直接用map，进行设置参数也可以。。。
//		SendRedPack sendRedPack = new SendRedPack("随机字符串不超过32位", "随机订单号，不超过32位", "商户号", "公众号appid", "桑博",
//				"填写接受人的openid", 100, 1, "萌萌哒", "127.0.0.1", "桑博红包", "桑博", "PRODUCT_5");
		SendRedPack sendRedPack = new SendRedPack(Tool.returnUUID(), Tool.getOrderNum(), "1511379171",
//				"wxd5b895806209b842", "车而告之", "oW6OP4mHcESAZpqiWGUrlCQr_tZw", 100, 1, "动次打次", "www.cegzm.com", "车而告之红包",
				"wxd5b895806209b842", "车而告之", "oVZKK5Ub2jIC-VD0W-KRyF8pi9c8", 100, 1, "TLONG", "www.cegzm.com", "车而告之红包",
				"车而告之", "PRODUCT_5");

		// 将实体类转换为url形式
		String urlParamsByMap = Tool.getUrlParamsByMap(Tool.toMap(sendRedPack));
		// 拼接我们再前期准备好的API密钥，前期准备第5条
//		urlParamsByMap += "&key=填写API密钥";
		urlParamsByMap += "&key=a331b4d195fe52c3a95bbcc660910777";
		// 进行签名，需要说明的是，如果内容包含中文的话，要使用utf-8进行md5签名，不然会签名错误
		String sign = Tool.parseStrToMd5L32(urlParamsByMap).toUpperCase();
		sendRedPack.setSign(sign);
		// 微信要求按照参数名ASCII字典序排序，这里巧用treeMap进行字典排序
		TreeMap treeMap = new TreeMap(Tool.toMap(sendRedPack));
		// 然后转换成xml格式
		String soapRequestData = Tool.getSoapRequestData(treeMap);
		// 发起请求前准备
		RequestBody body = RequestBody.create(MediaType.parse("text/xml;charset=UTF-8"), soapRequestData);
		Request request = new Request.Builder().url("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack")
				.post(body).build();
		// 为http请求设置证书
		SSLSocketFactory socketFactory = getSSL().getSocketFactory();
		X509TrustManager x509TrustManager = Platform.get().trustManager(socketFactory);
		OkHttpClient okHttpClient = new OkHttpClient.Builder().sslSocketFactory(socketFactory, x509TrustManager)
				.build();
		// 得到输出内容
		Response response = okHttpClient.newCall(request).execute();
		String content = response.body().string();
		System.out.println(content);

	}

	public static SSLContext getSSL() throws KeyStoreException, IOException, CertificateException,
			NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// 证书位置自己定义
		InputStream resourceAsStream = Object.class.getResourceAsStream("/apiclient_cert.p12");
//		FileInputStream instream = (FileInputStream) resourceAsStream;
		try {
			keyStore.load(resourceAsStream, "1511379171".toCharArray());// 填写证书密码，默认为商户号
		} finally {
			resourceAsStream.close();
		}
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1511379171".toCharArray()).build();// 填写证书密码，默认为商户号
		return sslcontext;
	}

}
