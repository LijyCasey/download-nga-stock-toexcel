package pers.ljy.download.ngastack.toexcel.httpclient;

import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;

import pers.ljy.download.ngastack.toexcel.model.MessageDetail;

public class ExcelWriter {

	public static void write(String fileName, List<MessageDetail> msgs) {
		com.alibaba.excel.ExcelWriter excelwriter = null;
		try {
			excelwriter = EasyExcel.write(fileName, MessageDetail.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("测试").build();
            excelwriter.write(msgs, writeSheet);
		}finally {
			if(excelwriter!=null) {
				excelwriter.finish();
			}
		}
	}
}
