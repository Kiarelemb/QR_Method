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
    public static final Map<String, Font> FONT_MAP = new HashMap<>();
    public static String[] systemFontNames;

    @Deprecated
    public static Font getFontInSize(int size, String fontFilePath) {
        return loadFontFromFile(size, fontFilePath);
    }

    /**
     * 从本地字体文件加载字体，并按字号返回可直接使用的 {@link Font}。
     * <p>
     * 字体会注册到本地 {@link GraphicsEnvironment}，并缓存到 {@link #FONT_MAP}。
     * 后续同名字体会直接从缓存派生指定字号，避免重复读取字体文件。
     *
     * @param size         字号
     * @param fontFilePath ttf 字体文件路径
     * @return 加载成功的字体；失败时返回 {@code null}
     */
    public static Font loadFontFromFile(int size, String fontFilePath) {
        return loadFontFromFile(size, new File(fontFilePath));
    }

    public static Font loadFontFromFile(int size, File file) {
        Font font = null;
        try {
            String fileName = QRFileUtils.getFileName(file);
            if (FONT_MAP.containsKey(fileName)) {
                return FONT_MAP.get(fileName).deriveFont((float) size);
            }
            InputStream is = new FileInputStream(file);
            //返回一个指定字体类型和输入数据的font
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            FONT_MAP.put(font.getFontName(), font);
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
            FONT_MAP.put(font.getFontName(), font);
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

    /**
     * 获取指定字体族和字号的字体。
     * <p>
     * 如果字体之前通过本类加载过，会从缓存字体派生指定字号；否则直接构造系统字体并缓存。
     * 调用方需要确认目标字体是否能显示业务文本，可配合 {@link #fontCanAllDisplay(String, Font)}。
     *
     * @param fontFamily 字体族名称
     * @param size       字号
     * @return 字体对象
     */
    public static Font getFont(String fontFamily, int size) {
        if (FONT_MAP.containsKey(fontFamily)) {
            return FONT_MAP.get(fontFamily).deriveFont((float) size);
        }
        Font font = new Font(fontFamily, Font.PLAIN, size);
        FONT_MAP.put(font.getFontName(), font);
        return font;
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
        return getStringBounds(text, g);
    }

    public static Rectangle2D getStringBounds(String str, Graphics g, Font f) {
        return f.getStringBounds(str, g.getFontMetrics().getFontRenderContext());
    }

    public static Rectangle2D getStringBounds(AttributedString text, Graphics g) {
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

    public static boolean isSystemFont(String fontName) {
        return Arrays.asList(getSystemFontNames()).contains(fontName);
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

    /**
     * 在系统字体列表中寻找第一个能完整显示指定字符串的字体。
     * <p>
     * 该方法会遍历系统全部字体，适合兜底选择字体，不适合在高频绘制路径中反复调用。
     *
     * @param str 待显示文本
     * @return 第一个可完整显示文本的字体；找不到时返回 {@code null}
     */
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
