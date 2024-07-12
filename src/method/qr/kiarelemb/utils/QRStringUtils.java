package method.qr.kiarelemb.utils;

import method.qr.kiarelemb.utils.data.QRStringSearchResult;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:15
 * @apiNote 字符串工具类
 */
public class QRStringUtils {
    public static final String[] ARR_EMPTY = new String[0];
    public static final int[] CHINESE_NORMAL = new int[]{11904, 12245, 13312, 40955};
    public static final int[] CHINESE_EXTRA = {55300, 57400};
    public static final int[] ENGLISH_RANGE = {65, 122};
    public static final int[] NUMBER_RANGE = {48, 57};
    public static final String AN_ENTER = "\n";
    public static final char AN_ENTER_CHAR = '\n';
    public static final String A_WHITE_SPACE = " ";
    public static final char A_WHITE_SPACE_CHAR = ' ';
    public static final char A_TAB_CHAR = '\t';
    public static final String SUPPORTED_CHINESE_MARK = "、，。：！？“”；%《～》…〖〗（）「」〈〉·‘’—";
    public static final String SUPPORTED_ENGLISH_MARK = " ,.\"'-;!:?&^%#@*()[]{}/1234567890";
    public static final Set<Character> ENGLISH_DIVIDE_MARK = new HashSet<>() {{
        char[] chars = " ,.\"';!:?&^%#@\r\n*‘“([{/%".toCharArray();
        for (char c : chars) {
            add(c);
        }
    }};

