package method.qr.kiarelemb.utils.data;

public class QRTextSendData {
    private final String text;
    private final long startIndex;
    private final boolean isEnglish;

    public QRTextSendData(String text, long startIndex, boolean isEnglish) {
        this.text = text;
        this.startIndex = startIndex;
        this.isEnglish = isEnglish;
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
}
