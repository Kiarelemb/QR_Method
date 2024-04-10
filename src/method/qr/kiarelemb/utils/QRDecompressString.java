package method.qr.kiarelemb.utils;

import java.util.LinkedList;

import static method.qr.kiarelemb.utils.QRCompressString.START_CHAR_INDEX;
 class QRDecompressString {
    private final LinkedList<String> replaced;
    private String decompressedString;

    QRDecompressString(String strToDecompress) {
        final String mark = "#";
        replaced = new LinkedList<>();
        if (strToDecompress.startsWith(mark)) {
            strToDecompress = strToDecompress.substring(1);
            int splitIndex = strToDecompress.lastIndexOf(mark);
            decompressedString = strToDecompress.substring(0, splitIndex);
            String keys = strToDecompress.substring(splitIndex + 1);
            int i = START_CHAR_INDEX;
            StringBuilder sb = new StringBuilder();
            final char[] chars = keys.toCharArray();
            char next = (char) i;
            for (char c : chars) {
                if (c == next) {
                    replaced.add(sb.toString());
                    sb = new StringBuilder();
                    i--;
                    next = (char) i;
                } else {
                    sb.append(c);
                }
            }
            replaced.add(sb.toString());
        } else {
            decompressedString = strToDecompress;
        }
//        replaced.addAll(Arrays.asList(keys.split("-")));
    }

    public QRDecompressString doDecompress() {
        if (replaced.size() > 0) {
//        StringBuilder sb =new StringBuilder(strToDecompress);
            int replaceInt = START_CHAR_INDEX;
            for (String key : replaced) {
                replaceInt++;
                char replaceChar = (char) replaceInt;
                final String replaceStr = String.valueOf(replaceChar);
                decompressedString = decompressedString.replaceAll(replaceStr, key);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return decompressedString;
    }
}