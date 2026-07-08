package method.qr.kiarelemb.utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Properties;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:20
 * @apiNote 资源文件的读取工具类
 */
public class QRPropertiesUtils {


	/**
	 * 加载属性文件。
	 * <p>
	 * 注意：该方法非 {@link Properties#load(InputStream)} 原生语法，而是本项目的轻量解析：
	 * 使用 UTF-8 按行读取；整行以 {@code !} 开头会被忽略；行内 {@code #} 后的内容会被当作注释删除；
	 * 仅当一行按 {@code =} 拆分后刚好得到两个字段时才写入 {@code prop}。
	 * 因此 value 中不能直接包含额外的 {@code =}。
	 * <p>
	 * 使用例：
	 * <pre>{@code
	 * Properties prop = new Properties();
	 * QRPropertiesUtils.loadProp(prop, "res/settings/window.properties");
	 * int width = QRPropertiesUtils.getPropInInteger(prop, "window.width", 1280, true);
	 * }</pre>
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param path 该资源文件的保存路径
	 */
	public static void loadProp0(Properties prop, String path) {
		loadProp(prop, path);
	}

	/**
	 * 加载资源文件。
	 * 注意，该方法非原生读取方法，而是自定义的读取方法
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param stream 该资源文件的读取流
	 */
	public static void loadProp0(Properties prop, InputStream stream) {
		loadProp(prop, stream);
	}


	/**
	 * 以本项目的简洁格式保存属性文件。
	 * <p>
	 * 每个属性会保存为一行 {@code key=value}，不写入时间戳、转义字符或 Unicode 转义。
	 * 写入使用 UTF-8，并通过 {@link QRFileUtils#fileWriterWithUTF8(String, java.util.List)}
	 * 覆盖原文件。
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param path 该资源文件的保存路径
	 */
	public static void storeProp0(Properties prop, String path) {
		LinkedList<String> lines = new LinkedList<>();
		for (Object object : prop.keySet()) {
			lines.add(object.toString() + "=" + prop.getProperty(object.toString()));
		}
		QRFileUtils.fileWriterWithUTF8(path, lines);
	}

	/**
	 * 加载资源文件。
	 * 注意，该方法非原生读取方法，而是自定义的读取方法
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param path 该资源文件的保存路径
	 */
	public static void loadProp(Properties prop, String path) {
		if (prop != null) {
			try {
				FileInputStream fis = new FileInputStream(path);
				loadProp(prop, fis);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载资源文件。
	 * 注意，该方法非原生读取方法，而是自定义的读取方法
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param path 该资源文件的保存路径
	 */
	public static void loadProp(Properties prop, InputStream stream) {
		if (prop != null) {
			QRFileUtils.fileReader(stream, StandardCharsets.UTF_8, lineText -> {
				lineText = lineText.trim();
				if (lineText.startsWith("!")) return;
				int index = lineText.indexOf('#');
				if (index != -1) {
					lineText = lineText.substring(0, index).trim();
				}
				String[] split = lineText.split("=");
				if (split.length == 2) {
					prop.setProperty(split[0].trim(), split[1].trim());
				}
			});
		}
	}

	/**
	 * 加载资源文件
	 *
	 * @param prop 已实例化的资源文件变量
	 * @param url  该资源文件的保存路径
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
		if (prop != null && !prop.isEmpty()) {
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
	 * 读取整数属性，缺失或不是数字时返回默认值。
	 * <p>
	 * {@code nullSet} 为 {@code true} 且 key 不存在时，会把默认值写回到传入的
	 * {@link Properties} 对象中；本方法不会自动持久化到文件，需要调用方再执行保存。
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
