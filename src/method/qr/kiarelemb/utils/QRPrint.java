package method.qr.kiarelemb.utils;

/**
 * @author Kiarelemb QR
 * @program: QR_Method
 * @description:
 * @create 2022-11-20 23:01
 **/
public class QRPrint {

	//region 控制台输出颜色封装
	/**
	 * 控制台输出白色字体
	 */
	public final static int FORE_COLOR_WHITE = 29;
	/**
	 * 控制台输出黑色字体
	 */
	public final static int FORE_COLOR_BLACK = 30;
	/**
	 * 控制台输出红色字体
	 */
	public final static int FORE_COLOR_RED = 31;
	/**
	 * 控制台输出绿色字体
	 */
	public final static int FORE_COLOR_GREEN = 32;
	/**
	 * 控制台输出黄色字体
	 */
	public final static int FORE_COLOR_YELLOW = 33;
	/**
	 * 控制台输出蓝色字体
	 */
	public final static int FORE_COLOR_BLUE = 34;
	/**
	 * 控制台输出紫色字体
	 */
	public final static int FORE_COLOR_PURPLE = 35;
	/**
	 * 控制台输出浅蓝色字体
	 */
	public final static int FORE_COLOR_LIGHT_BLUE = 36;
	/**
	 * 控制台输出灰色字体
	 */
	public final static int FORE_COLOR_GREY = 37;
	/**
	 * 控制台输出深灰色字体
	 */
	public final static int FORE_COLOR_DARK_GREY = 90;
	/**
	 * 控制台输出亮红色字体
	 */
	public final static int FORE_COLOR_BRIGHT_RED = 91;
	/**
	 * 控制台输出亮绿色字体
	 */
	public final static int FORE_COLOR_BRIGHT_GREEN = 92;
	/**
	 * 控制台输出亮黄色字体
	 */
	public final static int FORE_COLOR_BRIGHT_YELLOW = 93;
	/**
	 * 控制台输出亮蓝色字体
	 */
	public final static int FORE_COLOR_BRIGHT_BLUE = 94;
	/**
	 * 控制台输出亮紫色字体
	 */
	public final static int FORE_COLOR_BRIGHT_PURPLE = 95;
	/**
	 * 控制台输出浅蓝色字体
	 */
	public final static int FORE_COLOR_LIGHT_GREEN = 96;

	/**
	 * 除黑白色外的其他所有颜色集合
	 */
	public final static int[] FORE_COLORS = {FORE_COLOR_RED, FORE_COLOR_GREEN, FORE_COLOR_YELLOW, FORE_COLOR_BLUE, FORE_COLOR_PURPLE, FORE_COLOR_LIGHT_BLUE, FORE_COLOR_GREY, FORE_COLOR_DARK_GREY, FORE_COLOR_BRIGHT_RED, FORE_COLOR_BRIGHT_GREEN, FORE_COLOR_BRIGHT_YELLOW, FORE_COLOR_BRIGHT_BLUE, FORE_COLOR_BRIGHT_PURPLE, FORE_COLOR_LIGHT_GREEN};

	/**
	 * 控制台输出背景颜色为黑色的字体
	 */
	public final static int BACKGROUND_BLACK = 40;
	/**
	 * 控制台输出背景颜色为红色的字体
	 */
	public final static int BACKGROUND_RED = 41;
	/**
	 * 控制台输出背景颜色为绿色的字体
	 */
	public final static int BACKGROUND_GREEN = 42;
	/**
	 * 控制台输出背景颜色为黄色的字体
	 */
	public final static int BACKGROUND_YELLOW = 43;
	/**
	 * 控制台输出背景颜色为蓝色的字体
	 */
	public final static int BACKGROUND_BLUE = 44;
	/**
	 * 控制台输出背景颜色为紫色的字体
	 */
	public final static int BACKGROUND_PURPLE = 45;
	/**
	 * 控制台输出背景颜色为浅蓝色的字体
	 */
	public final static int BACKGROUND_LIGHT_BLUE = 46;

	/**
	 * 控制台输出无样式字体
	 */
	public final static int STYLE_NONE = 0;
	/**
	 * 控制台输出粗体字体
	 */
	public final static int STYLE_BOLD = 1;
	/**
	 * 控制台输出下划线字体
	 */
	public final static int STYLE_UNDERLINE = 4;
	/**
	 * 控制台输出反色字体，将背景色和前景色交换
	 */
	public final static int STYLE_INVERSE = 7;
	//endregion

