package method.qr.kiarelemb.utils.data;

import java.io.Serializable;

public class QRStringSearchResult implements Serializable {
	private final int index;
	private final String keyword;

	public QRStringSearchResult(int index, String keyword) {
		this.index = index;
		this.keyword = keyword;
	}

	public int index() {
		return index;
	}

	public String keyword() {
		return keyword;
	}

	@Override
	public String toString() {
		return index + "[" + keyword + "]";
	}
}