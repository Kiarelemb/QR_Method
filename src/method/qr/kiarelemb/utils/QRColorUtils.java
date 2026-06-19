package method.qr.kiarelemb.utils;

import java.awt.*;
import java.util.LinkedList;

/**
 * @author Kiarelemb
 * @projectName QR_Method
 * @className QRColorUtils
 * @description TODO
 * @create 2026/6/7 09:01
 */
public class QRColorUtils {
	/**
	 * 将一个16进制的rgb值转成Color类
	 */
	public static Color parseColor(String hexLen) {
		final LinkedList<String> split = QRArrayUtils.splitWithLength(hexLen, 2);
		assert split.size() == 3;
		int[] rgbs = new int[3];
		int i = 0;
		for (String s : split) {
			rgbs[i++] = Integer.parseInt(s, 16);
		}
		return new Color(rgbs[0], rgbs[1], rgbs[2]);
	}

	public static int cvBoundCheck(int value) {
		return Math.max(Math.min(value, 255), 0);
	}

	public static Color parseColor(String rgb, char separator) {
		int[] values = QRArrayUtils.splitToInt(rgb, separator);
		return new Color(values[0], values[1], values[2]);
	}
}