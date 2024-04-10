package method.qr.kiarelemb.utils;

import java.util.Locale;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 为减少打包的空间，复制了google的一个类
 * @date 2021/8/28 上午7:26
 * @apiNote 略有改动
 */
public class QRThreadBuilder {
	private String nameFormat = null;

	private QRThreadBuilder() {
	}

	private static ThreadFactory build(QRThreadBuilder builder) {
		final String nameFormat = builder.nameFormat;
		final ThreadFactory backingThreadFactory =
				Executors.defaultThreadFactory();
		final AtomicLong count = (nameFormat != null) ? new AtomicLong(0) : null;
		return runnable -> {
			Thread thread = backingThreadFactory.newThread(runnable);
			if (nameFormat != null) {
				thread.setName(format(nameFormat, count.getAndIncrement()));
			}
			return thread;
		};
	}

	private static String format(String format, Object... args) {
		return String.format(Locale.ROOT, format, args);
	}

	public QRThreadBuilder setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat;
		return this;
	}

	public ThreadFactory build() {
		return build(this);
	}

	public static ThreadPoolExecutor singleThread(String threadName) {
		String id = "QR_";
		int singleNum = 1;
		//线程名
		String name = id.concat(threadName);

		ThreadFactory threadNameVal = new QRThreadBuilder().setNameFormat(name).build();
		//线程池
		return new ThreadPoolExecutor(singleNum, 10, singleNum, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(10), threadNameVal, new ThreadPoolExecutor.AbortPolicy());
	}

	public static ThreadPoolExecutor thread(String threadName, int threadNum) {
		String id = "QR_";
		//线程名
		String name = id.concat(threadName);
		ThreadFactory threadNameVal = new QRThreadBuilder().setNameFormat(name).build();
		//线程池
		return new ThreadPoolExecutor(threadNum, 10, threadNum, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(10), threadNameVal, new ThreadPoolExecutor.AbortPolicy());
	}
}
