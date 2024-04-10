package method.qr.kiarelemb.utils.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Kiarelemb QR
 * @date 2021/9/5 下午8:25
 * @apiNote 单字乱序的类
 */
public class QRCharRandomData implements Comparable<QRCharRandomData>, Serializable {
	private final char c;
	private final int num;

	public QRCharRandomData(char c, int num) {
		this.c = c;
		this.num = num;
	}

	@Override
	public String toString() {
		return c + " -- " + num;
	}

	@Override
	public int compareTo(QRCharRandomData o) {
		return this.num > o.num ? 1 : -1;
	}

	public char c() {
		return c;
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
		QRCharRandomData that = (QRCharRandomData) obj;
		return this.c == that.c &&
		       this.num == that.num;
	}

	@Override
	public int hashCode() {
		return Objects.hash(c, num);
	}

}