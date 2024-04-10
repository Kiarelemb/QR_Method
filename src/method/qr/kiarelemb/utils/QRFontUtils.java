package method.qr.kiarelemb.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.AttributedString;
import java.util.*;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:54
 * @apiNote 字体工具类
 */
public class QRFontUtils {
	public static final Map<String, Font> fontMap = new HashMap<>();
	public static String[] systemFontNames;
	private static Font font;

	public static Font getFontInSize(int size, String fontFilePath) {
		if (font == null) {
			try {
				InputStream is = new FileInputStream(fontFilePath);
				//返回一个指定字体类型和输入数据的font
				font = Font.createFont(Font.TRUETYPE_FONT, is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return getFont("微软雅黑", size);
	}

	public static Font loadFontFromFile(int size, String fontFilePath) {
		Font font = null;
		try {
			InputStream is = new FileInputStream(fontFilePath);
			//返回一个指定字体类型和输入数据的font
			font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, size);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return font;
	}

	public static Font loadFontFromFile(int size, File file) {
		Font font = null;
		try {
			InputStream is = new FileInputStream(file);
			//返回一个指定字体类型和输入数据的font
			font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, size);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return font;
	}

	public static Font loadFontFromFile(int size, URL url) {
		Font font = null;
		try {
			//返回一个指定字体类型和输入数据的font
			font = Font.createFont(Font.TRUETYPE_FONT, url.openStream()).deriveFont(Font.PLAIN, size);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return font;
	}

	public static Font loadFontFromURL(URL url) {
//		File fontFile = new File(url.getFile());
		return QRFontUtils.loadFontFromFile(32, url);
	}

	public static Font getFontInSize(int size) {
		return new Font("微软雅黑", Font.PLAIN, size);
	}

	public static Font getFont(String fontFamily, int size) {
		return new Font(fontFamily, Font.PLAIN, size);
	}

	/**
	 * 给定控件与文本内容，返回该控件为显示该文本而需要的宽度
	 *
	 * @param j    控件
	 * @param text 文本内容
	 * @return 控件需要设置的宽度
	 */
	public static int getTextInWidth(JComponent j, String text) {
		FontMetrics fm = j.getFontMetrics(j.getFont());
		char[] c = text.toCharArray();
		return getTextInWidth(j, j.getFont(), text);
	}

	/**
	 * 给定控件与文本内容，返回该控件为显示该文本而需要的宽度
	 *
	 * @param j    控件
	 * @param text 文本内容
	 * @return 控件需要设置的宽度
	 */
	public static int getTextInWidth(JComponent j, Font font, String text) {
		FontMetrics fm = j.getFontMetrics(font);
		char[] c = text.toCharArray();
		return fm.charsWidth(c, 0, c.length);
	}

	public static Rectangle2D getStringBounds(String str, Font font) {
		BufferedImage buff = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		return font.getStringBounds(str, buff.createGraphics().getFontRenderContext());
	}

	public static Rectangle2D getStringBounds(Graphics g, String str) {
		AttributedString text = new AttributedString(str);
		text.addAttribute(TextAttribute.FONT, g.getFont());
		final FontMetrics metrics = g.getFontMetrics();
		return metrics.getStringBounds(text.getIterator(), 0, text.getIterator().getEndIndex(), g);
	}

	public static Rectangle2D getStringBounds(String str, Graphics g, Font f) {
		return f.getStringBounds(str, g.getFontMetrics().getFontRenderContext());
	}

	public static Rectangle2D getStringBounds(AttributedString text, Graphics g, String str) {
		final FontMetrics metrics = g.getFontMetrics();
		return metrics.getStringBounds(text.getIterator(), 0, text.getIterator().getEndIndex(), g);
	}


	public static String[] getSystemFontNames() {
		if (systemFontNames == null) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			systemFontNames = ge.getAvailableFontFamilyNames();
		}
		return systemFontNames;
	}

	public static boolean containsTheFont(String fontName) {
		Set<String> names = new HashSet<>(Arrays.asList(getSystemFontNames()));
		return names.contains(fontName);
	}

	public static boolean fontCanAllDisplay(String str, String fontName) {
		final Font font = QRFontUtils.getFont(fontName, 10);
		return fontCanAllDisplay(str, font);
	}

	public static boolean fontCanAllDisplay(String str, Font font) {
		return font.canDisplayUpTo(str) == -1;
	}

	public static Font getCanDisplayFont(String str) {
		String[] fontNames = getSystemFontNames();
		for (String systemFontName : fontNames) {
			Font f = getFont(systemFontName, 10);
			if (fontCanAllDisplay(str, f)) {
				return f;
			}
		}
		return null;
	}
}
