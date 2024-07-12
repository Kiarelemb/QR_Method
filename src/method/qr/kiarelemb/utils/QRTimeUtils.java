package method.qr.kiarelemb.utils;

import method.qr.kiarelemb.utils.data.QRRestDayData;
import method.qr.kiarelemb.utils.data.QRStringRandomData;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:45
 * @apiNote 时间的工具类
 */
public class QRTimeUtils {
	private static final String WWW_NTSC_AC_CN = "http://www.ntsc.ac.cn";
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateAndTimeMMFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static String onlineDate = null;

	/**
	 * 计算两个时间的差值(秒)，出错则返回负数
	 */
	public static long getTimeDifference(String timeBefore, String timeAfter) {
		try {
			Date begin = timeFormat.parse(timeBefore);
			Date end = timeFormat.parse(timeAfter);
			return (end.getTime() - begin.getTime()) / 1000;
		} catch (ParseException e) {
			return -1;
		}
	}

	/**
	 * 计算两个时间的差值(秒)，出错则返回负数
	 */
	public static long getDateAndTimeDifference(String datBefore, String datAfter) {
		try {
			Date begin = dateAndTimeFormat.parse(datBefore);
			Date end = dateAndTimeFormat.parse(datAfter);
			return (end.getTime() - begin.getTime()) / 1000;
		} catch (ParseException e) {
			return -1;
		}
	}

	public static String getTimeNow() {
		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
		return f.format(today);
	}

	public static String getDateNow() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public static String getOnlineDate() {
		if (onlineDate == null) {
			OnlineDataUpdate();
		}
		return onlineDate;
	}

