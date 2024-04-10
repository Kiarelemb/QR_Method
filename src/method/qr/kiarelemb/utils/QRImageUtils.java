package method.qr.kiarelemb.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class QRImageUtils {
	/**
	 * 创建任意角度的旋转图像
	 *
	 * @param imageUrl 图像
	 * @param alpha    角度
	 * @return 返回调整后的图片对象
	 */
	public static BufferedImage rotateImage(URL imageUrl, double alpha) {
		BufferedImage image;
		try {
			image = ImageIO.read(imageUrl);
		} catch (IOException e) {
			return null;
		}
		int width = image.getWidth();
		int height = image.getHeight();
		double angle = alpha * Math.PI / 180;
		double[] xCords = getX(width / 2, height / 2, angle);
		double[] yCords = getY(width / 2, height / 2, angle);
		int WIDTH = (int) (xCords[3] - xCords[0]);
		int HEIGHT = (int) (yCords[3] - yCords[0]);
		BufferedImage resultImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				int x = i - WIDTH / 2;
				int y = HEIGHT / 2 - j;
				double radius = Math.sqrt(x * x + y * y);
				double angle1;
				if (y > 0) {
					angle1 = Math.acos(x / radius);
				} else {
					angle1 = 2 * Math.PI - Math.acos(x / radius);
				}
				x = (int) (radius * Math.cos(angle1 - angle));
				y = (int) (radius * Math.sin(angle1 - angle));
				int rgb;
				if (x < (width / 2) & x > -(width / 2) & y < (height / 2) & y > -(height / 2)) {
					rgb = image.getRGB(x + width / 2, height / 2 - y);
				} else {
					rgb = ((Color.WHITE.getRed() & 0xff) << 16) | ((Color.WHITE.getGreen() & 0xff) << 8)
					      | ((Color.WHITE.getBlue() & 0xff));
				}
				resultImage.setRGB(i, j, rgb);
			}
		}
		return resultImage;
	}

	// 获取四个角点旋转后Y方向坐标
	private static double[] getY(int i, int j, double angle) {
		double[] results = new double[4];
		double radius = Math.sqrt(i * i + j * j);
		double angle1 = Math.asin(j / radius);
		results[0] = radius * Math.sin(angle1 + angle);
		results[1] = radius * Math.sin(Math.PI - angle1 + angle);
		results[2] = -results[0];
		results[3] = -results[1];
		Arrays.sort(results);
		return results;
	}

	// 获取四个角点旋转后X方向坐标
	private static double[] getX(int i, int j, double angle) {
		double[] results = new double[4];
		double radius = Math.sqrt(i * i + j * j);
		double angle1 = Math.acos(i / radius);
		results[0] = radius * Math.cos(angle1 + angle);
		results[1] = radius * Math.cos(Math.PI - angle1 + angle);
		results[2] = -results[0];
		results[3] = -results[1];
		Arrays.sort(results);
		return results;
	}

	/**
	 * 输出文件至源目录，此方法不删除源文件，如果转换后的文件存在，则会被删除
	 *
	 * @param pngFilePath png文件路径
	 */
	public static void pngToJpg(String pngFilePath) throws IOException {
		pngToJpg(pngFilePath, false);
	}

	/**
	 * 输出文件至源目录，并设置是否删除源文件，如果转换后的文件存在，则会被删除
	 *
	 * @param pngFilePath        png文件路径
	 * @param originalFileDelete 为{@code true}则删除源文件
	 */
	public static void pngToJpg(String pngFilePath, boolean originalFileDelete) throws IOException {
		String outputFilePath = pngFilePath.substring(0, pngFilePath.lastIndexOf(".")) + ".jpg";
		QRFileUtils.fileDelete(outputFilePath);
		BufferedImage image = ImageIO.read(new File(pngFilePath));
		BufferedImage newBufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
		ImageIO.write(newBufferedImage, "jpg", new File(outputFilePath));
		if (originalFileDelete) {
			QRFileUtils.fileDelete(pngFilePath);
		}
	}

	/**
	 * 给一张图片贴 图片，并生成新图片
	 * @param bigPath  底图路径
	 * @param smallPath 要贴的图片路径
	 * @param outPath  合成输出图片路径
	 * @param x 贴图的位置
	 * @param y 贴图的位置
	 * @param smallWidth 要贴的图片宽度
	 * @param smallHeight 要贴的图片高度
	 * @throws IOException 抛出io异常
	 */
	public void mergeImage( String bigPath,
	                        String smallPath,
	                        String outPath,
	                        String x,
	                        String y,
	                        int smallWidth,
	                        int smallHeight ) throws IOException {
		try {
			//加载图片
			BufferedImage small = imageIoRead(smallPath);
			BufferedImage big = imageIoRead(bigPath);
			//得到2d画笔对象
			Graphics2D g = big.createGraphics();
			float fx = Float.parseFloat(x);
			float fy = Float.parseFloat(y);
			int x_i = (int)fx;
			int y_i = (int)fy;
			g.drawImage(small, x_i, y_i,smallWidth,smallHeight, null);
			g.dispose();
			//输出图片
			ImageIO.write(big, "png", new File(outPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向画布上写多行文字文字，自动居中
	 *
	 * @param filePath   原图路径
	 * @param text       要添加的文字
	 * @param outPath    输出图片路径
	 * @param font       字体
	 * @param x          坐标X
	 * @param y          坐标y
	 * @param color      字体颜色
	 * @param fontheight 字体高度
	 * @param maxWeight  每行字体最大宽度
	 * @param center     是否居中
	 * @param rate       字体间距
	 * @return int  写了几行字
	 */
	public int drawTextInImg(String filePath, String text, String outPath, Font font, int x, int y, Color color, int maxWeight, int fontheight, boolean center, double rate) {
		int row = 0;
		try {
			//图片加载到缓冲区
			BufferedImage bimage = imageIoRead(filePath);
			//得到2d画笔对象
			Graphics2D g = bimage.createGraphics();
			//设置填充颜色
			g.setPaint(color);
			//设置字体
			g.setFont(font);
			//调用写写文字方法
			row = drawString(g, font, text, x, y, maxWeight, fontheight, center, rate);
			g.dispose();
			//输出图片
			FileOutputStream out = new FileOutputStream(outPath);
			ImageIO.write(bimage, "png", out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return row;
	}

	/**
	 * 写文字
	 *
	 * @param g        2d画笔对象
	 * @param font     字体
	 * @param text     要添加的文字
	 * @param x        坐标X
	 * @param y        坐标y
	 * @param maxWidth 每行字体最大宽度
	 * @param height   字体高度
	 * @param center   是否居中
	 * @param rate     字体间距
	 * @return int 写了几行字
	 */
	public int drawString(Graphics2D g, Font font, String text, int x, int y, int maxWidth, int height, boolean center, double rate) {
		int row = 1;
		JLabel label = new JLabel(text);
		label.setFont(font);
		FontMetrics metrics = label.getFontMetrics(label.getFont());
		int textW = metrics.stringWidth(label.getText());
		String tempText = text;
		//如果字符串长度大于最大宽度，执行循环
		while (textW > maxWidth) {
			int n = textW / maxWidth;
			int subPos = tempText.length() / n;
			String drawText = tempText.substring(0, subPos);
			int subTxtW = metrics.stringWidth(drawText);
			while (subTxtW > maxWidth) {
				subPos--;
				drawText = tempText.substring(0, subPos);
				subTxtW = metrics.stringWidth(drawText);
			}
			//g.drawString(drawText, x, y);  //不调整字体间距
			drawString(drawText, x, y, rate, g);
			y += height;
			textW = textW - subTxtW;
			tempText = tempText.substring(subPos);
			row++;
		}
		//居中
		if (center) {
			x = x + (maxWidth - textW) / 2;
		}
		//g.drawString(tempText, x, y);  //不调整字体间距
		drawString(tempText, x, y, rate, g);
		return row;
	}

	/**
	 * 一个字一个字写，控制字体间距
	 *
	 * @param str  要添加的文字
	 * @param x    坐标x
	 * @param y    坐标y
	 * @param rate 字体间距
	 * @param g    画笔
	 */
	public void drawString(String str, int x, int y, double rate, Graphics2D g) {
		String tempStr = "";
		int orgStringWight = g.getFontMetrics().stringWidth(str);
		int orgStringLength = str.length();
		int tempX = x;
		int tempY = y;
		while (str.length() > 0) {
			tempStr = str.substring(0, 1);
			str = str.substring(1);
			g.drawString(tempStr, tempX, tempY);
			tempX = (int) (tempX + (double) orgStringWight / (double) orgStringLength * rate);
		}
	}

	/**
	 * 解析本地图片或者http网络图片，并把图片加载到缓冲区
	 *
	 * @param path 图片路径（本地路径或者网络图片http访问路径）
	 * @throws IOException 抛出异常
	 */
	public BufferedImage imageIoRead(String path) throws IOException {
		BufferedImage bufferedImage;
		if (path.contains("http")) {
			//网络图片
			bufferedImage = ImageIO.read(new URL(path));
		} else {
			//本地图片
			bufferedImage = ImageIO.read(new File(path));
		}
		return bufferedImage;
	}
}
