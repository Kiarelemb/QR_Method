package method.qr.kiarelemb.utils.data;

public class QRStringRepeatSearchData {
    private final int index;
    private final int len;

  public QRStringRepeatSearchData(int index, int len) {
        this.index = index;
        this.len = len;
    }

    public int index() {
        return index;
    }

    public int len() {
        return len;
    }


}