	public static void OnlineDataUpdate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try {
			URL url = new URL(WWW_NTSC_AC_CN);
			URLConnection uc = url.openConnection();
			uc.connect();
			long ld = uc.getDate();
			Date date = new Date(ld);
			onlineDate = sdf.format(date);
		} catch (Exception e) {
			onlineDate = LocalDate.now().toString();
		}
	}

	public static String getDateAndTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.now().format(formatter);
	}

	public static String getOnlineTime() {
		try {
			URL url = new URL(WWW_NTSC_AC_CN);
			URLConnection uc = url.openConnection();
			uc.connect();
			long ld = uc.getDate();
			Date date = new Date(ld);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
			return sdf.format(date);
		} catch (Exception e) {
			final String s = LocalTime.now().toString();
			int i = s.indexOf('.');
			if (i != -1) {
				return s.substring(0, i);
			} else {
				return s;
			}
		}
	}

	/**
	 * 从指定字符串中找到第几次出现指定字符位置，并切割
	 * 切割后的字符串实际是指定字符出现times - 1次
	 * 若text中未出现指定次数那么多次，则返回原字符串
	 */
	public static QRStringRandomData cutAtTimes(String text, char mark, int times) {
		int time = 0;
		int preIndex = -1;
		while (true) {
			preIndex = text.indexOf(mark, preIndex + 1);
			if (preIndex == -1) {
				preIndex = text.length();
				break;
			}
			if (++time == times) {
				break;
			}
		}
		return new QRStringRandomData(text.substring(0, preIndex), preIndex);
	}

	public static String getTime(long systemTime) {
		final Date date = new Date(systemTime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA);
		return sdf.format(date);
	}

	/**
	 * @param time 小时-分钟
	 * @return 返回 <code>LocalTime</code> 类型
	 */
	public static LocalTime getLocalTime(String time) {
		String[] split = time.split("-");
		return LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}

	public static int[] getBigLocalTime(String time) {
		int[] ints = QRArrayUtils.splitToInt(time, '-');
		return new int[]{
				ints[0], ints[1]
		};
	}

	/**
	 * 获取打字的时间
	 */
	public static String getTimeCost(double totalTimeInSec, double totalTimeInMin) {
		String timeCost;
		if (totalTimeInMin <= 1.0) {
			final String format = String.format("%.3f", totalTimeInSec);
			final String[] split = format.split("\\.");
			timeCost = split[0] + "." + split[1];
		} else {
			String fore = "";
			double rest = totalTimeInSec;
			int hour;
			if (totalTimeInMin > 60) {
				hour = (int) (totalTimeInMin / 60);
				rest -= hour * 60 * 60;
				fore = hour + ":";
			}
			int min = (int) (rest / 60);
			rest -= min * 60;
			final String s1 = String.format("%.3f", rest);
			final String[] split = s1.split("\\.");
			if (split[0].length() == 1) {
				split[0] = "0" + split[0];
			}
			timeCost = fore + min + ":" + split[0] + "." + split[1];
		}
		return timeCost;
	}

	public static int[] strSplitToThreeParts(String date, String regex) {
		int[] d = new int[3];
		String[] da = date.split(regex);
		for (int i = 0, daLength = da.length; i < daLength; i++) {
			d[i] = Integer.parseInt(da[i]);
		}
		return d;
	}

	public static LocalDateTime getLocalData(String dateAndTime) {
		//2021-09-29 01:08:08
		String[] split = dateAndTime.split(QRStringUtils.A_WHITE_SPACE);
		String date = split[0];
		String time = split[1];
		int[] dates = strSplitToThreeParts(date, "-");
		int[] times = strSplitToThreeParts(time, ":");
		return LocalDateTime.of(dates[0], dates[1], dates[2], times[0], times[1], times[2]);
	}

	public static LocalDate getLocalDate(String date) {
		//2021-09-29
		try {
			final int[] dates = strSplitToThreeParts(date, "-");
			return LocalDate.of(dates[0], dates[1], dates[2]);
		} catch (Exception e) {
			return null;
		}
	}

	public static QRRestDayData restDayAndTimeCount(String endDateAndTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		try {
			Date endTime = df.parse(endDateAndTime);
			final String nowStr = LocalDateTime.now().format(pattern);
			Date now = df.parse(nowStr);
			long l = endTime.getTime() - now.getTime();
			int double60 = 60 * 60;
			int day = (int) (l / (24 * double60 * 1000));
			int d = day;
			int year = 0;
			int month = 0;
			if (day > 365) {
				year = day / 365;
				day -= year * 365;
			}
			if (day > 30) {
				month = day / 30;
				day -= month * 30L;
			}
			int hour = (int) (l / (double60 * 1000) - d * 24);
			int min = (int) ((l / (60 * 1000)) - d * 24 * 60 - hour * 60);
			int sec = (int) (l / 1000 - d * 24 * double60 - hour * double60 - min * 60);
			return new QRRestDayData(year, month, day, hour, min, sec);
		} catch (ParseException e) {
			return QRRestDayData.getNullData();
		}
	}

	public static String getEndDateAndTime(int daysToAdd) {
		final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		final LocalDateTime countTime = LocalDateTime.now();
		final LocalDateTime endDateAndTime = countTime.plusDays(daysToAdd);
		return endDateAndTime.format(pattern);
	}

	public static boolean isDateAndTime(String dt) {
		if (!dt.contains(" ")) {
			return false;
		}
		if (dt.length() < 8) {
			return false;
		}
		final String[] split = dt.split(" ");
		if (split.length != 2) {
			return false;
		}
		final String[] date = split[0].split("-");
		if (date.length != 3) {
			return false;
		}
		final String[] time = split[1].split(":");
		return time.length == 3;
	}

	/**
	 * 计算当前年月日的星期顺序：从周日到周六
	 *
	 * @param year  年
	 * @param month 月
	 * @param day   日
	 * @return 返回区间 <code>[0, 6]</code> 的整数
	 */
	public static int getWeek(int year, int month, int day) {
		if (month < 3) {
			month += 12;
			year--;
		}
		return (day + 1 + 2 * month + 3 * (month + 1) / 5 + year + (year >> 2)
		        - year / 100 + year / 400) % 7;
	}

	/**
	 * 计算当前年月日的星期顺序：从周日到周六
	 *
	 * @param date 日期类
	 * @return 返回区间 <code>[0, 6]</code> 的整数
	 */
	public static int getWeek(LocalDate date) {
		int month = date.getMonthValue();
		int day = date.getDayOfMonth();
		int year = date.getYear();
		return getWeek(year, month, day);
	}
}