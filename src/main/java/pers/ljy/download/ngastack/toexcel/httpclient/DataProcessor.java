package pers.ljy.download.ngastack.toexcel.httpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.jayway.jsonpath.JsonPath;

import pers.ljy.download.ngastack.toexcel.model.MessageDetail;

public class DataProcessor {
	private static final String QUOTE_PATTERN_str = "\\[quote\\](.+)\\[\\/quote\\]";

	private static final String IMG_PATTERN_Str = "\\[img\\].+?\\[\\/img\\]";

	private static final String B_PATTERN_str = "<b>.+<\\/b>";

	private static final String BR_PATTERN_str = "<br\\/>";

	private static final String PID_PATTERN_str = "\\[pid=(.*)\\[(.*)pid]";

	private static final Pattern IMG_PATTERN = Pattern.compile("\\[img\\](.+?)\\[\\/img\\]");

	private static final Pattern QUOTE_PATTERN = Pattern.compile(QUOTE_PATTERN_str);

	public static void handleMessage(Object msgDetail, List<MessageDetail> mDetails) {
		if (msgDetail == null) {
			return;
		}try {
			int rsSize = JsonPath.read(msgDetail, "$.result.length()");
			for (int i = 0; i < rsSize; i++) {
				Object rs = JsonPath.read(msgDetail, "$.result[" + i + "]");
				// 先看作者
				Integer uid = (Integer) JsonPath.read(rs, "$.author.uid");
				String authorName = Author.authors.get(uid);
				if (StringUtils.isEmpty(authorName)) {
					continue;
				}
				String postTime = JsonPath.read(rs, "$.postdate");
				String contentStr = JsonPath.read(rs, "$.content");
				// 获取引用
				String replyContent = resolveReply(contentStr);
				// 去掉引用
				contentStr = excludeQuote(contentStr);
				// 去掉回复
				contentStr = excludeReply(contentStr);
				// 处理图片
				List<String> img_url = new ArrayList<>();
				Matcher matcher = IMG_PATTERN.matcher(contentStr);
				while (matcher.find()) {
					img_url.add(matcher.group(1));
				}
				contentStr = excludeImg(contentStr);
				MessageDetail detail = new MessageDetail();
				detail.setAuthor(authorName);
				detail.setDate(postTime);
				detail.setContent(contentStr);
				detail.setFloor(JsonPath.read(rs, "$.lou").toString());
				detail.setReply(replyContent);
				mDetails.add(detail);
			}
		}catch(Exception e) {
			//ignore
		}
		
	}

	private static String excludeImg(String contentStr) {
		// 把图片内容去掉
		String rs = contentStr.replaceAll(IMG_PATTERN_Str, "");
		return rs;
	}

	// 处理主消息
	private static String excludeQuote(String contentStr) {
		// 把引用内容去掉
		String rs = contentStr.replaceAll(QUOTE_PATTERN_str, "");
		rs = rs.replaceAll(BR_PATTERN_str, "\n");
		return rs;
	}

	// 处理回复的消息
	private static String resolveReply(String contentStr) {
		Matcher matcher = QUOTE_PATTERN.matcher(contentStr);
		if (matcher.find()) {
			try {
				String replyContent = matcher.group(1);
				replyContent = replyContent.replaceAll(IMG_PATTERN_Str, "[图片]");
				replyContent = replyContent.replaceAll(BR_PATTERN_str, "");
				replyContent = replyContent.replaceAll(PID_PATTERN_str, "");
				replyContent = replyContent.replaceAll(B_PATTERN_str, "");
				return replyContent;
			} catch (Exception e) {
//				logger.error("有reply，但是报错了");
			}

		}
		return null;
	}

	private static String excludeReply(String contentStr) {
		String rs = contentStr.replaceAll("<b>Reply to(.+?)<br/>", "");
		return rs;
	}
}
