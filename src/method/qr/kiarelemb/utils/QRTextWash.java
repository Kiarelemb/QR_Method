package method.qr.kiarelemb.utils;

import method.qr.kiarelemb.utils.data.QRStringRandomData;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Kiarelemb QR
 * @date 2021/9/22 下午9:57
 * @apiNote
 */
public class QRTextWash {

    /**
     * 中英文都要处理的
     */
    public static String lightWashBasic(String text) {
        //全角转半角
        text = QRStringUtils.fullWidthToHalf(text);
        String[] enSymbol = {"@", "/", "「", "」", "〔", "〕", "&", "\\^", "　", "\t", "<", ">", "\\[", "]", "\\*", "\\+", "\\^", "\\|"};
        for (String s : enSymbol) {
            text = text.replaceAll(s, "");
        }
        text = text.replaceAll("\r", "");
        text = text.replaceAll(QRStringUtils.AN_ENTER, "");
        return text;
    }

    public static String lightWashForEnglish(String text) {
        text = lightWashBasic(text);
        String[] cnSymbol = {"“", "”", "，", "；", "……", "！", "：", "。", "？", "（", "）"};
        String[] enSymbol = {"\"", "\"", ",", ";", "...", "!", ":", ".", "?", "(", ")", ""};
        for (int i = 0; i < cnSymbol.length; i++) {
            text = text.replaceAll(cnSymbol[i], enSymbol[i]);
        }
        String[] enSymbol2 = {"\\)", "\\(", "\\?", "\\.", "\"", "'", ",", ";", "\\.\\.\\.", "!", ":", "\\\\"};
        for (String s : enSymbol2) {
            text = text.replaceAll(" +" + s, s);
        }
        return QRStringUtils.notEnglishCharClear(text).replaceAll(" +", " ");
    }

    public static String lightWashForChinese(String text) {
        text = text.replaceAll("[a-zA-Z0-9]*?\\.+?[a-zA-Z0-9]*", "");
        text = text.replaceAll("[a-zA-Z]*?'[a-zA-Z]*?", "");
        text = lightWashBasic(text);
        String[] enSymbol = {"\\)", "\\(", "\\?", "\\.", "\"", "'", ",", ";", "\\.\\.\\.", "!", ":", "\\{", "}", "-", "\\\\", QRStringUtils.A_WHITE_SPACE};
        String[] cnSymbol = {"）", "（", "？", "。", "", "", "，", "；", "……", "！", "：", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        for (int i = 0; i < enSymbol.length; i++) {
            text = text.replaceAll(enSymbol[i], cnSymbol[i]);
        }
        text = QRStringUtils.notSupportedCharClear(text);
        text = text.replaceAll("（）", "");
        text = text.replaceAll("：“”", "");
        text = text.replaceAll("“”", "");
        text = text.replaceAll("·+", "……");
        text = text.replaceAll("。{2,}", "……");
        text = text.replaceAll("…", "……");
        text = text.replaceAll("…{3,}", "……");
        text = text.replaceAll("“[。，：！？、]”", "");
        return text;
    }

    /**
     * 检查文件中是否有不支持的内容，如果有，返回true
     *
     * @param as 返回的不支持内容，或文本文件的最后的内容储存在as中，as.get(0)可以取到。
     *           StringRandomData中，c值为内容，num为内容的开始位置
     */
    public static boolean articlesAbnormalCharFind(String filePath, ArrayList<QRStringRandomData> as) {
        try {
            RandomAccessFile ra = new RandomAccessFile(filePath, "r");
            int startIndex = 0;
            int length = (int) (ra.length() / 3);
            int endIndex = length;
            int mid = endIndex / 2;
            final int len = 35;
            String str = "";
            while (endIndex - startIndex > len) {
                str = QRFileUtils.fileReaderByRandomAccessWithUTF8(ra, mid, len);
                if (QRStringUtils.isChineseArticle(str)) {
                    startIndex = mid;
                } else {
                    endIndex = mid;
                }
                mid = (endIndex - startIndex) / 2 + startIndex;
            }
            as.add(new QRStringRandomData(str, mid));
            ra.close();
            if (length <= mid + len) {
                return false;
            }
        } catch (Exception e) {
            QRTools.doNothing(e);
        }
        return true;
    }
}