package method.qr.kiarelemb.utils;

import method.qr.kiarelemb.utils.data.QRTextSendData;

import java.util.Scanner;

/**
 * @author Kiarelemb
 * @projectName QR_Method
 * @className Test
 * @description TODO
 * @create 2024/4/17 21:59
 */
public class Test {


	public static void main(String[] args) {
		String filePath = "F:\\IdeaProjects\\LYTyper\\res\\type\\局外人.txt";
		Scanner scanner = new Scanner(System.in);
		long index = 0;
		int length = 60;
		QRTextSendData sendData = null;
		while (true) {
			if ("1".equals(scanner.nextLine())) {
				// 下一段
				sendData = QRFileUtils.fileReaderByRandomAccessMarkPositionFind(filePath, index, length, true);
				if (sendData != null) {
					index += sendData.typeTextByteLen();
					System.out.println("[下一段 " + index + "]" + sendData.text());
				}
			} else if (sendData != null) {
				index -= sendData.typeTextByteLen();
				// 上一段
				sendData = QRFileUtils.fileForeReaderByRandomAccessMarkPositionFind(filePath, index,
						length, true);
				if (sendData != null) {
					System.out.println("[上一段 " + index + "]" + sendData.text());
				}
			}
		}
	}
}