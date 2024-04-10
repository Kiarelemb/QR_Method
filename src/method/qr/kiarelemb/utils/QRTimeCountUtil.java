package method.qr.kiarelemb.utils;

/**
 * @author Kiarelemb QR
 * @date 2021/9/22 下午9:29
 * @apiNote 时间的工具类
 */
public class QRTimeCountUtil {
	private final long totalStartTime;
	private long startTime;
	private boolean startTimeUpdated = false;
	private int min = 2;
	private long sec = 5;
	private short mmm = 5;

	public QRTimeCountUtil(int min) {
		this();
		this.min = min;
	}

	public QRTimeCountUtil(long sec) {
		this();
		this.sec = sec;
	}

	public QRTimeCountUtil(short mm) {
		this();
		mmm = mm;
	}

	public QRTimeCountUtil() {
		this.totalStartTime = System.currentTimeMillis();
		this.startTime = this.totalStartTime;
	}

	/**
	 * 查看上次更新后的时间与开始取得时间的差值是否在一秒内
	 */
	public boolean updateAndCheckTimeDiff() {
		final long get = get();
		if (get > 1000) {
			return false;
		}
		startTimeUpdate();
		return true;
	}

	/**
	 * 如果有调用{@link #startTimeUpdate()}，则返回最后一次调用{@code startTimeUpdate()}至调用该方法时的时间差；
	 * <p>如果没有，则结果等于 {@link #endAndGetTotal()}
	 */
	public String endAndGet() {
		return endAndGet("");
	}

	public String endAndGet(String prefix) {
		if (!startTimeUpdated) {
			return endAndGetTotal(prefix);
		} else {
			final long total = get();
			double totalTimeInSec = total / 1000D;
			double totalTimeInMin = total / 1000D / 60;
			return prefix + "耗时: " + QRTimeUtils.getTimeCost(totalTimeInSec, totalTimeInMin);
		}
	}

	/**
	 * 取得从对象开始建立到调用该方法时的时间差
	 */
	public String endAndGetTotal() {
		final long total = getTotal();
		double totalTimeInSec = total / 1000D;
		double totalTimeInMin = total / 1000D / 60;
		return "共计耗时: " + QRTimeUtils.getTimeCost(totalTimeInSec, totalTimeInMin);
	}

	public String endAndGetTotal(String prefix) {
		final long total = getTotal();
		double totalTimeInSec = total / 1000D;
		double totalTimeInMin = total / 1000D / 60;
		return prefix + "共计耗时: " + QRTimeUtils.getTimeCost(totalTimeInSec, totalTimeInMin);
	}

	public long get() {
		return System.currentTimeMillis() - startTime;
	}

	public long getAndUpdate() {
		final long currentTimeMillis = System.currentTimeMillis();
		long diff = currentTimeMillis - startTime;
		startTime = currentTimeMillis;
		startTimeUpdated = true;
		return diff;
	}

	public long getTotal() {
		return System.currentTimeMillis() - totalStartTime;
	}

	public void startTimeUpdate() {
		this.startTime = System.currentTimeMillis();
		startTimeUpdated = true;
	}

	public boolean isPassedLongTime() {
		final long now = System.currentTimeMillis();
		return (now - startTime) / (60 * 1000) > min;
	}

	public boolean isPassedSecTime() {
		final long now = System.currentTimeMillis();
		return (now - startTime) / 1000 > sec;
	}

	public boolean isPassedMmTime() {
		final long now = System.currentTimeMillis();
		return (now - startTime) > mmm;
	}

	public int min() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public long sec() {
		return sec;
	}

	public void setSec(long sec) {
		this.sec = sec;
	}

	public short mmm() {
		return mmm;
	}

	public void setMmm(short mmm) {
		this.mmm = mmm;
	}
}
