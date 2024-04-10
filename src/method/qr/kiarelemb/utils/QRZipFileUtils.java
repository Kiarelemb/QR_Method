package method.qr.kiarelemb.utils;

import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class QRZipFileUtils {

	/**
	 * 将这些文件压缩在{@code parentPath}文件夹中
	 *
	 * @param parentPath 父目录
	 * @param fileNames  父目录下的文件
	 * @return 是否成功
	 */
	public static boolean zipFile(String parentPath, String... fileNames) {
		boolean flag = false;
		try {
			// 判断 "压缩的根目录"是否存在! 是否是一个文件夹!
			File baseDir = new File(parentPath);
			// 得到这个 "压缩的根目录" 的绝对路径
			String baseDirPath = baseDir.getAbsolutePath();
			File fileName = new File(fileNames[0]);
			String zipFileName = fileName.getName().substring(0, fileName.getName().lastIndexOf('.'));
			String outputZipName = parentPath + zipFileName + ".kmb";
			File targetFile = new File(outputZipName);
			QRFileUtils.fileCreate(targetFile);
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFile));
			File[] files = new File[fileNames.length];
			for (int i = 0; i < files.length; i++) {
				files[i] = new File(new String(fileNames[i].getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
			}
			if (files[0].isFile()) {
				QRZipFileUtils.filesToZip(baseDirPath, files, out);
			}
			out.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * AntZip.unZip("/home/Chinese Articles/中文.zip", "/home/Chinese Articles/中文");
	 */
	public static boolean unZip(String ZipFileFullPath, String outputDirectory) {
		return unzipFile(ZipFileFullPath, outputDirectory, true);
	}

	public static boolean unZipEpubFile(String ZipFileFullPath, String outputDirectory) {
		return unzipFile(ZipFileFullPath, outputDirectory, false);
	}

	public static boolean unzipFile(String ZipFileFullPath, String outputDirectory, boolean normalFile) {
		boolean flag = false;
		try {
			org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(ZipFileFullPath);
			java.util.Enumeration e = zipFile.getEntries();
			String zipFileName = QRFileUtils.getFileName(ZipFileFullPath);
			org.apache.tools.zip.ZipEntry zipEntry;
			while (e.hasMoreElements()) {
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					String path = outputDirectory + File.separator + name;
					File f = new File(new String(path.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
					QRFileUtils.dirCreate(f);
				} else {
					final boolean endsWithHtml = zipEntry.getName().endsWith(".html");
					final boolean b = !normalFile && (!endsWithHtml || "catalog.html".equals(zipEntry.getName()));
					if (b) {
						continue;
					}
					String pathname;
					if (endsWithHtml) {
						pathname = outputDirectory + File.separator + zipEntry.getName();
					} else {
						pathname = outputDirectory + File.separator + zipFileName + QRFileUtils.getFileExtension(zipEntry.getName());
					}
					File f = new File(new String(pathname.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
					QRFileUtils.fileCreate(f);
					InputStream in = zipFile.getInputStream(zipEntry);

					FileWriter fileWriter = new FileWriter(f, StandardCharsets.UTF_8);
					BufferedWriter bw = new BufferedWriter(fileWriter);

					FileOutputStream out = new FileOutputStream(f);
					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					out.close();
					in.close();
				}
				flag = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	public static void fileToZip(String baseDirPath, File file, ZipOutputStream out) throws IOException {
		//
		FileInputStream in;
		org.apache.tools.zip.ZipEntry entry;
		// 创建复制缓冲区 1024*4 = 4K
		byte[] buffer = new byte[1024 * 4];
		int bytes_read = 0;
		if (file.isFile()) {
			in = new FileInputStream(file);
			// 根据 parent 路径名字符串和 child 路径名字符串创建一个新 File 实例
			String zipFileName = new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
			entry = new org.apache.tools.zip.ZipEntry(zipFileName);
			// "压缩文件" 对象加入 "要压缩的文件" 对象
			out.putNextEntry(entry);
			// 现在是把 "要压缩的文件" 对象中的内容写入到 "压缩文件" 对象
			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			out.closeEntry();
			in.close();
		}
	}

	/**
	 * 多个文件目录压缩到Zip 输出流
	 */
	public static void filesToZip(String baseDirPath, File[] files, ZipOutputStream out) throws IOException {
		// 遍历所有的文件 一个一个地压缩
		for (File file : files) {
			// 调用本类的一个静态方法 压缩一个文件
			QRZipFileUtils.fileToZip(baseDirPath, file, out);
		}
	}
}
