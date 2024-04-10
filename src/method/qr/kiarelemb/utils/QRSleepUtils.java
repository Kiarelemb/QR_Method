package method.qr.kiarelemb.utils;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:46
 * @apiNote 线程休眠的工具类
 */
public class QRSleepUtils {

	public static void randomSleep(long mmMax) {
		long l = QRRandomUtils.getRandomLong(mmMax);
		sleep(l);
	}

	public static void sleep(long mm) {
		try {
			Thread.sleep(mm);
		} catch (Exception e) {
			QRTools.doNothing();
		}
	}

	public static void sleep(long mm, int nn) {
		try {
			Thread.sleep(mm, nn);
		} catch (Exception e) {
			QRTools.doNothing();
		}
	}

	public static void sleepSec(int sec) {
		sleep(sec * 1000L);
	}

	public static void sleepSec(long sec) {
		sleep(sec * 1000);
	}

	public static void sleepMin(int min) {
		sleep(min * 60 * 1000L);
	}
}
