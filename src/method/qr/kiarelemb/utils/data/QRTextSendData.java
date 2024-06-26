package method.qr.kiarelemb.utils.data;

import java.io.Serializable;
import java.util.StringJoiner;

public class QRTextSendData implements Serializable {
    private static final long serialVersionUID = 541669446331057502L;

    private final String text;
    private final long startIndex;
    private final boolean isEnglish;
    private final int typeTextByteLen;

    public QRTextSendData(String text, long startIndex, boolean isEnglish, int typeTextByteLen) {
        this.text = text;
        this.startIndex = startIndex;
        this.isEnglish = isEnglish;
        this.typeTextByteLen = typeTextByteLen;
    }

    public String text() {
        return text;
    }

    public long startIndex() {
        return startIndex;
    }

    public boolean isEnglish() {
        return isEnglish;
    }

    public int typeTextByteLen() {
        return typeTextByteLen;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "发文进度：[", "]")
                .add("startIndex=" + startIndex)
                .add("isEnglish=" + isEnglish)
                .add("typeTextByteLen=" + typeTextByteLen)
                .add("text='" + text + "'")
                .toString();
    }
}