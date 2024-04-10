package method.qr.kiarelemb.utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:20
 * @apiNote 资源文件的读取工具类
 */
public class QRPropertiesUtils {
	/**
	 * 加载资源文件
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param path 该资源文件的保存路径
	 */
	public static void loadProp(Properties prop, String path) {
		if (prop != null) {
			try {
				FileInputStream fis = new FileInputStream(path);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				BufferedReader in = new BufferedReader(isr);
				prop.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载资源文件
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param url 该资源文件的保存路径
	 */
	public static void loadProp(Properties prop, URL url) {
		if (prop != null) {
			try {
				prop.load(url.openStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Properties loadProp(String path) {
		Properties prop = new Properties();
		loadProp(prop, path);
		return prop;
	}

	public static Properties loadProp(URL url) {
		Properties prop = new Properties();
		loadProp(prop, url);
		return prop;
	}

	/**
	 * 保存资源文件
	 *
	 * @param prop 已实例化的资源文件变量
	 */
	public static void storeProp(Properties prop, String path) {
		if (prop != null && prop.size() != 0) {
			try {
				FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(fileWriter);
				prop.store(bw, null);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 传入Properties与key值，将结果转为int值
	 */
	public static int getPropInInteger(Properties prop, String key) {
		return getPropInInteger(prop, key, 0);
	}

	public static int getPropInInteger(Properties prop, String key, int defaultValue) {
		return getPropInInteger(prop, key, defaultValue, false);
	}

	public static int getPropInInteger(Properties prop, String key, int defaultValue, boolean nullSet) {
		String property = prop.getProperty(key);
		if (property == null) {
			if (nullSet) {
				prop.setProperty(key, String.valueOf(defaultValue));
			}
			return defaultValue;
		}
		if (QRStringUtils.isNumber(property)) {
			return Integer.parseInt(property);
		}
		return defaultValue;
	}


	/**
	 * 传入Properties与key值，将结果转为short值
	 */
	public static short getPropInShort(Properties prop, String key) {
		short s = 0;
		return getPropInShort(prop, key, s);
	}

	public static short getPropInShort(Properties prop, String key, short defaultValue) {
		return getPropInShort(prop, key, defaultValue, false);
	}

	public static short getPropInShort(Properties prop, String key, short defaultValue, boolean nullSet) {
		String property = prop.getProperty(key);
		if (property == null) {
			if (nullSet) {
				prop.setProperty(key, String.valueOf(defaultValue));
			}
			return defaultValue;
		}
		if (QRStringUtils.isNumber(property)) {
			return Short.parseShort(property);
		}
		return defaultValue;
	}

	/**
	 * 传入Properties与key值，将结果转为long值
	 */
	public static long getPropInLong(Properties prop, String key) {
		return getPropInLong(prop, key, 0);
	}

	public static long getPropInLong(Properties prop, String key, long defaultValue) {
		return getPropInLong(prop, key, defaultValue, false);
	}

	public static long getPropInLong(Properties prop, String key, long defaultValue, boolean nullSet) {
		String property = prop.getProperty(key);
		if (property == null) {
			if (nullSet) {
				prop.setProperty(key, String.valueOf(defaultValue));
			}
			return defaultValue;
		}
		if (QRStringUtils.isNumber(property)) {
			return Long.parseLong(property);
		}
		return defaultValue;
	}

	public static boolean getPropInBoolean(Properties prop, String key) {
		return getPropInBoolean(prop, key, false);
	}

	public static boolean getPropInBoolean(Properties prop, String key, boolean defaultValue) {
		return getPropInBoolean(prop, key, defaultValue, false);
	}

	public static boolean getPropInBoolean(Properties prop, String key, boolean defaultValue, boolean nullSet) {
		String property = prop.getProperty(key);
		if (property == null) {
			if (nullSet) {
				prop.setProperty(key, String.valueOf(defaultValue));
			}
			return defaultValue;
		}
		return Boolean.parseBoolean(property);
	}

	/**
	 * 传入Properties与key值，将结果转为float值
	 */
	public static Float getPropInFloat(Properties prop, String key) {
		return getPropInFloat(prop, key, 1.0f);
	}

	public static Float getPropInFloat(Properties prop, String key, float defaultValue) {
		return getPropInFloat(prop, key, defaultValue, false);
	}

	/**
	 * 传入Properties与key值，将结果转为float值
	 */
	public static Float getPropInFloat(Properties prop, String key, float defaultValue, boolean nullSet) {
		String property = prop.getProperty(key);
		if (property == null) {
			if (nullSet) {
				prop.setProperty(key, String.valueOf(defaultValue));
			}
			return defaultValue;
		}
		try {
			return Float.parseFloat(property);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}


	/**
	 * 传入Properties与key值，将结果转为double值
	 */
	public static double getPropInDouble(Properties prop, String key) {
		return getPropInDouble(prop, key, 0);
	}

	public static double getPropInDouble(Properties prop, String key, double defaultValue) {
		return getPropInDouble(prop, key, defaultValue, false);
	}

	public static double getPropInDouble(Properties prop, String key, double defaultValue, boolean nullSet) {
		String property = prop.getProperty(key);
		if (property == null) {
			if (nullSet) {
				prop.setProperty(key, String.valueOf(defaultValue));
			}
			return defaultValue;
		}
		if (QRStringUtils.isNumber(property)) {
			return Double.parseDouble(property);
		}
		return defaultValue;
	}

	public static String getPropInString(Properties prop, String key, String defaultValue) {
		return getPropInString(prop, key, defaultValue, false);
	}

	public static String getPropInString(Properties prop, String key, String defaultValue, boolean nullSet) {
		String value = prop.getProperty(key);
		if (value == null) {
			if (nullSet) {
				prop.setProperty(key, defaultValue);
			}
			return defaultValue;
		}
		return value;
	}
}