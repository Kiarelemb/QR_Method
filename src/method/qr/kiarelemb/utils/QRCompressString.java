package method.qr.kiarelemb.utils;

import java.util.LinkedList;

/**
 * 字符串压缩算法
 */ class QRCompressString {
    public static final int START_CHAR_INDEX = 2 << 12;
    private final String strToCompress;
    private final LinkedList<String> replaced;
    private String compressedString;

    QRCompressString(String strToCompress) {
        this.strToCompress = strToCompress;
        replaced = new LinkedList<>();
    }

    public QRCompressString doCompress() {
        final int strLen = strToCompress.length();
        int replaceInt = START_CHAR_INDEX;
        //        String tmp = sb.toString();
        String result = strToCompress;
        for (int cutLen = Math.min(strLen / 2, 64); cutLen >= 2; cutLen--) {
//        for (int cutLen =strLen / 3; cutLen >= 2; cutLen--) {
            final int end = strLen - cutLen;
            for (int offIndex = 0; offIndex < end; offIndex++) {
                String childStr = strToCompress.substring(offIndex, offIndex + cutLen);
                LinkedList<Integer> indexList = getAllOccurredChildStrIndexes(result, childStr, offIndex);
                final int size = indexList.size();
                if (size > 2 || (size > 1 && cutLen != 2)) {
                    int sbSize = result.length() - cutLen * (size - 1);
                    StringBuilder sbR = new StringBuilder(sbSize);
                    final Integer[] indexes = indexList.toArray(new Integer[0]);
                    replaceInt++;
                    String replacement = String.valueOf((char) replaceInt);
//                    result = result.replaceAll(childStr, replacement);
                    sbR.append(result, 0, indexes[0]).append(replacement);
                    for (int i = 0; i < indexes.length - 1; i++) {
                        Integer index = indexes[i];
                        sbR.append(result, index + cutLen, indexes[i + 1]).append(replacement);
                    }
                    sbR.append(result, indexes[indexes.length - 1] + cutLen, result.length());
                    result = sbR.toString();
                    //从大位置开始替换
//                    for (int i = indexes.length - 1; i >= 0; i--) {
//                        Integer index = indexes[i];
//
//                        sb.replace(index, index + cutLen, replaceStr);
//                    }
                    replaced.add(childStr);
//                    tmp = sb.toString();
                }
            }
        }
        compressedString = result;
        return this;
    }

    private LinkedList<Integer> getAllOccurredChildStrIndexes(String tmp, String childStr, int offIndex) {
        LinkedList<Integer> indexList = new LinkedList<>();
        int strLen = tmp.length();
        int childLen = childStr.length();
        for (int startIndex = offIndex + 1; startIndex < strLen - childLen; ) {
            final int i = tmp.indexOf(childStr, startIndex);
            if (i != -1) {
                indexList.add(i);
                startIndex = i + childLen;
            } else {
                break;
            }
        }
        return indexList;
    }

    @Override
    public String toString() {
        System.out.print("原长：" + strToCompress.length());
        StringBuilder rules = new StringBuilder();
        rules.append("#").append(compressedString).append("#");
        int i = START_CHAR_INDEX;
        for (String s1 : replaced) {
            rules.append(s1).append((char) i);
            i--;
        }
        final int length = rules.length() -1;
        System.out.println("\t|\t压缩后：" + length);
        return rules.substring(0, length);
    }
}