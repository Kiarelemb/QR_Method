package method.qr.kiarelemb.file;

import method.qr.kiarelemb.collection.QRJoinCollectionUtils;
import method.qr.kiarelemb.file.action.QRFileReaderLineAction;
import method.qr.kiarelemb.string.QRStringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kiarelemb QR
 * @program: QR_Method
 * @description:
 * @create 2023-02-15 09:58
 **/
public class QRFileReaderUtils {

	/**
	 * 读取文件
	 *
	 * @param filePath 文件路径
	 * @return 载入文件行的列表，其对象默认情况下为 {@link LinkedList<String>}
	 */
	public static List<String> fileReaderInUTF8(String filePath) {
		return fileReader(new File(filePath), StandardCharsets.UTF_8, new LinkedList<>());
	}

	/**
	 * 读取文件
	 *
	 * @param file 文件对象
	 * @return 载入文件行的列表，其对象默认情况下为 {@link LinkedList<String>}
	 */
	public static List<String> fileReaderInUTF8(File file) {
		return fileReader(file, StandardCharsets.UTF_8, new LinkedList<>());
	}

	/**
	 * 读取文件
	 *
	 * @param filePath 文件路径
	 * @param list     传入的列表集合，默认情况下为 {@link LinkedList<String>}
	 * @return 载入文件行的列表
	 */
	public static List<String> fileReaderInUTF8(String filePath, List<String> list) {
		return fileReader(filePath, StandardCharsets.UTF_8, list);
	}

	/**
	 * 读取文件
	 *
	 * @param file 文件对象
	 * @param list 传入的列表集合，默认情况下为 {@link LinkedList<String>}
	 * @return 载入文件行的列表
	 */
	public static List<String> fileReaderInUTF8(File file, List<String> list) {
		return fileReader(file, StandardCharsets.UTF_8, list);
	}

	/**
	 * 读取文件，并将内容放置一个字符串中
	 *
	 * @param filePath 文件路径
	 * @return 文本全部内容
	 */
	public static String fileReaderInUTF8All(String filePath) {
		return fileReaderAll(filePath, StandardCharsets.UTF_8);
	}

	/**
	 * 读取文件，并将内容放置一个字符串中
	 *
	 * @param file 文件对象
	 * @return 文本全部内容
	 */
	public static String fileReaderInUTF8All(File file) {
		return fileReaderAll(file, StandardCharsets.UTF_8);
	}

	/**
	 * 读取文件
	 *
	 * @param filePath 文件路径
	 * @param charset  编码
	 * @return 载入文件行的列表，其对象默认情况下为 {@link LinkedList<String>}
	 */
	public static List<String> fileReader(String filePath, Charset charset) {
		return fileReader(new File(filePath), charset, new LinkedList<>());
	}

	/**
	 * 读取文件
	 *
	 * @param file    文件对象
	 * @param charset 编码
	 * @return 载入文件行的列表，其对象默认情况下为 {@link LinkedList<String>}
	 */
	public static List<String> fileReader(File file, Charset charset) {
		return fileReader(file, charset, new LinkedList<>());
	}

	/**
	 * 读取文件，并将内容放置一个字符串中
	 *
	 * @param filePath 文件路径
	 * @param charset  编码
	 * @return 文本全部内容
	 */
	public static String fileReaderAll(String filePath, Charset charset) {
		return fileReaderAll(new File(filePath), charset);
	}

	/**
	 * 读取文件，并将内容放置一个字符串中
	 *
	 * @param file    文件对象
	 * @param charset 编码
	 * @return 文本全部内容
	 */
	public static String fileReaderAll(File file, Charset charset) {
		List<String> list = fileReader(file, charset);
		return QRJoinCollectionUtils.join(list, QRStringUtils.AN_ENTER);
	}

	public static List<String> fileReader(String filePath, Charset charset, List<String> list) {
		return fileReader(new File(filePath), charset, list);
	}

	/**
	 * 读取文件
	 *
	 * @param file    文件对象
	 * @param charset 编码
	 * @param list    传入的列表集合，默认情况下为 {@link LinkedList<String>}
	 * @return 载入文件行的列表
	 */
	public static List<String> fileReader(File file, Charset charset, List<String> list) {
		fileReader(file, charset, list::add);
		return list;
	}

	/**
	 * 读取文件，并对每行执行固定的操作
	 *
	 * @param filePath 文件路径
	 * @param action   操作
	 */
	public static void fileReaderInUTF8(String filePath, QRFileReaderLineAction action) {
		fileReader(new File(filePath), StandardCharsets.UTF_8, action);
	}


	/**
	 * 读取文件，并对每行执行固定的操作
	 *
	 * @param file   文件对象
	 * @param action 操作
	 */
	public static void fileReaderInUTF8(File file, QRFileReaderLineAction action) {
		fileReader(file, StandardCharsets.UTF_8, action);
	}

	/**
	 * 读取文件，并对每行执行固定的操作
	 *
	 * @param filePath 文件路径
	 * @param charset  编码
	 * @param action   操作
	 */
	public static void fileReader(String filePath, Charset charset, QRFileReaderLineAction action) {
		fileReader(new File(filePath), charset, action);
	}

	/**
	 * 读取文件，并对每行执行固定的操作
	 *
	 * @param file    文件对象
	 * @param charset 编码
	 * @param action  操作
	 */
	public static void fileReader(File file, Charset charset, QRFileReaderLineAction action) {
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, charset);
			BufferedReader in = new BufferedReader(isr);
			String lineText;
			while ((lineText = in.readLine()) != null) {
				action.lineText(lineText);
			}
			in.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