    public static String getMd5(String string) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bs = md5.digest(string.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            final int i = x & 0xff;
            if (i >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(i));
            } else {
                sb.append(Integer.toHexString(i));
            }
        }
        return sb.toString();
    }

    public static String md5ToNum(String str) {
        if (str.isEmpty()) {
            return null;
        }
        char[] ch = str.toCharArray();
        StringBuilder key = new StringBuilder();
        for (char c : ch) {
            switch (c) {
                case 'a':
                    key.append("17");
                    break;
                case 'b':
                    key.append("38");
                    break;
                case 'c':
                    key.append("29");
                    break;
                case 'd':
                    key.append("47");
                    break;
                case 'e':
                    key.append("66");
                    break;
                case 'f':
                    key.append("95");
                    break;
                default:
                    key.append(c);
                    break;
            }
        }
        return key.toString();
    }

    /**
     * 判断一个字符串是不是true
     *
     * @param str 要判断的字符串
     * @return 字符串不为true，则返回false
     */
    public static boolean stringToBoolean(String str) {
        return "true".equalsIgnoreCase(str);
    }

    /**
     * 字符串转数字
     *
     * @param str 要转的字符串
     * @return 数字
     */
    public static int stringToInt(String str) {
        return Integer.parseInt(str);
    }

    /**
     * 高效率的空格去除算法，经测试，速度吊打String.replaceAll()方法
     */
    public static String spaceClear(String text, boolean checkContains) {
        if (checkContains && !text.contains(QRStringUtils.A_WHITE_SPACE)) {
            return text;
        }
        char[] c = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (char ch : c) {
            if (ch == QRStringUtils.A_WHITE_SPACE_CHAR || ch == QRStringUtils.A_TAB_CHAR || ch == '　') {
                continue;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 将两个及两个以上的空格替换为一个空格
     *
     * @param text 文本
     * @return 替换结果
     */
    public static String spaceFormat(String text) {
        return text.replaceAll(" {2,}", QRStringUtils.A_WHITE_SPACE).trim();
    }

    /**
     * 高效率的换行符去除算法，经测试，速度吊打String.replaceAll()方法
     */
    public static String lineSeparatorClear(String text, boolean checkContains) {
        if (checkContains && !text.contains(AN_ENTER)) {
            return text;
        }
        char[] c = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : c) {
            if (ch == QRStringUtils.AN_ENTER_CHAR) {
                continue;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 去除空格和换行符
     */
    public static String spaceAndLineSeparatorClear(String text) {
        boolean spaceContain = text.contains(QRStringUtils.A_WHITE_SPACE);
        boolean lineSeparatorContain = text.contains(AN_ENTER);
        if (!spaceContain && !lineSeparatorContain) {
            return text;
        } else if (!spaceContain) {
            return lineSeparatorClear(text, false);
        } else if (!lineSeparatorContain) {
            return spaceClear(text, false);
        }
        char[] c = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : c) {
            if (ch == QRStringUtils.A_WHITE_SPACE_CHAR || ch == QRStringUtils.AN_ENTER_CHAR) {
                continue;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 计算字符串内指定字符的字数
     */
    public static int englishWordsCount(String text) {
        return markCount(spaceFormat(text), QRStringUtils.A_WHITE_SPACE_CHAR) + 1;
    }

    /**
     * 将英语句子拆分为英语单词
     *
     * @param englishText 英语句子
     * @return 拆分后的英语单词数组
     */
    public static String[] englishSplit(String englishText) {
        return englishText.split(" ");
    }

    /**
     * 将英语句子拆分为英语单词，标点也会拆开
     *
     * @param englishText 英语句子
     * @return 拆分后的英语单词数组
     */
    public static String[] englishSplitPro(String englishText) {
        LinkedList<String> list = new LinkedList<>();
        char[] chars = englishText.toCharArray();
        StringBuilder sb = new StringBuilder();
        String mark = " ,.\"';!:“”-?&\r\n^#@*()[]{}/";
        for (int i = 0, len = chars.length; i < len; i++) {
            char c = chars[i];
            if (mark.indexOf(c) != -1) {
                if (c != '\'' || (i != 0 && (chars[i - 1] == ',' || chars[i - 1] == '.'))) {
                    if (i != len - 1 && c == '-' && chars[i + 1] == '-') {
                        list.add("--");
                        sb = new StringBuilder();
                        i++;
                    } else {
                        if (sb.length() != 0) {
                            list.add(sb.toString());
                            sb = new StringBuilder();
                        }
                        if (c != ' ') {
                            //拆
                            list.add(String.valueOf(c));
                        }
                    }
                    continue;
                }
            }
            sb.append(c);
        }
        if (sb.length() != 0) {
            list.add(sb.toString());
        }
        return QRArrayUtils.stringListToArr(list);
    }

    /**
     * 英文分词去标点
     *
     * @param text 英文文本
     * @return 分词
     */
    public static String[] englishParticiple(String text) {
        LinkedList<String> list = new LinkedList<>();
        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        String mark = " ,.\"';!:“”-?&^#@*\n()[]{}/";
        for (int i = 0, len = chars.length; i < len; i++) {
            char c = chars[i];
            if (mark.indexOf(c) != -1) {
                if (c != '\'' || (i != 0 && (chars[i - 1] == ',' || chars[i - 1] == '.'))) {
                    if (i != len - 1 && c == '-' && chars[i + 1] == '-') {
                        sb = new StringBuilder();
                        i++;
                    } else {
                        if (sb.length() != 0) {
                            list.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                    continue;
                }
            }
            sb.append(c);
        }
        if (sb.length() != 0) {
            list.add(sb.toString());
        }
        return QRArrayUtils.stringListToArr(list);
    }

    public static int markCount(String text, char mark) {
        char[] c = text.toCharArray();
        int count = 0;
        for (char value : c) {
            if (value == mark) {
                count++;
            }
        }
        return count;
    }

    /**
     * 这个算法来自StringUtils，略改，非个人编写
     */
    public static String join(final String[] array, String separator) {
        final int startIndex = 0;
        final int endIndex = array.length;
        final StringBuilder buf = new StringBuilder(getStringArrLen(array));
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(final Set<String> array, String separator) {
        final StringBuilder buf = new StringBuilder(getStringArrLen(array));
        for (String s : array) {
            if (s != null) {
                buf.append(separator).append(s);
            }
        }
        return buf.substring(separator.length());
    }

    public static String join(final List<String> array, String separator) {
        final StringBuilder buf = new StringBuilder(getStringArrLen(array));
        for (String s : array) {
            if (s != null) {
                buf.append(separator).append(s);
            }
        }
        return buf.substring(separator.length());
    }

    /**
     * 这个算法来自StringUtils，略改，非个人编写
     */
    private static String replaceOnce(final String text, String searchString, final String replacement) {
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = Math.max(increase, 0);
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        buf.append(text, start, end).append(replacement);
        start = end + replLength;
        buf.append(text, start, text.length());
        return buf.toString();
    }

    /**
     * 取得拓展字
     */
    public static String[] getChineseExtraPhrase(String text) {
        if (text.length() == 1) {
            return new String[]{text};
        }
        ArrayList<String> als = new ArrayList<>();
        StringBuilder extra = new StringBuilder();
        char[] c = text.toCharArray();
        for (char value : c) {
            final String s = String.valueOf(value);
            if (SUPPORTED_CHINESE_MARK.contains(s) || isEnglish(value)) {
                als.add(s);
                continue;
            }
            if (isCharInRange(value, CHINESE_NORMAL)) {
                als.add(s);
            } else if (isCharInRange(value, CHINESE_EXTRA)) {
                extra.append(value);
                if (extra.length() == 2) {
                    als.add(extra.toString());
                    extra = new StringBuilder();
                }
            } else {
                als.add(s);
            }
            //常用字添加
//            if ((value < CHINESE_NORMAL[0] || value > CHINESE_NORMAL[3]) || (value > CHINESE_NORMAL[1] && value < CHINESE_NORMAL[2])) {
//                extra.append(value);
//                if (extra.length() == 2) {
//                    als.add(extra.toString());
//                    extra = new StringBuilder();
//                }
//            } else {
//
//            }
        }
        return als.toArray(ARR_EMPTY);
    }

    /**
     * @param c 符号
     * @return 是否是中英文标点符号
     */
    public static boolean isCNOrENMark(char c) {
        return SUPPORTED_CHINESE_MARK.contains(String.valueOf(c)) || isEnglish(c);
    }

    /**
     * 将长流词提文件中的每行内容分割
     */
    public static String[] tipSplit(String readLine) {
        return stringSplit(readLine, QRStringUtils.A_TAB_CHAR, false);
    }

    /**
     * 字符串搜索
     *
     * @param parentStr 被搜索的父字符串
     * @param childStr  需要搜索的子字符串
     * @return 返回个数
     */
    public static int stringContains(String parentStr, String childStr) {
        String[] split = QRStringUtils.splitToCharStr(childStr);
        int num = 0;
        for (String s : split) {
            if (parentStr.contains(s)) {
                num++;
            }
        }
        return num;
    }

    /**
     * @param parentStr 被搜索的父字符串
     * @param childStrs 需搜索的子字符串
     * @return 父字符串是否包含子字符串中的 <b>所有<b/> 字符串
     */
    public static boolean stringContainsAll(String parentStr, String[] childStrs) {
        return Arrays.stream(childStrs).allMatch(parentStr::contains);
    }

    /**
     * @param parentStr 被搜索的父字符串
     * @param childStrs 需搜索的子字符串
     * @return 父字符串是否包含子字符串中的 <b>任意<b/> 字符串
     */
    public static boolean stringContainsAny(String parentStr, String[] childStrs) {
        return Arrays.stream(childStrs).anyMatch(parentStr::contains);
    }

    /**
     * @param parentStr 被搜索的父字符串
     * @param childStrs 需搜索的子字符串
     * @return 父字符串是否 <b>不<b/> 含子字符串中的任意字符串
     */
    public static boolean stringContainsNon(String parentStr, String[] childStrs) {
        return Arrays.stream(childStrs).noneMatch(parentStr::contains);
    }

    /**
     * @param parentStr 被搜索的父字符串
     * @param childStrs 需搜索的子字符串
     * @return 父字符串是否包含子字符串中的任意字符串中的字符
     */
    public static boolean stringContainsVag(String parentStr, String[] childStrs) {
        return Arrays.stream(childStrs).anyMatch(childStr -> QRStringUtils.stringContains(parentStr, childStr) > 0);
    }

    /**
     * @param parentStr 被搜索的父字符串
     * @param childStrs 需搜索的子字符串
     * @return 父字符串是否包含子字符串中的任意字符串的开头内容
     */
    public static boolean stringStartsWithAny(String parentStr, String[] childStrs) {
        return Arrays.stream(childStrs).anyMatch(parentStr::startsWith);
    }


    /**
     * @param parentStr 被搜索的父字符串
     * @param childStrs 需搜索的子字符串
     * @return 父字符串是否包含子字符串中的任意字符串的结尾内容
     */
    public static boolean stringEndsWithAny(String parentStr, String[] childStrs) {
        return Arrays.stream(childStrs).anyMatch(parentStr::endsWith);
    }

    public static boolean stringContains(String parentStr, String childStr, float count) {
        String[] split = QRStringUtils.splitToCharStr(childStr);
        float num = Arrays.stream(split).filter(parentStr::contains).count();
        return num / split.length >= count;
    }

    public static String[] stringSplit(String lineText, char splitChar) {
        return stringSplit(lineText, splitChar, false);
    }

    public static String[] stringSplit(String lineText, char splitChar, boolean trimStr) {
        StringBuilder sb = new StringBuilder();
        String[] s = new String[2];
        char[] c = lineText.trim().toCharArray();
        for (char ch : c) {
            if (ch == splitChar && s[0] == null) {
                if (trimStr) {
                    s[0] = sb.toString().trim();
                } else {
                    s[0] = sb.toString();
                }
                sb = new StringBuilder();

                continue;
            }
            sb.append(ch);
        }
        if (trimStr) {
            s[1] = sb.toString().trim();
        } else {
            s[1] = sb.toString();
        }
        return s;
    }

    public static String getMessageMD5(String mac, String message) {
        StringBuilder sbMsg = new StringBuilder(message);
        StringBuilder sbMac = new StringBuilder(mac);
        try {
            return getMd5(sbMsg.reverse().append(sbMac.reverse()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 判断中文非拓展字
     */
    public static boolean isChineseNormal(char value) {
        return (value >= CHINESE_NORMAL[0] && value <= CHINESE_NORMAL[3]) && (value <= CHINESE_NORMAL[1] || value >= CHINESE_NORMAL[2]);
    }

    /**
     * 判断中文非拓展字
     */
    public static boolean isWholeSingleChineseNormal(String s) {
        char[] c = s.toCharArray();
        for (char value : c) {
            if (!isChineseNormal(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断中文拓展字
     */
    public static boolean isChineseExtra(char value) {
        return value >= CHINESE_EXTRA[0] && value <= CHINESE_EXTRA[1];
    }

    /**
     * 判断中文拓展字
     */
    public static boolean isWholeSingleChineseExtra(String s) {
        char[] c = s.toCharArray();
        for (char value : c) {
            if (!isChineseExtra(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否包含中文
     */
    public static boolean containsChineseNormal(String s) {
        char[] c = s.toCharArray();
        for (char value : c) {
            if ((value >= CHINESE_NORMAL[0] && value <= CHINESE_NORMAL[3]) && (value <= CHINESE_NORMAL[1] || value >= CHINESE_NORMAL[2])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含拓展字
     */
    public static boolean containsChineseExtra(String s) {
        char[] c = s.toCharArray();
        for (char value : c) {
            if (value >= CHINESE_NORMAL[0] && value <= CHINESE_NORMAL[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断中文单字
     */
    public static boolean isWholeSingleChinese(String s) {
        StringBuilder sb = isCharInRange(s, CHINESE_NORMAL);
        if (sb.length() != 0) {
            return true;
        }
        //再识别拓展字
        return isWholeSingleChineseExtra(sb.toString());
    }

    /**
     * @return 取得所有的中文常用单字
     */
    public static String getMarkCutChineseNormal(String s) {
        final char[] c = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char value : c) {
            final boolean isInRange = (value >= CHINESE_NORMAL[0] && value <= CHINESE_NORMAL[3]) && (value <= CHINESE_NORMAL[1] || value >= CHINESE_NORMAL[2]);
            if (isInRange) {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    /**
     * 判断该字符是不是英文字母
     */
    public static boolean isAlphabet(char c) {
        return isCharInRange(c, ENGLISH_RANGE);
    }

    /**
     * 判断该字符串是不是全是英文字母
     */
    public static boolean isAlphabets(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (!isAlphabet(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断该字符串是否包含字母
     */
    public static boolean containsAlphabet(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (isAlphabet(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断该字符是不是英文
     */
    public static boolean isEnglish(char c) {
        return c > 35 && c < 123;
    }

    /**
     * 判断发的文是不是英文
     */
    public static boolean isEnglish(String text) {
        int englishNum = 0;
        final int i = 1000;
        //如果太长了，取一段
        if (text.length() > i) {
            text = text.substring(100, 500);
        }
        final String lineSeparatorClear = spaceAndLineSeparatorClear(text);
        char[] ch = lineSeparatorClear.toCharArray();
        for (char c : ch) {
            if (isEnglish(c)) {
                englishNum++;
            }
        }
        //只要考虑英文占比就可以了
        return ((englishNum + 0.0) / ch.length > 0.7);
    }

    /**
     * 判断纯英文，内部自动去掉空格
     */
    public static boolean isWholeEnglish(String s) {
        return isCharInRange(spaceAndLineSeparatorClear(s), ENGLISH_RANGE).toString().isEmpty();
    }

    /**
     * 判断是不是中文的词组
     */
    public static boolean isChinesePhrase(String s) {
        StringBuilder sb = isCharInRange(s.trim(), CHINESE_NORMAL);
        return sb.toString().trim().isEmpty();
    }

    /**
     * 判断英文短语
     */
    public static boolean isEnglishPhrase(String s) {
        char[] c = s.toCharArray();
        ArrayList<Character> ach = new ArrayList<>();
        ach.add('\'');
        ach.add('-');
        ach.add('/');
        for (char value : c) {
            if (value < ENGLISH_RANGE[0] || value > ENGLISH_RANGE[1] || !ach.contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否在指定的范围内
     * 会取出不是范围内的字
     */
    public static StringBuilder isCharInRange(String s, int[] range) {
        char[] c = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        if (range.length == 2) {
            for (char value : c) {
                if (value < range[0] || value > range[1]) {
                    sb.append(value);
                }
            }
        } else if (range.length == 4) {
            for (char value : c) {
                final boolean b = (value < range[0] || value > range[3]) || (value > range[1] && value < range[2]);
                if (b) {
                    sb.append(value);
                }
            }
        }
        return sb;
    }

    public static boolean isCharInRange(char value, int[] range) {
        if (range.length == 2) {
            return value >= range[0] && value <= range[1];
        } else if (range.length == 4) {
            return (value >= range[0] && value <= range[3]) && (value < range[1] || value > range[2]);
        }
        return false;
    }

    /**
     * 取出字串中所有空格的位置
     *
     * @param s 要找的字串
     * @return 空格位置集
     */
    public static ArrayList<Integer> getAllSpaceIndexes(String s) {
        return appearIndexes(s, QRStringUtils.A_WHITE_SPACE_CHAR);
    }

    /**
     * 找到所有子字符串在父字符串中出现的位置
     *
     * @param str       父字符串
     * @param searchStr 子字符串
     * @return 位置列表
     */
    public static ArrayList<Integer> appearIndexes(String str, String searchStr) {
        int childLen = searchStr.length();
        if (childLen == 1) {
            return appearIndexes(str, searchStr.charAt(0));
        }
        ArrayList<Integer> list = new ArrayList<>();
        char[] chars = str.toCharArray();
        char[] child = searchStr.toCharArray();
        int index = 0;
        int maxIndex = chars.length - childLen;
        for (int i = 0; i <= maxIndex; ) {
            if (chars[i] == child[index]) {
                index++;
                boolean equal = true;
                for (int j = 1; j < childLen; j++, index++) {
                    if (chars[i + j] != child[index]) {
                        equal = false;
                        break;
                    }
                }
                index = 0;
                if (equal) {
                    list.add(i);
                    i += childLen;
                    continue;
                }
            }
            i++;
        }
        return list;
    }

    /**
     * 找到所有字符出现的位置
     *
     * @param str 父字符串
     * @param ch  字符
     * @return 位置列表
     */
    public static ArrayList<Integer> appearIndexes(String str, char ch) {
        ArrayList<Integer> ali = new ArrayList<>();
        if (!str.contains(QRStringUtils.A_WHITE_SPACE)) {
            return ali;
        }
        char[] c = str.toCharArray();
        for (int i = 0, cLength = c.length; i < cLength; i++) {
            if (c[i] == ch) {
                ali.add(i);
            }
        }
        return ali;
    }

    /**
     * 从字符串 {@code code} 中查找 {@code findChars} 的每个字符，返回找到的最大的那个位置
     *
     * @param code      父字符串
     * @param findChars 子串字符串
     * @return 最大出现位置
     */
    public static int getLatestIndex(String code, String findChars) {
        int index = -1;
        char[] chars = findChars.toCharArray();
        for (char c : chars) {
            index = Math.max(index, code.lastIndexOf(c));
        }
        return index;
    }

    /**
     * 从字符串 {@code parent} 中查找 {@code findChars} 的每个字符，返回找到的最小的那个位置
     *
     * @param parent    父字符串
     * @param findChars 子串字符串
     * @return 最小出现位置
     */
    public static int getForemostIndex(String parent, String findChars) {
        int index = parent.length();
        boolean flag = true;
        char[] chars = findChars.toCharArray();
        for (char c : chars) {
            int temp = parent.indexOf(c);
            if (temp != -1) {
                index = Math.min(index, temp);
                flag = false;
            }
        }
        if (flag) {
            return -1;
        }
        return index;
    }

    /**
     * 从字符串 {@code parent} 中查找 {@code findStr} 的每个字符串，返回找到的最小的那个位置
     *
     * @param parent     父字符串
     * @param startIndex 开始查找的位置
     * @param findStr    子串字符串
     * @return 最小出现位置
     */
    public static QRStringSearchResult getForemostIndex(String parent, int startIndex, String... findStr) {
        int index = parent.length();
        boolean flag = true;
        String find = "";
        for (String s : findStr) {
            int i = parent.indexOf(s, startIndex);
            if (i != -1 && i < index) {
                index = i;
                find = s;
                flag = false;
            }
        }
        if (flag) {
            return null;
        }
        return new QRStringSearchResult(index, find);
    }


    /**
     * 将一个字符串以一个字符为单位，转为字符串数组
     */
    public static String[] stringToStringArr(String text) {
        char[] c = text.toCharArray();
        String[] returnArr = new String[c.length];
        for (int i = 0, cLength = c.length; i < cLength; i++) {
            char ch = c[i];
            returnArr[i] = String.valueOf(ch);
        }
        return returnArr;
    }

    public static String getSha1(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 该算法改自commons
     *
     * @param str       被搜索的字符串
     * @param searchStr 搜索的字符
     * @param findTimes 第几次出现的次数
     * @return 第几次出现的位置
     */
    public static int getIndexWhenFindTimes(String str, String searchStr, int findTimes) {
        if (str == null || searchStr == null || findTimes <= 0 || searchStr.isEmpty()) {
            return -1;
        }
        int found = 0;
        int searchLen = searchStr.length();
        int index = -searchLen;
        do {
            index = str.indexOf(searchStr, index + searchLen);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < findTimes);
        return index;
    }


    /**
     * 在一个字符串中找一个字符，看是否出现了指定的次数
     *
     * @param str       被搜索的字符串
     * @param searchStr 搜索的字符
     * @param findTimes 预计要出现的次数
     * @return 若未达到预期，则返回 {@code false}
     */
    public static boolean findAtLeast(String str, String searchStr, int findTimes) {
        if (str == null || searchStr == null || findTimes < 0 || searchStr.isEmpty()) {
            return false;
        }
        int found = 0;
        int searchLen = searchStr.length();
        int index = -searchLen;
        do {
            index = str.indexOf(searchStr, index + searchLen);
            if (index < 0) {
                return findTimes == 0;
            }
            found++;
        } while (found < findTimes);
        return true;
    }

    /**
     * 在一个字符串中找一个字符，看是否只出现了指定的次数
     *
     * @param str       被搜索的字符串
     * @param searchStr 搜索的字符
     * @param findTimes 预计要出现的次数
     * @return 若未达到预期，则返回 {@code false}
     */
    public static boolean findAtTimes(String str, String searchStr, int findTimes) {
        if (str == null || searchStr == null || findTimes < 0 || searchStr.isEmpty()) {
            return false;
        }
        int found = 0;
        int searchLen = searchStr.length();
        int index = -searchLen;
        while (true) {
            index = str.indexOf(searchStr, index + searchLen);
            if (index < 0) {
                return found == findTimes;
            }
            found++;
            if (found > findTimes) {
                return false;
            }
        }
    }

    /**
     * 判断一个字符串中是否只出现了一次指定的字符
     *
     * @param str       被搜索的字符串
     * @param searchStr 搜索的字符
     * @return 若未达到预期，则返回 {@code false}
     */
    public static boolean findOnlyOnce(String str, String searchStr) {
        return findAtTimes(str, searchStr, 1);
    }

    /**
     * 找出子字符串在父字符串中出现的次数
     *
     * @param str
     * @param searchStr
     * @return
     */
    public static int findTimes(String str, String searchStr) {
        if (str == null || searchStr == null || searchStr.isEmpty()) {
            return -1;
        }
        if (!str.contains(searchStr)) {
            return -1;
        }
        int found = 0;
        int searchLen = searchStr.length();
        int index = -searchLen;
        while (true) {
            index = str.indexOf(searchStr, index + searchLen);
            if (index < 0) {
                break;
            }
            found++;
        }
        return found;
    }

    /**
     * 将成绩单的加密字符，取6位
     */
    public static String getSha1Short(String grade, String s) {
        //对内容加盐
        String code = getSha1(s + grade);
        return code.substring(0, 6);
    }

    /**
     * @param grade 成绩单
     */
    public static boolean gradeCheck(String grade, String s) {
        try {
            String mark = "成绩单:";
            int startIndex = grade.lastIndexOf(mark);
            String substring = grade.substring(0, startIndex).trim();
            String code = getSha1(s + substring);
            String shortCode = grade.substring(startIndex + mark.length()).trim();
            if (shortCode.length() == 6) {
                return code.substring(0, 6).equals(shortCode);
            }
        } catch (Exception e) {
            QRTools.doNothing(e);
        }
        return false;
    }

    /**
     * 去除所有非中文和非中文标点的内容算法
     */
    public static String notSupportedCharClear(String text) {
        if (text.isEmpty()) {
            return "";
        }
        char[] c = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char value : c) {
            if ("、，。：！？“”；%《～》…〖〗（）「」〈〉·‘’—/\\1234567890".contains(String.valueOf(value)) || QRStringUtils.isEnglish(value)) {
                sb.append(value);
            } else if (QRStringUtils.isCharInRange(value, CHINESE_NORMAL) || QRStringUtils.isCharInRange(value, CHINESE_EXTRA)) {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    /**
     * 判断内容是否为中文文章内容
     */
    public static boolean isChineseArticle(String text) {
        if (text.isEmpty()) {
            return false;
        }
        final String string = isCharInRange(text.replaceAll("[、，。：！？“”；%《～》…〖〗（）「」〈〉·‘’—]", ""), CHINESE_NORMAL).toString();
        return string.isEmpty();
    }

    public static boolean isEmail(String str) {
        String regex = "[a-zA-Z_]*[0-9]*@(([a-zA-z0-9]-*)+\\.)+[a-zA-z\\-]+";
        return Pattern.compile(regex).matcher(str).matches();
    }

    /**
     * 最多只能有一个小数点
     *
     * @param num 字符串
     * @return 只要有一个不是数字，就会返回 false
     */
    public static boolean isNumber(String... num) {
        if (num.length == 0) {
            return false;
        }
        for (String s : num) {
            if (s == null || s.isEmpty()) {
                return false;
            }
            char[] c = s.toCharArray();
            int len = c.length;
            if (c[len - 1] == '.' || c[0] == '.') {
                return false;
            }
            int i = 0;
            if (c[0] == '-' || c[0] == '+') {
                i = 1;
            }
            boolean dotHas = false;
            for (; i < len; i++) {
                char ch = c[i];
                if (ch == 46) {
                    if (dotHas) {
                        return false;
                    }
                    dotHas = true;
                    continue;
                }
                // 47 - /
                // 58 - :
                if (!isNumber(ch)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 不能有小数点
     *
     * @param num 字符串
     * @return 只要有一个不是整数，就会返回 false
     */
    public static boolean isNumberStrict(String... num) {
        if (num.length == 0) {
            return false;
        }
        for (String s : num) {
            if (s == null || s.isEmpty()) {
                return false;
            }
            char[] c = s.toCharArray();
            int i = 0;
            if (c[0] == '-' || c[0] == '+') {
                i = 1;
            }
            for (int len = c.length; i < len; i++) {
                char ch = c[i];
                if (!isNumber(ch)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否只是一个数字
     *
     * @param c 字符
     */
    public static boolean isNumber(char c) {
        return isCharInRange(c, NUMBER_RANGE);
    }

    /**
     * 将一个字符的数字转为整型
     */
    public static int charToInt(char c) {
        if (isNumber(c)) {
            return c ^ 48;
        }
        return -1;
    }

    public static String notEnglishCharClear(String text) {
        final char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (char c : chars) {
            if (isEnglish(c) || SUPPORTED_ENGLISH_MARK.indexOf(c) != -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 返回字符串的最后的一字符
     */
    public static String lastChar(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        if (str.isBlank()) {
            return QRStringUtils.A_WHITE_SPACE;
        }
        int len = str.length();
        if (len == 1) {
            return str;
        }
        return String.valueOf(str.charAt(len - 1));
    }

    /**
     * 全角转半角
     */
    public static String fullWidthToHalf(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                //全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    public static String halfWidthToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                //全角空格为12288，半角空格为32
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] > 32 && c[i] < 127) {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    public static String htmlLineProcess(String lineText) {
        return lineText.replaceAll("<.+?>", "");
    }

    public static String htmlMarkProcess(String singleLineText) {
        final int startIndex = singleLineText.indexOf("<p>");
        final int endIndex = singleLineText.lastIndexOf("</p>");
        if (startIndex == -1 || endIndex == -1) {
            return "";
        }
        return htmlLineProcess(singleLineText.substring(startIndex + 3, endIndex)).replaceAll("&rdquo;", "").replaceAll("&ldquo;", "").replaceAll("</p>", "").replaceAll("<p>", "");
    }

    public static int getActualChineseLength(String str) {
        final String[] chineseExtraPhrase = getChineseExtraPhrase(str);
        return chineseExtraPhrase.length;
    }

    public static int getStringArrLen(String[] arr) {
        return Arrays.stream(arr).mapToInt(String::length).sum();
    }

    public static int getStringArrLen(List<String> arr) {
        int sum = 0;
        for (String s : arr) {
            int length = s.length();
            sum += length;
        }
        return sum;
    }

    public static int getStringArrLen(Set<String> arr) {
        return arr.stream().mapToInt(String::length).sum();
    }

    /**
     * 判断路径是否合法，但该方法仅支持英文路径
     *
     * @param path 路径
     */
    @Deprecated
    public static boolean checkPathValid(String path) {
        Pattern pattern;
        if (QRSystemUtils.IS_WINDOWS) {
            pattern = Pattern.compile("(^[A-Z]:(([\\\\/])([a-zA-Z0-9\\-_]){1,255}){1,255}|([A-Z]:([\\\\/])))");
        } else {
            pattern = Pattern.compile("(/([a-zA-Z0-9][a-zA-Z0-9_\\-]{0,255}/)*([a-zA-Z0-9][a-zA-Z0-9_\\-]{0,255})|/)");
        }
        return pattern.matcher(path).matches();
    }

    public static boolean isGrade(String grade) {
        String[] items = {"速度", "击键", "码长", "正文:", "小启", "成绩单:"};
        for (String s : items) {
            int index = grade.indexOf(s);
            if (index == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 使用前应该先判断这个是不是成绩单
     */
    public static float getGradeSpeed(String grade) {
        char space = ' ';
        final int speedStrIndex = grade.indexOf("速度") + 2;
        final int speedSpaceIndex = grade.indexOf(space, speedStrIndex);
        String speedStr = grade.substring(speedStrIndex, speedSpaceIndex);
        final int markIndex = speedStr.indexOf('/');
        if (markIndex != -1) {
            speedStr = speedStr.substring(0, markIndex);
        }
        return Float.parseFloat(speedStr);
    }

    /**
     * 一个字符串的清理方法
     *
     * @param strToClear        要清理的字符串
     * @param mustContainsChars 字符串中必须包含的不重复的元素
     * @return 清理后的字符串
     */
    public static String notContainsClear(String strToClear, String mustContainsChars) {
        final char[] chars = strToClear.toCharArray();
        StringBuilder sb = new StringBuilder(strToClear.length());
        for (char aChar : chars) {
            if (mustContainsChars.indexOf(aChar) != -1) {
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

    /**
     * 将一个字符串按照所有不是数字进行切割，如：
     * <code>255i255i255</code>，则将会以 <code>i</code> 为切割符切成三个整型数组
     *
     * @param str 要切割的字符串
     * @return 整型数组
     */
    public static int[] splitWithNotNumber(String str) {
        if (str == null) {
            return null;
        }
        final char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        LinkedList<Integer> nums = new LinkedList<>();
        for (char c : chars) {
            if (isNumberStrict(String.valueOf(c))) {
                sb.append(c);
            } else {
                if (sb.length() != 0) {
                    nums.add(Integer.valueOf(sb.toString()));
                    sb = new StringBuilder();
                }
            }
        }
        if (sb.length() != 0) {
            nums.add(Integer.valueOf(sb.toString()));
        }
        int[] n = new int[nums.size()];
        int i = 0;
        for (Integer num : nums) {
            n[i] = num;
            i++;
        }
        return n;
    }

    /**
     * 分本分割，可设置多个分割符
     *
     * @param str
     * @param mark
     * @return
     */
    public static String[] splitEnhance(String str, String... mark) {
        QRStringSearchResult startIndex;
        ArrayList<String> parts = new ArrayList<>();
        int foreIndex = 0;
        while ((startIndex = getForemostIndex(str, foreIndex, mark)) != null) {
            parts.add(str.substring(foreIndex, startIndex.index()));
            foreIndex = startIndex.index() + startIndex.keyword().length();
        }
        if (foreIndex != 0 && foreIndex != str.length()) {
            parts.add(str.substring(foreIndex));
        }
        return QRArrayUtils.stringListToArr(parts);
    }

    public static String[] splitToCharStr(String str) {
        char[] chars = str.toCharArray();
        String[] arr = new String[chars.length];
        int i = 0, charsLength = chars.length;
        while (i < charsLength) {
            arr[i] = String.valueOf(chars[i]);
            i++;
        }
        return arr;
    }

    /**
     * 将首字母转成大写
     */
    public static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] chars = str.toCharArray();
        chars[0] = toUpperCase(chars[0]);
        return String.valueOf(chars);
    }

    /**
     * 将内容转为大写
     *
     * @param text 英文句子
     * @return 全大写英文句子
     */
    public static String toUpperCase(String text) {
        if (text.isBlank()) {
            return text;
        }
        char[] chars = text.toCharArray();
        for (int i = 0, charsLength = chars.length; i < charsLength; i++) {
            char c = chars[i];
            if (c >= 97 && c <= 122) {
                chars[i] ^= 32;
            }
        }
        return new String(chars);
    }

    /**
     * 将内容转为小写
     *
     * @param text 英文句子
     * @return 全小写英文句子
     */
    public static String toLowerCase(String text) {
        if (text.isBlank()) {
            return text;
        }
        char[] chars = text.toCharArray();
        for (int i = 0, charsLength = chars.length; i < charsLength; i++) {
            char c = chars[i];
            if (c >= 65 && c <= 90) {
                chars[i] |= 32;
            }
        }
        return new String(chars);
    }

    public static char toUpperCase(char chars) {
        if (chars >= 97 && chars <= 122) {
            chars ^= 32;
        }
        return chars;
    }

    public static char toLowerCase(char chars) {
        if (chars >= 65 && chars <= 90) {
            chars |= 32;
        }
        return chars;
    }

    /**
     * 含有unicode 的字符串转一般字符串
     *
     * @param unicodeStr 混有 Unicode 的字符串
     * @return
     */
    public static String unicodeStr2String(String unicodeStr) {
        int length = unicodeStr.length();
        int count = 0;
        String regex = "\\\\u[a-f0-9A-F]{1,4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(unicodeStr);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String oldChar = matcher.group();
            String newChar = unicode2String(oldChar);
            int index = matcher.start();
            sb.append(unicodeStr, count, index);
            sb.append(newChar);
            count = index + oldChar.length();
        }
        sb.append(unicodeStr, count, length);
        return sb.toString();
    }

    /**
     * unicode 转字符串
     *
     * @param unicode 全为 Unicode 的字符串
     * @return
     */
    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }


    /**
     * 获取异常的位置
     */
    public static String getExceptionTraceInfo(Exception e) {
        StringBuilder debug = new StringBuilder(e.getMessage());
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            debug.append("\n\tat ").append(traceElement);
        }
        return debug.toString();
    }

    /**
     * 获取异常的位置
     *
     * @param e      异常信息
     * @param prefix 类名（含包名等）
     * @return 字符串
     */
    public static String getExceptionTraceInfo(Exception e, String prefix) {
        StringBuilder debug = new StringBuilder(e.getMessage());
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            String toString = traceElement.toString();
            if (toString.startsWith(prefix)) {
                debug.append("\n\tat ").append(traceElement);
            }
        }
        return debug.toString();
    }


    /**
     * 重复字符串，取自{@code commons-lang3-3.9}
     *
     * @param str    要重复的字符串
     * @param repeat 重复次数
     * @return 字符串
     */
    public static String repeat(final String str, final int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return "";
        }
        final int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= 8192) {
            return repeat(str.charAt(0), repeat);
        }
        final int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                return repeat(str.charAt(0), repeat);
            case 2:
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                return str.repeat(repeat);
        }
    }

    /**
     * 字符串的关联度计算
     */
    public static float stringRelevancy(String str1, String str2) {
        int a = stringContains(str1, str2);
        int b = stringContains(str2, str1);
        return QRMathUtils.floatFormat((a + b) / (str1.length() + str2.length() + 0.0f), 2);
    }

    /**
     * 身份证号校验算法
     *
     * @param id 身份证
     * @return 是否正确
     */
    public static boolean isIdCard(String id) {
        if (id.length() != 18) {
            return false;
        }
        char[] chars = id.toCharArray();
        if (chars[17] == 'x') {
            chars[17] = 'X';
        }
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int sum = IntStream.range(0, weight.length).map(i -> weight[i] * (chars[i] - 48)).sum();
        int index = sum % 11;
        String check = "10X98765432";
        return check.charAt(index) == chars[17];
    }

    /**
     * @param text       源文本
     * @param part       源文本中的一部分
     * @param startIndex 该部分的开始位置
     * @return 判断该部分在源文本中是否未被切断，即单词完整
     */
    public static boolean inWords(String text, String part, int startIndex) {
        if (startIndex != 0) {
            char foreChar = text.charAt(startIndex - 1);
            if (!ENGLISH_DIVIDE_MARK.contains(foreChar)) {
                return false;
            }
        }
        int length = part.length();
        int endIndex = startIndex + length;
        if (endIndex != text.length()) {
            char backChar = text.charAt(endIndex);
            if (backChar == '\'' && endIndex != text.length() - 1) {
                return !isAlphabet(text.charAt(endIndex + 1));
            }
            return ENGLISH_DIVIDE_MARK.contains(backChar);
        }
        return true;
    }

//	public static String[] englishParticiple(String text){
//		String[] split = text.split(" ");
//		String[] result = new String[split.length];
//		for (int i = 0; i < split.length; i++) {
//
//
//		}
//	}

    //region repeat

    /**
     * 重复字符，取自{@code commons-lang3-3.9}
     *
     * @param ch     要重复的字符
     * @param repeat 重复次数
     * @return 字符串
     */
    public static String repeat(final char ch, final int repeat) {
        if (repeat <= 0) {
            return "";
        }
        if (repeat == 1) {
            return String.valueOf(ch);
        }
        final char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }


    /**
     * 从文段中找到重复了的字符
     *
     * @param texts 文段
     * @return 重复了的字符数组
     */
    public static char[] findRepeatChars(String texts) {
        Set<Character> charSet = new HashSet<>();
        char[] chars = texts.toCharArray();
        ArrayList<Character> list = new ArrayList<>(chars.length);
        for (char c : chars) {
            if (charSet.contains(c)) {
                list.add(c);
            } else {
                charSet.add(c);
            }
        }
        list.trimToSize();
        char[] charArray = new char[list.size()];
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            charArray[i] = list.get(i);
        }
        return charArray;
    }

    /**
     * 从文段中找到重复了的字符，并提取出现的次数
     *
     * @param texts 文段
     * @return 重复了的字符数组
     */
    public static Map<Character, Integer> findRepeatCharsAndAppearTimes(String texts) {
        char[] chars = texts.toCharArray();
        Map<Character, Integer> list = new HashMap<>();
        for (char c : chars) {
            list.merge(c, 1, Integer::sum);
        }
        return list;
    }

    /**
     * 从文段中找到未重复的字符
     *
     * @param texts 文段
     * @return 重复了的字符数组
     */
    public static char[] findNotRepeatChars(String texts) {
        Set<Character> charSet = new HashSet<>();
        char[] chars = texts.toCharArray();
        ArrayList<Character> list = new ArrayList<>(chars.length);
        for (char c : chars) {
            if (charSet.contains(c)) {
                list.add(c);
            } else {
                charSet.add(c);
            }
        }
        list.forEach(charSet::remove);
        char[] charArray = new char[charSet.size()];
        int i = 0;
        for (Character c : charSet) {
            charArray[i++] = c;
        }
        return charArray;
    }

    /**
     * 从该列表中取得未重字符串列表
     *
     * @param list 可能有内容重复的列表
     * @return 去掉了所有重复了的元素的字符串列表
     */
    public static ArrayList<String> getNotRepeatElement(List<String> list) {
        Set<String> set = new HashSet<>();
        LinkedList<String> repeat = new LinkedList<>();
        list.forEach(str -> {
            if (!set.add(str)) {
                repeat.add(str);
            }
        });
        repeat.forEach(set::remove);
        return new ArrayList<>(set);
    }

    /**
     * 从该列表中取得重复了的字符串列表
     *
     * @param list 可能有内容重复的列表
     * @return 重复了的字符串列表
     */
    public static ArrayList<String> getRepeatElement(List<String> list) {
        Set<String> set = new HashSet<>();
        LinkedList<String> repeat = new LinkedList<>();
        list.forEach(str -> {
            if (!set.add(str)) {
                repeat.add(str);
            }
        });
        return new ArrayList<>(repeat);
    }

    /**
     * 安全地向列表中添加另一个列表，使得原列表内容不重
     *
     * @param list     添加进的列表
     * @param elements 添加内容的列表
     * @return 添加后的不重列表
     */
    public static List<String> getNoRepeatList(List<String> list, List<String> elements) {
        Set<String> set = new HashSet<>(list);
        elements.stream().filter(set::add).forEachOrdered(list::add);
        return list;
    }

    /**
     * 字符串去重
     *
     * @param str 所有待去重的字符串
     * @return 返回传入的所有字符串中去重了的组合
     */
    public static String getNoRepeatString(String... str) {
        if (str == null) {
            return "";
        }
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (String s : str) {
            if (s == null) {
                continue;
            }
            String[] parts = QRStringUtils.getChineseExtraPhrase(s);
            for (String part : parts) {
                if (set.add(part)) {
                    list.add(part);
                }
            }
        }
        if (list.isEmpty()) {
            return "";
        }
        if (list.size() == 1)
            return list.get(0);
        return QRStringUtils.join(list, "");
    }
    //endregion repeat

    //region KeyEvent, KeyStroke, String

    /**
     * 返回格式为：{@code 86+130}、{@code 86}
     */
    public static String getKeyStrokeValue(KeyStroke key) {
        int modifiers = key.getModifiers();
        String modifiersExText = InputEvent.getModifiersExText(modifiers);
        if (modifiersExText.isEmpty()) {
            return String.valueOf(key.getKeyCode());
        } else {
            return key.getKeyCode() + "+" + modifiers;
        }
    }

    /**
     * 返回格式为：{@code 86+130}、{@code 86}
     */
    public static String getKeyStrokeValue(KeyEvent key) {
        int modifiers = key.getModifiersEx();
        String modifiersExText = InputEvent.getModifiersExText(modifiers);
        if (modifiersExText.isEmpty()) {
            return String.valueOf(key.getKeyCode());
        } else {
            return key.getKeyCode() + "+" + modifiers;
        }
    }

    /**
     * 返回格式为：{@code Ctrl + Alt + Shift + S}、{@code F12}
     */
    public static String getKeyStrokeString(KeyStroke key) {
        String str = key.toString();
        String letters = QRStringUtils.captureName(str.substring(str.lastIndexOf(QRStringUtils.A_WHITE_SPACE) + 1));
        if (str.startsWith("pressed") || str.startsWith("typed") || str.startsWith("released")) {
            return letters;
//			return String.valueOf(key.getKeyCode());
        } else {
            String modifiers = InputEvent.getModifiersExText(key.getModifiers()).replaceAll("\\+", " + ");
            return modifiers + " + " + letters;
//			return key.getModifiers() + "+" + key.getKeyCode();
        }
    }

    /**
     * 返回格式为：{@code Ctrl + Alt + Shift + S}、{@code F12}
     */
    public static String getKeyEventString(KeyEvent e) {
        StringBuilder sb = new StringBuilder();
        int keyCode = e.getKeyCode();
        if (e.isControlDown() || e.isAltDown() || e.isShiftDown()) {
            sb = new StringBuilder();
            if (e.isControlDown()) {
                sb.append("Ctrl").append(" + ");
            }
            if (e.isAltDown()) {
                sb.append("Alt").append(" + ");
            }
            if (e.isShiftDown()) {
                sb.append("Shift").append(" + ");
            }
        }
        if (keyCode > 18 || keyCode == 10) {
            sb.append(KeyEvent.getKeyText(keyCode));
        } else {
            return sb.substring(0, sb.lastIndexOf("+") - 1);
        }
        return sb.toString();
    }

    public static KeyStroke getKeyStroke(KeyEvent e) {
        return KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiersEx());
    }

    public static KeyStroke getKeyStroke(int... values) {
        if (values.length == 1) {
            return KeyStroke.getKeyStroke(values[0], 0);
        } else if (values.length == 2) {
            return KeyStroke.getKeyStroke(values[0], values[1]);
        }
        return null;
    }

    /**
     * 有+号则优先以+号分割，再以空格分割
     * <p>支持格式{@code Ctrl + Alt + Shift + s}、{@code a}、{@code shift a}
     */
    public static KeyStroke getKeyStroke(String str) {
        if (str != null) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(str.replace("\t", "TAB"));
            if (keyStroke != null) {
                return keyStroke;
            }
            if (str.endsWith("+")) {
                char[] chars = str.toCharArray();
                chars[chars.length - 1] = '\u200B';
                str = new String(chars);
            }
            if (str.equals(" ")) {
                return KeyStroke.getKeyStroke("space8");
            }
            if (str.contains(" ") || str.contains("+")) {
                String[] split = str.contains("+") ? str.split("\\+") : str.replaceAll(" {2,}", " ").split(" ");
                int length = split.length;
                if (length > 0) {
                    for (int i = 0; i < length - 1; i++) {
                        split[i] = split[i].trim().toLowerCase(Locale.ROOT);
                    }
                    if (split[length - 1].charAt(0) == '\u200B') {
                        split[length - 1] = "+";
                    } else if (split[length - 1].charAt(0) == '\u200C') {
                        split[length - 1] = " ";
                    } else {
                        split[length - 1] = split[length - 1].trim().toUpperCase(Locale.ROOT);
                    }
                    return KeyStroke.getKeyStroke(String.join(" ", split));
                }
            } else {
                return KeyStroke.getKeyStroke(str.toUpperCase(Locale.ROOT));
            }
        }
        return null;
    }
    //endregion
}