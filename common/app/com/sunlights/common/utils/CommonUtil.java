package com.sunlights.common.utils;

import com.sunlights.common.AppConst;
import com.sunlights.common.MsgCode;
import com.sunlights.common.Severity;
import com.sunlights.common.exceptions.BusinessRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import play.Logger;
import play.mvc.Http;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static play.mvc.Controller.request;

/**
 * <p>Project: fsp</p>
 * <p>Title: CommonUtil.java</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2014 Sunlights.cc</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:jiaming.wang@sunlights.cc">wangJiaMing</a>
 */
public class CommonUtil {
	private static CommonUtil commonUtil = new CommonUtil();

	public static CommonUtil getInstance() {
		return commonUtil;
	}

	private static String BLANK = "--";

	public CommonUtil() {

	}

	/**
	 * 参数验证
	 *
	 * @param params
	 */
	public void validateParams(String... params) {
		for (String param : params) {
			if (StringUtils.isEmpty(param)) {
				throw errorBusinessException(MsgCode.ACCESS_FAIL, param);
			}
		}
	}

	public BusinessRuntimeException errorBusinessException(MsgCode msgCode, Object... params) {
		String detail = getDetail(msgCode, params);
		return new BusinessRuntimeException(Severity.ERROR, msgCode.getCode(), msgCode.getMessage(), detail);
	}

	private String getDetail(MsgCode msgCode, Object[] params) {
		String detail = msgCode.getDetail();
		if (params != null) {
			detail = MessageFormat.format(detail, params);
		}
		return detail;
	}

	public BusinessRuntimeException fatalBusinessException(MsgCode msgCode, Object... params) {
		String detail = getDetail(msgCode, params);
		return new BusinessRuntimeException(Severity.FATAL, msgCode.getCode(), msgCode.getMessage(), detail);
	}

	public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";

	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	public static final String DATE_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FORMAT_ICU = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DATE_FORMAT_SHUMI = "yyyy-MM-dd'T'HH:mm:ss.sss";

	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_SHORT);

	public static synchronized String dateToString(Date date, String... format) {
		if (date == null) {
			return "";
		}

		if (format != null && format.length > 0) {
			return new SimpleDateFormat(format[0]).format(date);
		} else {
			return DATE_FORMAT.format(date);
		}
	}

	public static synchronized Date stringToDate(String dateString, String... format) throws ParseException {
		if (StringUtils.isEmpty(dateString)) {
			return new Date();
		}

		if (format != null && format.length > 0) {
			return new SimpleDateFormat(format[0]).parse(dateString);
		} else {
			return DATE_FORMAT.parse(dateString);
		}
	}

	public static Date stringToDateTime(String dateString) {
		if (StringUtils.isEmpty(dateString)) {
			return null;
		}
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		return fmt.parseDateTime(dateString).toDate();
	}

	public static String format(String value) {
		return StringUtils.isEmpty(value) ? BLANK : value;
	}

	public static Integer format(Integer value) {
		return (value == null) ? 0 : value;
	}

	@Deprecated
	public static String getCurrentVersion(Http.Request request) {
		String userAgent = request.getHeader(AppConst.HEADER_USER_AGENT);
		// Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X)
		// AppleWebKit/537.51.2 (KHTML, like Gecko)
		// Mobile/11D167\jindoujialicai\1.2

		return getCurrentVersionFromStr(userAgent);
	}

	@Deprecated
	public static String getCurrentPlatform(Http.Request request) {
		String userAgent = request.getHeader(AppConst.HEADER_USER_AGENT);
		// Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X)
		// AppleWebKit/537.51.2 (KHTML, like Gecko)
		// Mobile/11D167\jindoujialicai\1.2

		return getCurrentPlatformFromStr(userAgent);
	}

	public static String getCurrentVersionFromStr(String userAgent) {
		// String userAgent = request.getHeader(AppConst.HEADER_USER_AGENT);
		// Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X)
		// AppleWebKit/537.51.2 (KHTML, like Gecko)
		// Mobile/11D167\jindoujialicai\1.2

		Logger.info(">>userAgent:" + userAgent);

		String name = "jindoujialicai";
		int index = userAgent.indexOf(name);
		if (index <= 0) {
			return "";
		}
		String version = userAgent.substring(index + name.length() + 1, userAgent.length());
		Logger.info(">>当前版本号：" + version);

		return version;
	}

	public static String getCurrentPlatformFromStr(String userAgent) {
		// String userAgent = request.getHeader(AppConst.HEADER_USER_AGENT);
		// Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X)
		// AppleWebKit/537.51.2 (KHTML, like Gecko)
		// Mobile/11D167\jindoujialicai\1.2
		// Mozilla/5.0 (Linux; U; Android 4.3; zh-; HUAWEI C8816
		// Build/HuaweiC8816) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0
		// Mobile Safari/534.30\jindoujialicai\1.2
		Logger.info(">>userAgent:" + userAgent);

		if (StringUtils.isEmpty(userAgent)) {// pc端
			return AppConst.PLATFORM_PC;
		}

		String name = "Android";
		int index = userAgent.indexOf(name);
		if (index > 0) {
			Logger.info(">>当前platform：" + AppConst.PLATFORM_ANDROID);
			return AppConst.PLATFORM_ANDROID;
		}

		name = "iPhone";
		index = userAgent.indexOf(name);
		if (index > 0) {
			Logger.info(">>当前platform：" + AppConst.PLATFORM_IOS);
			return AppConst.PLATFORM_IOS;
		}

		return AppConst.PLATFORM_PC;
	}

	public static void checkPlatform(String platform) {
		if (AppConst.PLATFORM_IOS.equals(platform) || AppConst.PLATFORM_ANDROID.equals(platform)) {
			return;
		}
		throw CommonUtil.getInstance().errorBusinessException(MsgCode.NOT_SUPPORT_PLATFORM);
	}


    /**
     * 判断当前请求客户端
     * @param channel
     * @return
     */
    public static boolean fromApp(String channel) {
        if(AppConst.CHANNEL_ANDROID.equals(channel) || AppConst.CHANNEL_IOS.equals(channel) || StringUtils.isEmpty(channel)) {
            return true;
        }
        return false ;
    }

    public static String getRequestToken(){
        final Http.Request request = request();
        Http.Cookie cookie = request.cookie(AppConst.TOKEN);
        String token = cookie == null ? null : cookie.value();
        return token;
    }

}
