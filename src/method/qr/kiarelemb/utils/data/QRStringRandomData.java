package method.qr.kiarelemb.utils.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Kiarelemb QR
 * @date 2021/9/5 下午8:25
 * @apiNote 可多字乱序的类
 */
public class QRStringRandomData implements Comparable<QRStringRandomData>, Serializable {
	private final Object text;
	private final int num;

	public QRStringRandomData(Object text, int num) {
		this.text = text;
		this.num = num;
	}

	@Override
	public String toString() {
		return text + " -- " + num;
	}

	@Override
	public int compareTo(QRStringRandomData o) {
		return this.num > o.num ? 1 : -1;
	}

	public Object text() {
		return text;
	}

	public int num() {
		return num;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		QRStringRandomData that = (QRStringRandomData) obj;
		return Objects.equals(this.text, that.text) &&
				this.num == that.num;
	}

	@Override
	public int hashCode() {
		return Objects.hash(text, num);
	}

}