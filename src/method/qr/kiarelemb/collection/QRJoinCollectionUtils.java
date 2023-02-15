package method.qr.kiarelemb.collection;

import java.util.Collection;

/**
 * @author Kiarelemb QR
 * @program: QR_Method
 * @description:
 * @create 2023-02-15 10:17
 **/
public class QRJoinCollectionUtils {

	/**
	 * 将列表组合为字符串
	 *
	 * @param collection 集合
	 * @param separator  连接符
	 * @return 连接后的字符串
	 */
	public static String join(final Collection<String> collection, String separator) {
		int separatorLen = separator.length();
		int length = collection.stream().mapToInt(String::length).sum() + collection.size() * separatorLen;
		final StringBuilder builder = new StringBuilder(length);
		for (String s : collection) {
			if (s != null) {
				builder.append(separator).append(s);
			}
		}
		return builder.substring(separatorLen);
	}
}
