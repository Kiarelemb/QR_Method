package method.qr.kiarelemb.utils.data;

import java.io.Serializable;

public class QRTextSendData implements Serializable {
	@java.io.Serial
	private static final long serialVersionUID = 1377033238394485952L;

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
		final StringBuilder sb = new StringBuilder("QRTextSendData{");
		sb.append("text='").append(text).append('\'');
		sb.append(", startIndex=").append(startIndex);
		sb.append(", isEnglish=").append(isEnglish);
		sb.append(", typeTextByteLen=").append(typeTextByteLen);
		sb.append('}');
		return sb.toString();
	}
}