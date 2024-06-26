package method.qr.kiarelemb.utils;

import method.qr.kiarelemb.utils.data.QRStringRandomData;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:39
 * @apiNote 数组或集合的工具类
 */
public class QRArrayUtils {
    /**
     * 在一组char字符数组中，判断是否有重复元素
     */
    public static boolean checkRepeatEle(char[] group) {
        Character[] c = IntStream
                .range(0, group.length)
                .mapToObj(i -> group[i])
                .toArray(Character[]::new);
        return checkRepeatEle(c);
    }

    public static boolean checkRepeatEle(Object[] group) {
        Set<Object> s = new HashSet<>();
        Collections.addAll(s, group);
        return s.size() != group.length;
    }

    /**
     * 将多个空格转为一个空格
     */
    public static String spaceFormat(String text) {
        for (String s : Arrays.asList("\r+", "\n{2,}")) {
            text = text.replaceAll(s, QRStringUtils.AN_ENTER);
        }
        return text.replaceAll(" {2,}", QRStringUtils.A_WHITE_SPACE).trim();
    }

    /**
     * 从零开始，向 LinkedList 中添加 max 个数
     *
     * @param max 最后的值为max - 1
     */
    public static LinkedList<Integer> getOrderedIntegers(int max) {
        return IntStream.range(0, max)
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * 字符串结合单位乱序算法
     */
    public static String[] getRandomPhrase(String[] str) {
        if (str.length == 1) {
            return str;
        }
        ArrayList<QRStringRandomData> asr = stringRandomDataRandom(str);
        return asr.stream()
                .map(QRStringRandomData::text)
                .collect(Collectors.toCollection(ArrayList::new))
                .toArray(QRStringUtils.ARR_EMPTY);
    }


    public static Object[] getRandomObject(List<?> list) {
        Object[] objects = list.toArray(new Object[0]);
        if (list.size() == 1) {
            return objects;
        }
        ArrayList<QRStringRandomData> asr = stringRandomDataRandom(objects);
        return asr.stream()
                .map(QRStringRandomData::text)
                .toArray(Object[]::new);
    }

    /**
     * stringRandomData的乱序算法
     */
    private static ArrayList<QRStringRandomData> stringRandomDataRandom(Object[] str) {
        int length = str.length;
        LinkedList<Integer> li = getOrderedIntegers(length);
        ArrayList<QRStringRandomData> asr = new ArrayList<>();
        int i = 0;
        while (!li.isEmpty()) {
            int num = li.get(QRRandomUtils.getRandomInt(li.size()));
            if (i != num) {
                asr.add(new QRStringRandomData(str[i], num));
                i++;
                li.remove(Integer.valueOf(num));
            } else if (i == length - 1 && li.size() == 1 && li.get(0) == i) {
                return stringRandomDataRandom(str);
            }
        }
        //对每个字对应的随机数进行排序
        Collections.sort(asr);
        return asr;
    }

    /**
     * 将字符串按指定的长度进行分割
     *
     * @param str 字符串
     * @param len 指定长度
     */
    public static LinkedList<String> splitWithLength(String str, int len) {
        char[] ch = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        LinkedList<String> arr = new LinkedList<>();
        for (int i = 1; i <= ch.length; i++) {
            char c = ch[i - 1];
            if (i % len == 0) {
                sb.append(c);
                arr.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }
            sb.append(c);
        }
        final String string = sb.toString();
        if (!string.isEmpty()) {
            arr.add(string);
        }
        return arr;
    }

    /**
     * 将英文文本按指定长度不分割单词地进行拆分
     *
     * @param englishText 文本
     * @param len         长度
     */
    public static LinkedList<String> splitWithLengthEnglish(String englishText, int len) {
        StringBuilder sb = new StringBuilder();
        LinkedList<String> arr = new LinkedList<>();
        String[] split = QRStringUtils.englishSplit(englishText);
        for (String str : split) {
            sb.append(str).append(" ");
            if (sb.length() > len) {
                arr.add(sb.toString().trim());
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0) {
            arr.add(sb.toString().trim());
        }
        return arr;
    }

    /**
     * 将字符串切割后转为int数组
     */
    public static Integer[] splitToInteger(String str, char mark) {
        char[] ch = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        LinkedList<Integer> arr = new LinkedList<>();
        for (char c : ch) {
            if (c == mark) {
                String num = sb.toString();
                if (QRStringUtils.isNumber(num)) {
                    arr.add(Integer.parseInt(num));
                }
                sb = new StringBuilder();
                continue;
            }
            sb.append(c);
        }
        String num = sb.toString();
        if (QRStringUtils.isNumber(num)) {
            arr.add(Integer.parseInt(num));
        }
        return arr.toArray(new Integer[0]);
    }

    public static int[] splitToInt(String str, char mark) {
        char[] ch = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        LinkedList<Integer> arr = new LinkedList<>();
        for (char c : ch) {
            if (c == mark) {
                String num = sb.toString().trim();
                if (QRStringUtils.isNumber(num)) {
                    arr.add(Integer.parseInt(num));
                }
                sb = new StringBuilder();
                continue;
            }
            sb.append(c);
        }
        String num = sb.toString();
        if (QRStringUtils.isNumber(num)) {
            arr.add(Integer.parseInt(num));
        }
        return listToArr(arr);
    }

    public static byte[] arrayContact(byte[] a, byte[] b) {
        int zero = 0;
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, zero, c, zero, a.length);
        System.arraycopy(b, zero, c, a.length, b.length);
        return c;
    }

    /**
     * String[]数组列表转String[][]二维数组
     *
     * @param ls String[]数组列表
     * @return String[][]二维数组
     */
    public static String[][] getData(LinkedList<String[]> ls) {
        String[][] arr = new String[ls.size()][];
        int i = 0;
        for (String[] strings : ls) {
            arr[i++] = strings;
        }
        return arr;
    }

    /**
     * 从前往后替换一次
     */
    public static String charReplaceOnce(final char[] chars, char findChar, char replaceChar) {
        for (int i = 0, charsLength = chars.length; i < charsLength; i++) {
            char aChar = chars[i];
            if (aChar == findChar) {
                chars[i] = replaceChar;
                return new String(chars);
            }
        }
        return new String(chars);
    }

    public static char[] charReplaceAll(final char[] chars, char findChar, char replaceChar) {
        for (int i = 0, charsLength = chars.length; i < charsLength; i++) {
            char aChar = chars[i];
            if (aChar == findChar) {
                chars[i] = replaceChar;
            }
        }
        return chars;
    }

    /**
     * 从指定的byte数组中，搜索指定字符串编码解码后的byte数组，并返回位置
     */
    public static int byteIndex(byte[] b, String searchStr, Charset charset) {
        byte[] mark = searchStr.getBytes(charset);
        int markLen = mark.length;
        for (int i = b.length - 1; i >= 0; i--) {
            byte bb = b[i];
            if (mark[markLen - 1] == bb) {
                int i0 = i - 1;
                boolean flag = false;
                for (int j = mark.length - 2; j >= 0 && i0 >= 0; j--, i0--) {
                    byte b1 = mark[j];
                    if (b1 != b[i0]) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return i - markLen + 1;
                }
            }
        }
        return -1;
    }

    public static int getStringArrayLength(String[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int length = 0;
        for (String s : arr) {
            length += s.length();
        }
        return length;
    }

    public static String getArraySubstring(String[] arr, int startIndex, int cutLength) {
        StringBuilder sb = new StringBuilder();
        int length = 0;
        for (int i = startIndex; i < arr.length; i++) {
            final String str = arr[i];
            int tmpLen = length + str.length();
            if (tmpLen < cutLength) {
                sb.append(str);
                length += str.length();
            } else {
                sb.append(str, 0, cutLength - length);
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 数组逆序算法，取自commons
     */
    public static String[] reverse(final String[] array) {
        if (array == null) {
            return null;
        }
        int i = 0;
        int j = array.length - 1;
        String tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static int[] listToArr(Collection<Integer> l) {
        final Integer[] array = l.toArray(new Integer[0]);
        return Arrays.stream(array).mapToInt(integer -> integer).toArray();
    }

    public static int[] listToArr(List<Integer> l) {
        final Integer[] array = l.toArray(new Integer[0]);
        return Arrays.stream(array).mapToInt(integer -> integer).toArray();
    }

    public static int[] listToArr(Set<Integer> l) {
        final Integer[] array = l.toArray(new Integer[0]);
        return Arrays.stream(array).mapToInt(integer -> integer).toArray();
    }

    public static String[] stringListToArr(List<String> list) {
        return list.toArray(QRStringUtils.ARR_EMPTY);
    }

    public static String[] stringListToArr(Set<String> list) {
        return list.toArray(QRStringUtils.ARR_EMPTY);
    }

    /**
     * 列表搜索
     *
     * @param list      要搜索的数组
     * @param searchStr 要搜索的字符串
     * @return 包含字符串的列表
     */
    public static LinkedList<String> arrayIndex(String[] list, String searchStr) {
        return arrayIndex(Arrays.asList(list), searchStr);
    }

    /**
     * 列表搜索
     *
     * @param list      要搜索的列表
     * @param searchStr 要搜索的字符串
     * @return 包含字符串的列表
     */
    public static LinkedList<String> arrayIndex(List<String> list, String searchStr) {
        return list.stream().filter(s -> s.contains(searchStr)).collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * 列表搜索{@link QRStringUtils#stringContainsAll}
     *
     * @param list       要搜索的列表
     * @param searchStrs 要搜索的字符串数组
     */
    public static String[] arrayContainsAll(List<String> list, String[] searchStrs) {
        ArrayList<String> arr = list.stream()
                .filter(parentStr -> QRStringUtils.stringContainsAll(parentStr, searchStrs))
                .collect(Collectors.toCollection(ArrayList::new));
        return stringListToArr(arr);
    }

    /**
     * 列表搜索{@link QRStringUtils#stringContainsAny}
     *
     * @param list       要搜索的列表
     * @param searchStrs 要搜索的字符串数组
     */
    public static String[] arrayContainsAny(List<String> list, String[] searchStrs) {
        ArrayList<String> arr = list.stream()
                .filter(parentStr -> QRStringUtils.stringContainsAny(parentStr, searchStrs))
                .collect(Collectors.toCollection(ArrayList::new));
        return stringListToArr(arr);
    }

    /**
     * 列表搜索{@link QRStringUtils#stringContainsNon}
     *
     * @param list       要搜索的列表
     * @param searchStrs 要搜索的字符串数组
     */
    public static String[] arrayContainsNon(List<String> list, String[] searchStrs) {
        ArrayList<String> arr = list.stream()
                .filter(parentStr -> QRStringUtils.stringContainsNon(parentStr, searchStrs))
                .collect(Collectors.toCollection(ArrayList::new));
        return stringListToArr(arr);
    }


    /**
     * 列表搜索{@link QRStringUtils#stringContainsVag}
     *
     * @param list       要搜索的列表
     * @param searchStrs 要搜索的字符串数组
     */
    public static String[] arrayContainsVag(List<String> list, String[] searchStrs) {
        ArrayList<String> arr = list.stream()
                .filter(parentStr -> QRStringUtils.stringContainsVag(parentStr, searchStrs))
                .collect(Collectors.toCollection(ArrayList::new));
        return stringListToArr(arr);
    }

    /**
     * 列表搜索{@link QRStringUtils#stringStartsWithAny}
     *
     * @param list       要搜索的列表
     * @param searchStrs 要搜索的字符串数组
     */
    public static String[] arrayStartsWithAny(List<String> list, String[] searchStrs) {
        ArrayList<String> arr = list.stream()
                .filter(parentStr -> QRStringUtils.stringStartsWithAny(parentStr, searchStrs))
                .collect(Collectors.toCollection(ArrayList::new));
        return stringListToArr(arr);
    }

    /**
     * 列表搜索{@link QRStringUtils#stringEndsWithAny}
     *
     * @param list       要搜索的列表
     * @param searchStrs 要搜索的字符串数组
     */
    public static String[] arrayEndsWithAny(List<String> list, String[] searchStrs) {
        ArrayList<String> arr = list.stream()
                .filter(parentStr -> QRStringUtils.stringEndsWithAny(parentStr, searchStrs))
                .collect(Collectors.toCollection(ArrayList::new));
        return stringListToArr(arr);
    }

    /**
     * 用于判断子数组中的前一部分元素是否在父数组中以结尾的形式存在
     *
     * @param parent 父数组
     * @param child  子数组
     * @return 父数组是否以子数组的前部分结尾
     */
    public static boolean arrayEndsWithPro(String[] parent, String[] child) {
        for (int i = child.length - 1; i >= 0; i--) {
            String last = child[i];
            int lastIndex = parent.length - 1;
            if (objectIndexOf(parent, last, 0) == lastIndex) {
                lastIndex--;
                for (int j = i - 1; j >= 0 && lastIndex >= 0; j--, lastIndex--) {
                    if (!Objects.equals(parent[lastIndex], child[j])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 摘自 ArrayUtils。
     * 从 {@code 0} 开始，查找数组中给定对象的索引。如果输入数组为空，此方法返回 {@code -1}。
     *
     * @param array        要搜索对象的数组，可能为 {@code null}
     * @param objectToFind 要查找的对象，可能为 {@code null}
     * @return 数组中从索引开始的对象的索引，如果未找到或数组输入为空，则为 {@code -1}
     */
    public static int objectIndexOf(final Object[] array, final Object objectToFind) {
        return objectIndexOf(array, objectToFind, 0);
    }

    /**
     * 摘自 ArrayUtils。
     * 从给定的索引开始，查找数组中给定对象的索引。如果输入数组为空，此方法返回 {@code -1}。
     *
     * @param array        要搜索对象的数组，可能为 {@code null}
     * @param objectToFind 要查找的对象，可能为 {@code null}
     * @param startIndex   开始搜索的索引
     * @return 数组中从索引开始的对象的索引，如果未找到或数组输入为空，则为 {@code -1}
     */
    public static int objectIndexOf(final Object[] array, final Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i < array.length; i++) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 集合内容比较，摘自 ListUtils
     *
     * @param list1 the first list, may be null
     * @param list2 the second list, may be null
     * @return whether the lists are equal by value comparison
     */
    public static boolean isEqualList(final Collection<?> list1, final Collection<?> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        final Iterator<?> it1 = list1.iterator();
        final Iterator<?> it2 = list2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            Object obj1 = it1.next();
            Object obj2 = it2.next();
            if (!Objects.equals(obj1, obj2)) {
                return false;
            }
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    /**
     * 字符串数组的首尾空格去除
     *
     * @param lines 所有行
     */
    public static String[] arrayTrims(String[] lines) {
        IntStream.range(0, lines.length).forEach(i -> lines[i] = lines[i].trim());
        return lines;
    }

    /**
     * 字符串数组的首尾空格去除
     *
     * @param lines 所有行
     */
    public static String[] arrayTrims(List<String> lines) {
        String[] arr = new String[lines.size()];
        int index = 0;
        for (String line : lines) {
            arr[index++] = line.trim();
        }
        return arr;
    }

    /**
     * 是否为空或含有空
     */
    public static boolean isNullOrContainsNull(Object[] arr) {
        if (arr == null) {
            return true;
        } else {
            int length = arr.length;
            for (Object o : arr) {
                if (o == null) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 字符串集合排其长度排序
     *
     * @param stringList      集合
     * @param fromShortToLong {@code true} 则从短到长排序
     * @return 集合
     */
    public static List<String> arrayRankByLength(List<String> stringList, boolean fromShortToLong) {
        if (fromShortToLong) {
            return stringList.stream().sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
        } else {
            return stringList.stream().sorted(Comparator.comparingInt(String::length).reversed()).collect(Collectors.toList());
        }
    }
}