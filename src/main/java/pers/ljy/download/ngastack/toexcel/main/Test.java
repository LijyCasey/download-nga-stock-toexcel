package pers.ljy.download.ngastack.toexcel.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.ljy.download.ngastack.toexcel.httpclient.DataFetcher;
import pers.ljy.download.ngastack.toexcel.httpclient.DataProcessor;
import pers.ljy.download.ngastack.toexcel.httpclient.ExcelWriter;
import pers.ljy.download.ngastack.toexcel.model.MessageDetail;

public class Test {
	static Logger logger = LoggerFactory.getLogger(Test.class);

	static String tid = "16053925";
	
	static List<MessageDetail> details = new ArrayList<>();

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		for (int i = 4000; i < 9089; i++) {
			logger.info("第"+i+"页");
			Object rs = DataFetcher.fetchData(i, tid);
			DataProcessor.handleMessage(rs,details);
			Thread.sleep(100);
		}
//		System.out.println(details);
		ExcelWriter.write("C:\\Users\\Casey\\Documents\\test\\08312.xlsx", details);
	}
}
