package method.qr.kiarelemb.utils.data;

import java.io.Serializable;

public class QRStringRepeatSearchData implements Serializable {
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