	//region 开启颜色打印

	/**
	 * 只换行打印
	 */
	public static void println() {
		System.out.println();
	}

	/**
	 * 换行打印
	 *
	 * @param printText 要打印内容
	 */
	public static void println(String printText) {
		System.out.println(printText);
	}

	/**
	 * 换行打印带颜色字体
	 *
	 * @param printText    要打印内容
	 * @param font_frColor 字体前景色
	 */
	public static void println(String printText, int font_frColor) {
		System.out.println("\033[" + font_frColor + "m" + printText + "\033[0m");
	}

	/**
	 * 带背景颜色或字体样式换行打印带颜色字体
	 *
	 * @param printText             要打印的内容
	 * @param font_frColor          字体前景色
	 * @param font_bgColor_or_style 字体背景色或字体样式
	 */
	public static void println(String printText, int font_frColor, int font_bgColor_or_style) {
		System.out.println("\033[" + font_frColor + ";" + font_bgColor_or_style + "m" + printText + "\033[0m");
	}

	/**
	 * 带背景颜色和字体样式换行打印带颜色字体
	 *
	 * @param printText    要打印的内容
	 * @param font_frColor 字体前景色
	 * @param font_bgColor 字体背景色
	 * @param font_style   字体样式
	 */
	public static void println(String printText, int font_frColor, int font_bgColor, int font_style) {
		System.out.println("\033[" + font_frColor + ";" + font_bgColor + ";" + font_style + "m" + printText + "\033[0m");
	}

	/**
	 * 不换行打印
	 *
	 * @param printText 要打印内容
	 */
	public static void print(String printText) {
		System.out.print(printText);
	}

	/**
	 * 不换行打印带颜色字体
	 *
	 * @param printText    要打印内容
	 * @param font_frColor 字体前景色
	 */
	public static void print(String printText, int font_frColor) {
		System.out.print("\033[" + font_frColor + "m" + printText + "\033[0m");
	}

	/**
	 * 带背景颜色或字体样式不换行打印带颜色字体
	 *
	 * @param printText             要打印的内容
	 * @param font_frColor          字体前景色
	 * @param font_bgColor_or_style 字体背景色
	 */
	public static void print(String printText, int font_frColor, int font_bgColor_or_style) {
		System.out.print("\033[" + font_frColor + ";" + font_bgColor_or_style + "m" + printText + "\033[0m");
	}

	/**
	 * 带背景颜色或字体样式不换行打印带颜色字体
	 *
	 * @param printText             要打印的内容
	 * @param font_frColor          字体前景色
	 * @param font_bgColor_or_style 字体背景色
	 */
	public static void printlt(String printText, int font_frColor, int font_bgColor_or_style) {
		System.out.print("\033[" + font_frColor + ";" + font_bgColor_or_style + "m" + printText + "\033[0m");
		System.out.print("\t");
	}

	/**
	 * 带背景颜色和字体样式不换行打印带颜色字体
	 *
	 * @param printText    要打印的内容
	 * @param font_frColor 字体前景色
	 * @param font_bgColor 字体背景色
	 * @param font_style   字体样式
	 */
	public static void print(String printText, int font_frColor, int font_bgColor, int font_style) {
		System.out.print("\033[" + font_frColor + ";" + font_bgColor + ";" + font_style + "m" + printText + "\033[0m");
	}

	/**
	 * 随机前景色不换行打印带颜色字体
	 *
	 * @param printText 要打印的内容
	 */
	public static void randomForeColorPrint(String printText) {
		print(printText, FORE_COLORS[QRRandomUtils.getRandomInt(FORE_COLORS.length)]);
	}

	/**
	 * 随机前景色换行打印带颜色字体
	 *
	 * @param printText 要打印的内容
	 */
	public static void randomForeColorPrintln(String printText) {
		println(printText, FORE_COLORS[QRRandomUtils.getRandomInt(FORE_COLORS.length)]);
	}
	//endregion

	/**
	 * 为控制台清屏
	 */
	public static void clear() {
		System.out.println("\033[2J");
	}
}
