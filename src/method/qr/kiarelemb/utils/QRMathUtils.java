package method.qr.kiarelemb.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class QRMathUtils {
	public static float floatFormat(float d, int scale) {
		if (Float.isInfinite(d) || Float.isNaN(d)) {
			throw new NumberFormatException("Infinite or NaN");
		}
		BigDecimal b = new BigDecimal(d);
		return b.setScale(scale, RoundingMode.HALF_UP).floatValue();
	}

	public static double doubleFormat(double d) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			throw new NumberFormatException("Infinite or NaN");
		}
		BigDecimal b = new BigDecimal(d);
		return b.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public static double doubleFormat(double d, int scale) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			throw new NumberFormatException("Infinite or NaN");
		}
		BigDecimal b = new BigDecimal(d);
		return b.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	public static double doubleFormat(double d, int scale, RoundingMode mode) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			throw new NumberFormatException("Infinite or NaN");
		}
		BigDecimal b = new BigDecimal(d);
		return b.setScale(scale, mode).doubleValue();
	}

	public static float floatCount(float f1, float f2) {
		BigDecimal b1 = new BigDecimal(Float.toString(f1));
		BigDecimal b2 = new BigDecimal(Float.toString(f2));
		return (float) doubleFormat(b1.subtract(b2).doubleValue());
	}

	public static String wordFormat(int wordNum) {
		if (wordNum > 10000) {
			return doubleFormat(wordNum / 10000d, 1, RoundingMode.DOWN) + "w";
		} else if (wordNum > 1000) {
			return doubleFormat(wordNum / 1000d, 1, RoundingMode.DOWN) + "k";
		}
		return Integer.toString(wordNum);
	}

	/**
	 * 成角如下所示，为角OPQ
	 * <pre>
	 *     P(x1, y1)
	 *     o
	 *     | \
	 *     |  \
	 *     |   \
	 *     O    o Q(x2, y2)
	 * <pre/>
	 * @return 计算两点间与实际y轴向下的角度
	 */
	public static double getAlpha(double x1, double y1, double x2, double y2) {
		return 90 + Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
	}

	/**
	 * @param d 要平方的值
	 * @return 传入值的平方
	 */
	public static double square(double d) {
		return Math.pow(d, 2);
	}

	public static void main(String[] args) {
		System.out.println(toString(100.00002f, 4, true));
	}

	/**
	 * @param f 要 {@code toString} 的小数
	 * @return 默认保留一位小数，且添加正号。如果小数点后有多的零，会被全部去掉
	 */
	public static String toString(float f) {
		return toString(f, 1, true);
	}

	/**
	 * @param f       要 {@code toString} 的小数
	 * @param showTag 是否添加正号
	 * @return 默认保留一位小数。如果小数点后有多的零，会被全部去掉
	 */
	public static String toString(float f, boolean showTag) {
		return toString(f, 1, showTag);
	}

	/**
	 * @param f             要 {@code toString} 的小数
	 * @param decimalPlaces 小数点后保留的位数
	 * @param showTag       是否添加正号
	 * @return 如果小数点后有多的零，会被全部去掉
	 */
	public static String toString(float f, int decimalPlaces, boolean showTag) {
		if (Float.isNaN(f)) {
			return "NaN";
		}

		StringBuilder buf = new StringBuilder();
		if (f >= 0) {
			if (showTag) {
				buf.append("+");
			}
		} else {
			if (showTag) {
				buf.append("-");
			}
			f = -f;
		}
		int value = (int) f;
		buf.append(value);
		if (decimalPlaces > 0) {
			buf.append(".");
			for (int i = 0; i < decimalPlaces; i++) {
				f = (f - value) * 10;
				value = (int) f;
				//如果是最后一位且为零，则不用添加了
				if (value == 0 && i == decimalPlaces - 1) {
					break;
				}
				buf.append(value);
			}
			//去掉末尾的0
			for (int ii = 0; ii < decimalPlaces - 1; ii++) {
				if (buf.charAt(buf.length() - 1) == '0') {
					buf.setLength(buf.length() - 1);
				}
			}
		}
		//去掉末尾的小数点
		if (buf.charAt(buf.length() - 1) == '.') {
			buf.setLength(buf.length() - 1);
			return buf.toString();
		}
		return buf.toString();
	}


	/**
	 * @param d 要 {@code toString} 的小数
	 * @return 默认保留一位小数，且添加正号。如果小数点后有多的零，会被全部去掉
	 */
	public static String toString(double d) {
		return toString(d, 1, true);
	}

	/**
	 * @param d       要 {@code toString} 的小数
	 * @param showTag 是否添加正号
	 * @return 默认保留一位小数。如果小数点后有多的零，会被全部去掉
	 */
	public static String toString(double d, boolean showTag) {
		return toString(d, 1, showTag);
	}

	/**
	 * @param d             要 {@code toString} 的小数
	 * @param decimalPlaces 小数点后保留的位数
	 * @param showTag       是否添加正号
	 * @return 如果小数点后有多的零，会被全部去掉
	 */
	public static String toString(double d, int decimalPlaces, boolean showTag) {
		if (Double.isNaN(d)) {
			return "NaN";
		}
		StringBuilder buf = new StringBuilder();
		if (d >= 0) {
			if (showTag) {
				buf.append("+");
			}
		} else {
			if (showTag) {
				buf.append("-");
			}
			d = -d;
		}
		int value = (int) d;
		buf.append(value);
		if (decimalPlaces > 0) {
			buf.append(".");
			for (int i = 0; i < decimalPlaces; i++) {
				d = (d - value) * 10;
				value = (int) d;
				//如果是最后一位且为零，则不用添加了
				if (value == 0 && i == decimalPlaces - 1) {
					break;
				}
				buf.append(value);
			}
			//去掉末尾的0
			for (int ii = 0; ii < decimalPlaces - 1; ii++) {
				if (buf.charAt(buf.length() - 1) == '0') {
					buf.setLength(buf.length() - 1);
				}
			}
		}
		//去掉末尾的小数点
		if (buf.charAt(buf.length() - 1) == '.') {
			buf.setLength(buf.length() - 1);
			return buf.toString();
		}
		return buf.toString();
	}

	/**
	 * @param number 要比较的数字
	 * @param min    大于这个最小值
	 * @param max    小于这个最大值
	 * @return 整数 {@code number} 是否在开区间 ({@code min},{@code max})间
	 */
	public static boolean boundCheck(int number, int min, int max) {
		return number > min && number < max;
	}

	/**
	 * 小数部分加1
	 */
	public static int doubleToInt(double d) {
		if (d == 0) {
			return 0;
		}
		int i = (int) d;
		if (d - i > 0) {
			return i + 1;
		}
		return i;
	}

	/**
	 * 小数部分加1
	 */
	public static int doubleToInt(float f) {
		if (f == 0) {
			return 0;
		}
		int i = (int) f;
		if (f - i > 0) {
			return i + 1;
		}
		return i;
	}

	/**
	 * 解一元二次方程组
	 * @param a 一般式中的A
	 * @param b 一般式中的B
	 * @param c 一般式中的C
	 * @return 列表为空则无解，只有一个则为一解，有俩则是俩解，大的在前，小的在后
	 */
	public static List<Double> quadraticEquationOfOneVariable(double a, double b, double c) {
		double delta;
		double x1;
		double x2;
		delta = b * b - 4 * a * c;
		ArrayList<Double> result = new ArrayList<>();
		if (delta >= 0) {
			if (delta > 0) {
				x1 = (-b + Math.sqrt(delta)) / (2 * a);
				x2 = (-b - Math.sqrt(delta)) / (2 * a);
				result.add(x1);
				result.add(x2);
			} else {
				x1 = x2 = (-b / (2 * a));
				result.add(x1);
			}
		}
		return result;
	}
}
