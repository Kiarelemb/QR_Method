package method.qr.kiarelemb.utils;

import method.qr.kiarelemb.utils.data.QRFileLoadData;

import java.io.*;
import java.util.Objects;

/**
 * @author Kiarelemb
 * @projectName QR_Method
 * @className QRSerializeUtils
 * @description TODO
 * @create 2024/4/10 22:12
 */
public class QRSerializeUtils {
	/**
	 * 序列化
	 *
	 * @param binPath 反序化文件路径
	 * @param obj     要序列化的对象
	 * @throws IOException 可能抛出的异常
	 */
	public static void writeObject(String binPath, Object obj) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(binPath);
		ObjectOutputStream ops = new ObjectOutputStream(outputStream);
		ops.writeObject(obj);
		ops.flush();
		ops.close();
		outputStream.close();
	}

	/**
	 * 反序列化
	 *
	 * @param binPath 反序化文件路径
	 * @return 反序列化的对象
	 * @throws Exception 可能抛出的异常
	 */
	public static Object readObject(String binPath) throws Exception {
		FileInputStream inputStream = new FileInputStream(binPath);
		ObjectInputStream in = new ObjectInputStream(inputStream);
		Object obj = in.readObject();
		in.close();
		inputStream.close();
		return obj;
	}

	/**
	 * @param binPath      反序化文件路径
	 * @param srcPath      源文件路径
	 * @param binCheckCode 反序化文件校验码
	 * @param srcCheckCode 源文件校验码码
	 * @param data         校验失效操作
	 * @return 读取的对象
	 * @throws Exception 可能抛出的异常
	 */
	public static Object loadObject(String binPath, String srcPath, String binCheckCode, String srcCheckCode,
	                                QRFileLoadData data) throws Exception {
		if (Objects.equals(binCheckCode, srcCheckCode)) {
			return readObject(binPath);
		}
		return data.action(new File(srcPath));
	}

	/**
	 * 采用 {@link QRFileUtils#getCrc32(String)} 方法校验文件
	 *
	 * @param binPath      反序化文件路径
	 * @param srcPath      源文件路径
	 * @param binCheckCode 反序化文件校验码
	 * @param data         校验失效操作
	 * @return 读取的对象
	 * @throws Exception 可能抛出的异常
	 */
	public static Object loadObject(String binPath, String srcPath, String binCheckCode, QRFileLoadData data) throws Exception {
		String crc32 = QRFileUtils.getCrc32(srcPath);
		return loadObject(binPath, srcPath, binCheckCode, srcPath, data);
	}
}