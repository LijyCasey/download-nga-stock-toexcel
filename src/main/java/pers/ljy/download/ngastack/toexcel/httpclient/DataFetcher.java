package pers.ljy.download.ngastack.toexcel.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;

public class DataFetcher {
	static Logger logger = LoggerFactory.getLogger(DataFetcher.class);
	public static String URL = "http://ngabbs.com/app_api.php?__lib=post&__act=list";

	public static Map<String, String> HEADERS = new HashMap<>();

	public static Map<String, String> DINGDING_HEADERS = new HashMap<>();

	static {
		HEADERS.put("Content-Type", "application/x-www-form-urlencoded");
		HEADERS.put("X-User-Agent", "NGA_skull/6.0.7(iPhone11,6;iOS 12.2)");
		HEADERS.put("User-Agent", "NGA/6.0.7 (iPhone; iOS 12.2; Scale/3.00)");
		HEADERS.put("Accept-Language", "zh-Hans-CN;q=1");
		DINGDING_HEADERS.put("Content-Type", "application/json");
	}

	public static Object fetchData(int page, String tid) throws ClientProtocolException, IOException {
		Map<String, String> param = new HashMap<>();
		param.put("tid", tid);
		param.put("page", page + "");
		try {
			String allRs = post(URL, HEADERS, param);
			Configuration conf = Configuration.defaultConfiguration();
			Object document = conf.jsonProvider().parse(allRs);
			return document;
		} catch (Exception e) {
			logger.error("第【"+page+"】页出错了",e);
			e.printStackTrace();
		}
		return null;
	}

	public static String post(String url, Map<String, String> headers, Map<String, String> param)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		headers.forEach((k, v) -> {
			post.setHeader(k, v);
		});
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Iterator<Map.Entry<String, String>> it = new TreeMap(param).entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> me = it.next();
			nvps.add(new BasicNameValuePair(me.getKey(), me.getValue()));
		}
		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		HttpResponse response = httpclient.execute(post);
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);
		httpclient.close();
		return body;
	}
}
