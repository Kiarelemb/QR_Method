package method.qr.kiarelemb.utils;

import info.monitorenter.cpdetector.io.*;
import method.qr.kiarelemb.utils.data.QRFileLoadData;
import method.qr.kiarelemb.utils.data.QRStringRandomData;
import method.qr.kiarelemb.utils.data.QRTextSendData;
import method.qr.kiarelemb.utils.inter.QRDirLoopSingleFileAction;
import method.qr.kiarelemb.utils.inter.QRFileReaderLineAction;
import method.qr.kiarelemb.utils.inter.QRFileReaderLineSplitAction;
import org.rr.mobi4java.MobiDocument;
import org.rr.mobi4java.MobiReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:06
 * @apiNote 文件操作工具类
 */
public class QRFileUtils {
    /**
     * 识别成功率为94.6%
     * <p>对于54905个文本文件，其部分识别结果如下：
     * <p>GB2312 = 37422
     * <p>GB18030 = 7284
     * <p>UTF-8 = 6515
     * <p>UTF-16LE = 408
     * <p>UTF-16BE = 98
     * <p>UTF-32LE = 216
     */
    private static final Set<String> FILE_CODES;

    static {
        FILE_CODES = new HashSet<>();
        FILE_CODES.add("GB2312");
        FILE_CODES.add("GB18030");
        FILE_CODES.add("UTF-8");
        FILE_CODES.add("UTF-16LE");
        FILE_CODES.add("UTF-16BE");
        FILE_CODES.add("US-ASCII");
    }

    public static void fileReader(String filePath, QRFileReaderLineAction action) {
        try {
            final FileReader reader = new FileReader(filePath);
            BufferedReader in = new BufferedReader(reader);
            String lineText;
            while ((lineText = in.readLine()) != null) {
                action.lineText(lineText);
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void fileReader(String filePath, String split, QRFileReaderLineSplitAction action) {
        try {
            final FileReader reader = new FileReader(filePath);
            BufferedReader in = new BufferedReader(reader);
            String lineText;
            while ((lineText = in.readLine()) != null) {
                action.lineText(lineText, lineText.split(split));
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static LinkedList<String> fileReader(File file, Charset charset) {
        LinkedList<String> arr = new LinkedList<>();
        try {
            final FileReader reader = new FileReader(file, charset);
            BufferedReader in = new BufferedReader(reader);
            String lineText;
            while ((lineText = in.readLine()) != null) {
                arr.add(lineText);
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return arr;
    }

    public static LinkedList<String> fileReader(String filePath, Charset charset) {
        return fileReader(new File(filePath), charset);
    }

    public static LinkedList<String> fileReaderWithUtf8(File file) {
        LinkedList<String> arr = new LinkedList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            String lineText;
            while ((lineText = in.readLine()) != null) {
                arr.add(lineText);
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return arr;
    }

    public static LinkedList<String> fileReaderWithUtf8(String filePath) {
        return fileReaderWithUtf8(new File(filePath));
    }

    /**
     * 单次读取全部内容至一个字符串中
     *
     * @param filePath 文件路径
     * @return 所有内容
     */
    public static String fileReaderWithUtf8All(String filePath) {
        return QRStringUtils.join(fileReaderWithUtf8(filePath), "\n");
    }

    /**
     * 单次读取全部内容至一个字符串中
     *
     * @param file 文件
     * @return 所有内容
     */
    public static String fileReaderWithUtf8All(File file) {
        return QRStringUtils.join(fileReaderWithUtf8(file), "\n");
    }

    public static void fileReaderWithUtf8(String filePath, QRFileReaderLineAction action) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            String lineText;
            while ((lineText = in.readLine()) != null) {
                action.lineText(lineText);
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void fileReaderWithUtf8(String filePath, String split, QRFileReaderLineSplitAction action) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            String lineText;
            while ((lineText = in.readLine()) != null) {
                action.lineText(lineText, lineText.split(split));
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static ArrayList<String> fileReaderFromLine100WithUTF8(String filePath) {
        ArrayList<String> arr = new ArrayList<>(100);
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            String lineText;
            int line = 0;
            while ((lineText = in.readLine()) != null) {
                if (++line > 100 && line <= 200) {
                    arr.add(lineText.trim());
                }
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return arr;
    }

    public static void fileWriterWithUTF8(String filePath, String[] texts) {
        if (texts.length > 0) {
            fileWriterWithUTF8(filePath, QRStringUtils.join(texts, System.lineSeparator()));
        } else {
            fileWriterWithUTF8(filePath, "");
        }
    }

    public static void fileWriterWithUTF8(String filePath, List<String> texts) {
        if (texts.size() > 0) {
            fileWriterWithUTF8(filePath, QRStringUtils.join(texts, System.lineSeparator()));
        } else {
            fileWriterWithUTF8(filePath, "");
        }
    }

    public static void fileWriterWithUTF8(String filePath, Set<String> texts) {
        if (texts.size() > 0) {
            fileWriterWithUTF8(filePath, QRStringUtils.join(texts, System.lineSeparator()));
        } else {
            fileWriterWithUTF8(filePath, "");
        }
    }

    public static void fileWriterWithUTF8(String filePath, String text) {
        fileWriter(filePath, text, StandardCharsets.UTF_8);
    }

    public static void fileWriter(String filePath, String[] texts) {
        if (texts.length > 0) {
            fileWriter(filePath, QRStringUtils.join(texts, System.lineSeparator()));
        } else {
            fileWriter(filePath, "");
        }
    }

    public static void fileWriter(String filePath, List<String> texts, Charset charset) {
        if (texts.size() > 0) {
            fileWriter(filePath, QRStringUtils.join(texts, System.lineSeparator()));
        } else {
            fileWriter(filePath, "");
        }
    }

    public static void fileWriter(File file, List<String> texts, Charset charset) {
        if (texts.size() > 0) {
            fileWriter(file, QRStringUtils.join(texts, System.lineSeparator()), charset);
        } else {
            fileWriter(file, "", charset);
        }
    }

    /**
     * 如果文件存在，会先删除
     */
    public static void fileWriter(File file, String[] texts, Charset charset) {
        fileWriter(file, QRStringUtils.join(texts, System.lineSeparator()), charset);
    }


    /**
     * 如果文件存在，会先删除
     */
    public static void fileWriter(String filePath, String[] texts, Charset charset) {
        fileWriter(filePath, QRStringUtils.join(texts, System.lineSeparator()), charset);
    }

    /**
     * 如果文件存在，会先删除
     */
    public static void fileWriter(String filePath, String text, Charset charset) {
        fileWriter(new File(filePath), text, charset);
    }

    public static void fileWriter(File file, String text, Charset charset) {
        try {
            fileDelete(file);
            FileWriter fileWriter = new FileWriter(file, charset);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fileWriter(String filePath, String text) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在文件后换行添加内容
     *
     * @param filePath 文件路径
     * @param text     添加内容
     */
    public static void fileWriterAppend(String filePath, String text) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在文件后添加内容并换行（先添加再换行）
     *
     * @param filePath 文件路径
     * @param text     添加内容
     */
    public static void fileWriterAppendFollowedANewLine(String filePath, String text) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(text);
            bw.write(System.lineSeparator());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在文件后添加多行内容并换行（先添加再换行）
     *
     * @param filePath 文件路径
     * @param texts    添加内容
     */
    public static void fileWriterAppendFollowedANewLine(String filePath, String[] texts) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            for (String text : texts) {
                bw.write(text);
                bw.write(System.lineSeparator());
                bw.flush();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在文件后添加内容并换行（先换行再添加）
     *
     * @param filePath 文件路径
     * @param text     添加内容
     */
    public static void fileWriterAppendFollowingANewLine(String filePath, String text) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(System.lineSeparator());
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在文件后添加多行内容并换行（先换行再添加）
     *
     * @param filePath 文件路径
     * @param texts    添加内容
     */
    public static void fileWriterAppendFollowingANewLine(String filePath, String[] texts) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            for (String text : texts) {
                bw.write(System.lineSeparator());
                bw.write(text);
                bw.flush();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileSave(File f, String[] texts) {
        fileWriter(f.getAbsolutePath(), texts);
    }

    public static void fileSaveWithUTF8(File f, String[] texts) {
        fileWriterWithUTF8(f.getAbsolutePath(), texts);
    }

    public static boolean fileCreate(String propPath) {
        return fileCreate(new File(new String(propPath.getBytes(StandardCharsets.UTF_8))));
    }

    /**
     * 文件复制
     *
     * @param origin 源文件
     * @param target 目的文件
     * @return 是否成功
     */
    public static Path fileCopy(String origin, String target) throws IOException {
        File originFile = new File(origin);
        File targetFile = new File(target);
        fileCreate(targetFile);
        return Files.copy(originFile.toPath(), targetFile.toPath());
    }

    /**
     * 文件复制
     *
     * @param from 源文件
     * @param dir  目的文件夹
     * @return 是否成功
     */
    public static Path fileCopy(File from, String dir) throws IOException {
        if (!dir.endsWith(File.separator)) {
            dir = dir.concat(File.separator);
        }
        return Files.copy(from.toPath(), new File(dir + from.getName()).toPath());
    }

    public static boolean dirCreate(String propPath) {
        return dirCreate(new File(propPath));
    }

    public static boolean dirCreate(File dir) {
        if (dir.exists()) {
            return true;
        }
        if (dir.mkdir()) {
            return true;
        }
        if (dir.mkdirs()) {
            return true;
        }
        LinkedList<File> dirs = new LinkedList<>();
        File parent = dir.getParentFile();
        while (parent != null && !parent.exists()) {
            dirs.add(0, parent);
            parent = parent.getParentFile();
        }
        if (dirs.size() != 0) {
            for (File file : dirs) {
                if (!file.mkdir()) {
                    return false;
                }
            }
        }
        return dir.mkdir();
    }

    /**
     * 创建文件，如果其目录不存在，也会创建其上级目录。如果文件存在，则不会创建
     *
     * @param file 文件对象
     * @return 是否创建成功
     */
    public static boolean fileCreate(File file) {
        if (file.exists()) {
            return true;
        }
        File parent = file.getParentFile().getAbsoluteFile();
        try {
            if (!parent.exists()) {
                if (!dirCreate(parent)) {
                    return false;
                }
            }
            if (!file.getAbsoluteFile().exists()) {
                return file.getAbsoluteFile().createNewFile();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 压缩文件
     * 如：Tools.zipFile("/home/kiarelemb/", "心的出口.ini", "心的出口.txt");
     */
    public static void zipFile(String parentDir, String... names) {
        QRZipFileUtils.zipFile(parentDir, names);
    }

    /**
     * 解压文件
     * 如：Tools.unZipFile("/home/kiarelemb/心的出口.kmb");
     */
    public static void unZipFile(String zipFilePath) {
        String name = zipFilePath.substring(0, zipFilePath.lastIndexOf('.'));
        QRZipFileUtils.unZip(zipFilePath, name);
    }

    /**
     * 解压文件到指定目录
     */
    public static boolean unZipFile(String zipFilePath, String unZipDirPath) {
        return QRZipFileUtils.unZip(zipFilePath, unZipDirPath);
    }

    public static boolean unEpubFile(String epubFilePath, String unEpubDirPath) {
        return QRZipFileUtils.unZipEpubFile(epubFilePath, unEpubDirPath);
    }

    /**
     * <p>将读取epub文件的内容输出到该文件的同目录同名的txt文件</p>
     * <p>使用示例：</p>
     * <code>QRFileUtils.epubFileToTextFile("D:\\小说\\书.epub");</code>
     *
     * @param epubFilePath epub文件绝对路径
     */
    public static String epubFileToTextFile(String epubFilePath) throws Exception {
        File f = new File(epubFilePath);
        return epubFileToTextFile(epubFilePath, f.getParentFile().getAbsolutePath());
    }

    /**
     * <p>使用示例：</p>
     * <code>QRFileUtils.epubFileToTextFile("D:\\小说\\书.epub", "D:\\小说\\testFiles");</code>
     *
     * @param epubFilePath  epub文件绝对路径
     * @param outputDirPath 末尾不带文件分隔符的目录路径
     */
    public static String epubFileToTextFile(String epubFilePath, String outputDirPath) throws Exception {
        String dirPath = getTempDirectoryPath() + "epub_tmp" + QRRandomUtils.getRandomNum();
        if (unEpubFile(epubFilePath, dirPath)) {
            String outputTextFilePath = outputDirPath + File.separator + getFileName(epubFilePath) + ".txt";
            final LinkedList<String> dirLoopFiles = getDirLoopFiles(dirPath);
            FileOutputStream fos = new FileOutputStream(outputTextFilePath, false);
            OutputStreamWriter otw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            for (String file : dirLoopFiles) {
                final Charset fileCode = getFileCode(file);
                if (fileCode != null) {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    BufferedReader in = new BufferedReader(isr);
                    String lineTextTmp;
                    boolean englishChecked = false;
                    boolean isEnglish = false;
                    final boolean singleLine = fileIsSingleLine(file, fileCode);
                    while ((lineTextTmp = in.readLine()) != null) {
                        String lineText = lineTextTmp.trim();
                        if (lineText.isEmpty()) {
                            continue;
                        }
                        final String text;
                        if (!singleLine) {
                            if (lineText.length() < 9 || !lineText.startsWith("<p") || !lineText.endsWith("</p>")) {
                                continue;
                            }
                            final int startIndex = lineText.indexOf('>') + 1;
                            final int lastIndex = lineText.lastIndexOf('<');
                            if (startIndex > lastIndex || startIndex == 0) {
                                continue;
                            }
                            if (!lineText.startsWith("<p>") && !lineText.contains("\"content\">")) {
                                continue;
                            }
                            text = QRStringUtils.htmlLineProcess(lineText).replaceAll("第[0-9]+页", "");
                        } else {
                            text = QRStringUtils.htmlMarkProcess(lineText);
                        }
                        if (text.isEmpty()) {
                            continue;
                        }
                        if (!englishChecked) {
                            englishChecked = true;
                            isEnglish = QRStringUtils.isEnglish(text);
                        }
                        //转为简体
                        if (isEnglish) {
                            otw.write(QRTextWash.lightWashForEnglish(text));
                        } else {
                            otw.write(QRTextWash.lightWashForChinese(text));
                        }
                        otw.flush();
                    }
                    in.close();
                } else {
                    throw new UnknownFormatConversionException("文件编码识别失败！");
                }
            }
            dirDelete(dirPath);
            return outputTextFilePath;
        }
        throw new UnknownFormatConversionException("文件读取失败！");
    }

    /**
     * <p>将读取mobi文件的内容输出到该文件的同目录同名的txt文件</p>
     * <p>使用示例：</p>
     * <code>QRFileUtils.mobiFileToTextFile("D:\\小说\\书.mobi");</code>
     *
     * @param mobiFilePath mobi文件绝对路径
     */
    public static String mobiFileToTextFile(String mobiFilePath) throws Exception {
        return mobiFileToTextFile(mobiFilePath, new File(mobiFilePath).getParentFile().getAbsolutePath());
    }

    /**
     * <p>将读取mobi文件的内容输出到该文件的同目录同名的txt文件</p>
     * <p>使用示例：</p>
     * <code>QRFileUtils.mobiFileToTextFile("D:\\小说\\书.mobi");</code>
     *
     * @param mobiFile mobi文件
     */
    public static String mobiFileToTextFile(File mobiFile) throws Exception {
        return mobiFileToTextFile(mobiFile.getAbsolutePath(), mobiFile.getParentFile().getAbsolutePath());
    }

    /**
     * <p>使用示例：</p>
     * <code>QRFileUtils.mobiFileToTextFile("D:\\小说\\书.mobi", "D:\\小说\\testFiles");</code>
     *
     * @param mobiFilePath  mobi文件绝对路径
     * @param outputDirPath 末尾不带文件分隔符的目录路径
     */
    public static String mobiFileToTextFile(String mobiFilePath, String outputDirPath) throws Exception {
        MobiDocument mobiDoc = new MobiReader().read(new File(mobiFilePath));
        final String textContent = mobiDoc.getTextContent();
        final boolean isEnglish = QRStringUtils.isEnglish(textContent);
        final String textWashed;
        if (isEnglish) {
            textWashed = QRTextWash.lightWashForEnglish(textContent).replaceAll("([ 0-9]+Chapter)+", "");
        } else {
            textWashed = QRTextWash.lightWashForChinese(textContent);
        }
        final String outputFilePath = outputDirPath + File.separator + getFileName(mobiFilePath) + ".txt";
        QRFileUtils.fileWriterWithUTF8(outputFilePath, textWashed);
        return outputFilePath;
    }

    public static LinkedList<String> getDirLoopFiles(String dirPath) {
        LinkedList<String> filePaths = new LinkedList<>();
        dirLoop(dirPath, file -> filePaths.add(file.getAbsolutePath()));
        return filePaths;
    }

    /**
     * 取得目录下的所有文件名，其文件名不包含扩展名
     */
    public static LinkedList<String> getDirLoopPourFileName(String dirPath) {
        LinkedList<String> list = new LinkedList<>();
        dirLoop(dirPath, file -> list.add(getFileName(file)));
        return list;
    }

    /**
     * 取第一层下的所有目录
     */
    public static LinkedList<String> getDirNameList(String dirPath) {
        LinkedList<String> filePaths = new LinkedList<>();
        File file = new File(dirPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        filePaths.add(f.getName());
                    }
                }
            }
        }
        return filePaths;
    }

    public static String getDirName(String filePath) {
        return getDirName(new File(filePath));
    }

    public static String getDirName(File file) {
        String name = QRFileUtils.getFileName(file);
        String absolutePath = file.getAbsolutePath();
        int index = absolutePath.lastIndexOf(name);
        int last = index - 1;
        int start = absolutePath.substring(0, last).lastIndexOf("\\") + 1;
        return absolutePath.substring(start, last);
    }

    public static boolean dirDelete(String path, boolean selfDelete) {
        if (path == null) {
            return false;
        }
        File f = new File(path);
        if (f.isDirectory()) {
            String[] children = f.list();
            if (children == null) {
                return false;
            }
            final String absolutePath = f.getAbsolutePath();
            for (String child : children) {
                boolean success = dirDelete(absolutePath + File.separator + child);
                if (!success) {
                    return false;
                }
            }
        }
        if (selfDelete) {
            return f.delete();
        }
        return true;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     */
    public static boolean dirDelete(String path) {
        return dirDelete(path, true);
    }

    /**
     * QR原创，文件的随机读取
     * Linux下测试可行
     */
    public static String fileReaderByRandomAccessWithUTF8(String filePath, int startIndex, int length) {
        try {
            RandomAccessFile ra = new RandomAccessFile(filePath, "r");
            final String s = fileReaderByRandomAccessWithUTF8(ra, startIndex, length);
            ra.close();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String fileReaderByRandomAccessWithUTF8(RandomAccessFile ra, int startIndex, int length) {
        return fileReaderByRandomAccessWithUTF8StartWithLong(ra, startIndex, length);
    }

    public static String fileReaderByRandomAccessWithUTF8StartWithLong(RandomAccessFile ra, long startIndex,
                                                                       int length) {
        try {
            int power = 3;
            ra.seek(startIndex);
            byte[] b = new byte[length * power];
            int read = ra.read(b);
            int endIndex = 0;
            if (read == -1) {
                for (int i = b.length - 1; i >= 0; i--) {
                    if (b[i] == 0) {
                        endIndex = i;
                    } else {
                        break;
                    }
                }
                b = Arrays.copyOfRange(b, 0, endIndex);
            }
            return new String(b, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String fileReaderByRandomAccessWithUTF8StartWithLong(String filePath, long startIndex, int length) {
        RandomAccessFile ra;
        try {
            ra = new RandomAccessFile(filePath, "r");
            String str = fileReaderByRandomAccessWithUTF8StartWithLong(ra, startIndex, length);
            ra.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 按句读取文件
     */
    public static QRTextSendData fileReaderByRandomAccessEndWithJuHao(String filePath, long startIndex, int len) {
        int diff = 10;
        int length0 = len + diff;
        try {
            RandomAccessFile ra = new RandomAccessFile(filePath, "r");
            ra.seek(startIndex);
            String text = fileReaderByRandomAccessWithUTF8StartWithLong(filePath, startIndex, length0);
            if (text.isEmpty()) {
                return null;
            }
            String typeText;
            int finalLen;
            while (true) {
                int endMarkIndex1 = text.indexOf("。");
                int endMarkIndex2 = text.indexOf("？");
                int endMarkIndex3 = text.indexOf("！");
                int[] i = {endMarkIndex1, endMarkIndex2, endMarkIndex3};
                ArrayList<Integer> ai =
                        Arrays.stream(i).filter(ii -> ii != -1).boxed().collect(Collectors.toCollection(ArrayList::new));
                if (ai.size() == 0) {
                    length0 *= 2;
                    text = fileReaderByRandomAccessWithUTF8StartWithLong(filePath, startIndex, length0);
                    if (text.isEmpty()) {
                        return null;
                    }
                } else {
                    finalLen = ai.get(0);
                    for (Integer ii : ai) {
                        if (ii < length0) {
                            finalLen = ii;
                        }
                    }
                    break;
                }
            }
            final int i = text.indexOf("”");
            if (i == ++finalLen) {
                ++finalLen;
            }
            typeText = text.substring(0, finalLen);
            int typeTextByteLen = typeText.getBytes(StandardCharsets.UTF_8).length;
            int endIndex = (int) startIndex + typeTextByteLen;
            return new QRTextSendData(typeText, endIndex, false, typeTextByteLen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件随机向前读取
     *
     * @param filePath      文件路径
     * @param startIndex    上段开始位置，需要先减去上段调用后的字节长度
     * @param len           长度
     * @param textEndInMark 是否以标点切断
     * @return 数据类
     */
    public static QRTextSendData fileForeReaderByRandomAccessMarkPositionFind(String filePath, long startIndex,
                                                                              final int len,
                                                                              final boolean textEndInMark) {
        int diff = 10;
        final int length0 = len + diff;
        long startIndex0 = startIndex - length0 * 3L;
        int cnLen = length0 * 3;
        if (startIndex0 < 0) {
            cnLen += (int) startIndex0;
            startIndex0 = 0;
        }
        try {
            RandomAccessFile ra = new RandomAccessFile(filePath, "r");
            ra.seek(startIndex0);

            byte[] b = new byte[cnLen];
            ra.read(b, 0, b.length);
//			String text = fileReaderByRandomAccessWithUTF8StartWithLong(filePath, startIndex0, length0);
            String text = new String(b, StandardCharsets.UTF_8);
            if (text.isEmpty()) {
                return null;
            }
            int finalStartIndex;
            String typeText;
            boolean isEnglish = QRStringUtils.isEnglish(text);
            if (!isEnglish) {
                if (!textEndInMark) {
                    finalStartIndex = text.length() - len;
                } else {
                    //region search
                    int endMarkIndex = getBestForeIndex(text, '，', len);
                    int endMarkIndex1 = getBestForeIndex(text, '。', len);
                    int endMarkIndex2 = getBestForeIndex(text, '？', len);
                    int endMarkIndex3 = getBestForeIndex(text, '！', len);
                    int endMarkIndex5 = getBestForeIndex(text, '”', len);
                    int endMarkIndex4 = getBestForeIndex(text, "……", len);
                    int endMarkIndex6 = getBestForeIndex(text, "——", len);
                    int endMarkIndex7 = getBestForeIndex(text, '：', len);
                    int endMarkIndex8 = getBestForeIndex(text, '；', len);
                    //endregion
                    int[] indexes = new int[]{endMarkIndex, endMarkIndex1, endMarkIndex2, endMarkIndex3, endMarkIndex4
                            , endMarkIndex6, endMarkIndex7, endMarkIndex8};
                    //如果都没有找到
                    if (Arrays.stream(indexes).sum() == -1 * indexes.length) {
                        finalStartIndex = text.length() - len;
                    } else {
                        //找到不等于-1的最小值
                        //int i = Math.max
                        int endFuHaoIndex = cnLen;
                        int minDiff = len;
                        for (final int index : indexes) {
                            if (index != -1) {
                                final int abs = Math.abs(index - len + 1);
                                if (abs < minDiff) {
                                    minDiff = abs;
                                    endFuHaoIndex = index;
                                }
                            }
                        }
                        if (endFuHaoIndex < endMarkIndex5) {
                            final int i = text.indexOf('“', endMarkIndex5);
                            final int extent = 5;
                            if (Math.abs(i - len) < extent || i > len) {
                                endFuHaoIndex = i - 1;
                            } else {
                                endFuHaoIndex = endMarkIndex5;
                            }
                        }
                        //开始位置是固定为零
                        finalStartIndex =
                                endFuHaoIndex + (endFuHaoIndex == endMarkIndex4 || endFuHaoIndex == endMarkIndex6 ? 2
                                        : 1);
                    }
                }
            } else {
                //最后一个单词可能是乱码的，所以要减掉一个
                while (true) {
                    String preStr;
                    final int count = QRStringUtils.englishWordsCount(text) - 1;
                    if (count < len) {
                        preStr = text;
                        text = fileReaderByRandomAccessWithUTF8StartWithLong(ra, startIndex0, length0 * 2);
                        //如果已经读到了尽头
                        if (text.equals(preStr)) {
                            finalStartIndex = text.length();
                            break;
                        }
                    } else {
                        char c = QRStringUtils.A_WHITE_SPACE_CHAR;
                        final QRStringRandomData data = QRTimeUtils.cutAtTimes(text, c, len);
                        finalStartIndex = data.num();
                        text = data.text();
                        break;
                    }
                }
            }
            typeText = text.substring(finalStartIndex);
            int typeTextByteLen = typeText.getBytes(StandardCharsets.UTF_8).length;
            long endIndex = startIndex - typeTextByteLen;
            return new QRTextSendData(typeText, endIndex, isEnglish, typeTextByteLen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机文字读取，并切取至标点结束
     */
    public static QRTextSendData fileReaderByRandomAccessMarkPositionFind(String filePath, long startIndex,
                                                                          final int len, final boolean textEndInMark) {
        int diff = 10;
        int length0 = len + diff;
        try {
            RandomAccessFile ra = new RandomAccessFile(filePath, "r");
            ra.seek(startIndex);
            String text = fileReaderByRandomAccessWithUTF8StartWithLong(filePath, startIndex, length0);
            if (text.isEmpty()) {
                return null;
            }
            int finalLen;
            String typeText;
            boolean isEnglish = QRStringUtils.isEnglish(text);
            if (!isEnglish) {
                if (textEndInMark) {
                    //region search
                    int endMarkIndex = getBestBackIndex(text, '，', len);
                    int endMarkIndex1 = getBestBackIndex(text, '。', len);
                    int endMarkIndex2 = getBestBackIndex(text, '？', len);
                    int endMarkIndex3 = getBestBackIndex(text, '！', len);
                    int endMarkIndex5 = getBestBackIndex(text, '”', len);
                    int endMarkIndex4 = getBestBackIndex(text, "……", len);
                    int endMarkIndex6 = getBestBackIndex(text, "——", len);
                    int endMarkIndex7 = getBestBackIndex(text, '：', len);
                    int endMarkIndex8 = getBestBackIndex(text, '；', len);
                    //endregion
                    int[] indexes = new int[]{endMarkIndex, endMarkIndex1, endMarkIndex2, endMarkIndex3, endMarkIndex4
                            , endMarkIndex6, endMarkIndex7, endMarkIndex8};
                    //如果都没有找到
                    if (Arrays.stream(indexes).sum() == -1 * indexes.length) {
                        finalLen = len;
                    } else {
                        //找到不等于-1的最小值
                        //int i = Math.max
                        int endFuHaoIndex = length0;
                        int minDiff = len;
                        for (final int index : indexes) {
                            if (index != -1) {
                                final int abs = Math.abs(index - len + 1);
                                if (abs < minDiff) {
                                    minDiff = abs;
                                    endFuHaoIndex = index;
                                }
                            }
                        }
                        if (endFuHaoIndex != -1 && endFuHaoIndex < endMarkIndex5) {
                            final int i = text.lastIndexOf('“', endMarkIndex5);
                            final int extent = 5;
                            if (Math.abs(i - len) < extent || i > len) {
                                endFuHaoIndex = i - 1;
                            } else {
                                endFuHaoIndex = endMarkIndex5;
                            }
                        }
                        //开始位置是固定为零
                        finalLen = endFuHaoIndex + (endFuHaoIndex == endMarkIndex4 || endFuHaoIndex == endMarkIndex6 ?
                                2 : 1);
                    }
                } else {
                    finalLen = len;
                }
            } else {
                //最后一个单词可能是乱码的，所以要减掉一个
                while (true) {
                    String preStr;
                    final int count = QRStringUtils.englishWordsCount(text) - 1;
                    if (count < len) {
                        preStr = text;
                        length0 *= 2;
                        text = fileReaderByRandomAccessWithUTF8StartWithLong(ra, startIndex, length0);
                        //如果已经读到了尽头
                        if (text.equals(preStr)) {
                            finalLen = text.length();
                            break;
                        }
                    } else {
                        char c = QRStringUtils.A_WHITE_SPACE_CHAR;
                        final QRStringRandomData data = QRTimeUtils.cutAtTimes(text, c, len);
                        finalLen = data.num();
                        text = data.text();
                        break;
                    }
                }
            }
            if (text.length() < finalLen) {
                finalLen = text.length();
            }
            typeText = text.substring(0, finalLen);
            int typeTextByteLen = typeText.getBytes(StandardCharsets.UTF_8).length;
            long endIndex = startIndex + typeTextByteLen;
            return new QRTextSendData(typeText, endIndex, isEnglish, typeTextByteLen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getBestForeIndex(final String temp, char searchStr, int len) {
        return getBestForeIndex(temp, searchStr, 0, len);
    }

    public static int getBestForeIndex(final String temp, char searchStr, int startSplitIndex, int len) {
        return getBestForeIndex(temp, String.valueOf(searchStr), startSplitIndex, len);
    }

    public static int getBestForeIndex(final String temp, String searchStr, int len) {
        return getBestForeIndex(temp, searchStr, 0, len);
    }

    public static int getBestForeIndex(final String temp, String searchStr, int startSplitIndex, int len) {
        int searchIndex = Math.max(temp.length() - len, 0);
        return getBestIndex(searchIndex, temp, searchStr, startSplitIndex, len);
    }

    public static int getBestBackIndex(final String temp, char searchStr, int len) {
        return getBestBackIndex(temp, searchStr, 0, len);
    }

    public static int getBestBackIndex(final String temp, char searchStr, int startSplitIndex, int len) {
        return getBestBackIndex(temp, String.valueOf(searchStr), startSplitIndex, len);
    }

    public static int getBestBackIndex(final String temp, String searchStr, int len) {
        return getBestBackIndex(temp, searchStr, 0, len);
    }

    public static int getBestBackIndex(final String temp, String searchStr, int startSplitIndex, int len) {
        int searchIndex = startSplitIndex + len;
        return getBestIndex(searchIndex, temp, searchStr, startSplitIndex, len);
    }

    public static int getBestIndex(final int searchIndex, final String temp, String searchStr, int startSplitIndex,
                                   int len) {
        int fore = temp.lastIndexOf(searchStr, searchIndex);
        int back = temp.indexOf(searchStr, searchIndex);
        final int foreDiff = Math.abs(searchIndex - fore);
        final int edge = 8;
        if (foreDiff > edge) {
            fore = -1;
        }
        final int backDiff = Math.abs(searchIndex - back);
        if (backDiff > edge) {
            back = -1;
        }
        if (fore == -1 && back == -1) {
            return -1;
        }
        return foreDiff <= backDiff ? fore : back;
    }

    /**
     * 检测乱序字符
     *
     * @param str 读取的内容
     * @return 有内容则是有问题
     */
    public static boolean fileFailRead(String str) {
        String strs = QRStringUtils.isCharInRange(str.replaceAll("[\\pP\\pZ\\pNa-zA-Z\\d]", ""),
                QRStringUtils.CHINESE_NORMAL).toString();
        return !strs.trim().isEmpty();
    }

    public static void restFileDelete(String dir, String extension) {
        File f = new File(dir);
        if (f.isDirectory()) {
            String[] children = f.list();
            if (children == null) {
                return;
            }
            for (String child : children) {
                if (child.endsWith(extension)) {
                    new File(f.getAbsolutePath() + File.separator + child).deleteOnExit();
                }
            }
        }
    }

    public static Charset getFileCode(String filePath) {
        return getFileCode(new File(filePath));
    }

    public static Charset getFileCode(File file) {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(UnicodeDetector.getInstance());
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());
        Charset charset = null;
        try {
            charset = detector.detectCodepage(file.toURI().toURL());
            final String name = charset.name();
            if (!FILE_CODES.contains(name)) {
//				System.out.println("filePath = " + filePath);
                return null;
            }
        } catch (Exception ex) {
            QRTools.doNothing(ex);
        }
        return charset;
    }

    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        return fileExists(new File(filePath));
    }

    public static boolean fileExists(File file) {
        return file.exists();
    }

    public static boolean fileDelete(String filePath) {
        return fileDelete(new File(filePath));
    }

    public static boolean fileDelete(File file) {
        try {
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从文件中删除指定行
     *
     * @param filePath   文件路径
     * @param lineNumber 行号，从1开始
     */
    public static void fileLineDeleteWithUtf8(String filePath, int lineNumber) {
        LinkedList<String> arr = new LinkedList<>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            int lineNum = 1;
            String lineText;
            while ((lineText = in.readLine()) != null) {
                if (lineNum++ == lineNumber) {
                    continue;
                }
                arr.add(lineText);
            }
            fileWriterWithUTF8(filePath, arr);
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 从文件中删除指定行内容，内容相同时只删 {@code 1} 次
     *
     * @param filePath 文件路径
     * @param lineText 行内容
     */
    public static void fileLineDeleteWithUtf8(String filePath, String lineText) {
        fileLineDeleteWithUtf8(filePath, lineText, 1);

    }

    /**
     * 从文件中删除指定行内容
     *
     * @param filePath 文件路径
     * @param lineText 行内容
     * @param times    内容相同时删几次，删除该次数后再出现相同的则不会删除。传入 {@code -1} 则全删
     */
    public static void fileLineDeleteWithUtf8(String filePath, String lineText, int times) {
        LinkedList<String> arr = new LinkedList<>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            int time = 0;
            String text;
            while ((text = in.readLine()) != null) {
                if (text.equals(lineText)) {
                    if (time++ < times || times == -1) {
                        continue;
                    }
                }
                arr.add(text);
            }
            fileWriterWithUTF8(filePath, arr);
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 校验文件的算法
     *
     * @param filePath 文件名称及路径
     * @return 返回10位数的校验码
     * @throws IOException 异常
     */
    public static String getCrc32(String filePath) throws IOException {
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(filePath), new CRC32());
        File f = new File(filePath);
        long fileSize = f.length();
        long checksum = 0;
        byte[] b = new byte[512];
        while (cis.read(b) >= 0) {
            checksum += cis.getChecksum().getValue();
        }
        return String.valueOf(checksum + fileSize + getFileNameInt(f.getName()));
    }

    private static long getFileNameInt(String fileName) {
        long returnLong = 0;
        for (char c : fileName.toCharArray()) {
            returnLong += c * 33;
        }
        return returnLong;
    }

    /**
     * 读取跟打文件的总字数
     */
    public static long getFileWordNumWithUTF8(String filePath) {
        try {
            File f = new File(filePath);
            int value = 500;
            String text = fileReaderByRandomAccessWithUTF8(filePath, 0, 200);
            final boolean isEnglish = QRStringUtils.isEnglish(text);
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader read = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferRead = new BufferedReader(read);
            long length = 0;
            char[] b = new char[value];
            int spaceCount = 0;
            while (bufferRead.read(b) != -1) {
                for (int i = 0, bLength = b.length; i < bLength && b[i] != 0; i++) {
                    if (!isEnglish) {
                        length++;
                    } else {
                        if (b[i] == QRStringUtils.A_WHITE_SPACE_CHAR) {
                            spaceCount++;
                        }
                    }
                }
                b = new char[value];
            }
            return isEnglish ? spaceCount + 1 : length;
        } catch (Exception e1) {
            return -1;
        }
    }

    public static boolean fileIsSingleLine(String filePath, Charset fileCode) {
        File f = new File(filePath);
        try {
            int value = 500;
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader read = new InputStreamReader(fis, fileCode);
            BufferedReader bufferRead = new BufferedReader(read);
            char[] b = new char[value];
            while (bufferRead.read(b) != -1) {
                for (int i = 0, bLength = b.length; i < bLength && b[i] != 0; i++) {
                    if (b[i] == QRStringUtils.AN_ENTER_CHAR) {
                        return false;
                    }
                }
                b = new char[value];
            }
            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    public static int getFileLineNumWithUtf8(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return -1;
        }
        try {
            int i = 0;
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            while (in.readLine() != null) {
                i++;
            }
            in.close();
            return i;
        } catch (Exception e1) {
            return -1;
        }
    }

    public static int getFileLineNum(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return -1;
        }
        final Charset fileCode = getFileCode(filePath);
        if (fileCode == null) {
//            System.out.println("文件编码识别失败！");
            return -1;
        }
        try {
            int i = 0;
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, fileCode);
            BufferedReader in = new BufferedReader(isr);
            String lineText;
            while (in.readLine() != null) {
                i++;
            }
            in.close();
            return i;
        } catch (Exception e1) {
            return -1;
        }
    }

    public static String getFileLineTextWithUtf8(String filePath, int line) {
        return getFileLineText(filePath, line, StandardCharsets.UTF_8);
    }

    public static String getFileLineTextWithUtf8(File file, int line) {
        return getFileLineText(file, line, StandardCharsets.UTF_8);
    }


    public static String getFileLineText(String filePath, int line) {
        return getFileLineText(new File(filePath), line);
    }

    public static String getFileLineText(File file, int line) {
        final Charset fileCode = getFileCode(file);
        if (fileCode == null) {
            return "";
        }
        return getFileLineText(file, line, fileCode);
    }

    public static String getFileLineText(String filePath, int line, Charset charset) {
        return getFileLineText(new File(filePath), line, charset);
    }

    /**
     * 取得指定文本文件及指定文件编码获取指定行内容
     *
     * @param file    文件一定存在的路径
     * @param line    行数
     * @param charset 编码
     * @return 该行内容。出错则返回空字符串
     */
    public static String getFileLineText(File file, int line, Charset charset) {
        if (!file.exists()) {
            return "";
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, charset);
            BufferedReader in = new BufferedReader(isr);
            int i = 1;
            String lineText;
            while ((lineText = in.readLine()) != null) {
                if (i++ == line) {
                    return lineText;
                }
            }
            in.close();
        } catch (Exception ignore) {
        }
        return "";
    }

    /**
     * 不包含扩展名
     */
    public static String getFileName(String filePath) {
        return getFileName(new File(filePath));
    }

    /**
     * 不包含扩展名
     */
    public static String getFileName(File f) {
        if (f.exists()) {
            final String name = new String(f.getName().getBytes(StandardCharsets.UTF_8));
            final int lastIndexOf = name.lastIndexOf('.');
            return lastIndexOf != -1 ? name.substring(0, lastIndexOf) : name;
        } else {
            return "";
        }
    }

    public static long getFileSize(String filePath) {
        return new File(filePath).length();
    }

    /**
     * 取得文件扩展名，其返回结果带有点
     *
     * @param filePath 文件路径
     * @return 扩展名
     */
    public static String getFileExtension(String filePath) {
        final int beginIndex = filePath.lastIndexOf('.');
        if (beginIndex == -1) {
            return filePath;
        }
        return filePath.substring(beginIndex);
    }

    /**
     * 取得文件扩展名，其返回结果带有点
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        final int beginIndex = name.lastIndexOf('.');
        if (beginIndex == -1) {
            return name;
        }
        return name.substring(beginIndex);
    }

    /**
     * 返回文件随机读取的最大可读的末位置
     *
     * @param filePath UTF-8文件编码
     */
    public static long getTextFileMaxReadablePosition(String filePath) {
        long startIndex = 0;
        long endIndex = new File(filePath).length() / 2;
        long mid = endIndex / 2;
        String str = "";
        int len = 35;
        while (endIndex - startIndex > len) {
            str = fileReaderByRandomAccessWithUTF8StartWithLong(filePath, mid, len);
            if (str.isEmpty()) {
                endIndex = mid;
            } else {
                startIndex = mid;
            }
            mid = (endIndex - startIndex) / 2 + startIndex;
        }
        return mid + str.getBytes(StandardCharsets.UTF_8).length;
    }

    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getFileMd5(String filePath) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            char[] hexDigits = {'A', '^', '(', 'F', '-', 'E', 'B', '.', '=', '?', '/', '$', 'C', '&', '*', 'D'};
            File f = new File(filePath);
            FileInputStream in = new FileInputStream(f);
            FileChannel ch = in.getChannel();
            MappedByteBuffer map = ch.map(FileChannel.MapMode.READ_ONLY, 0, f.length());
            messagedigest.update(map);
            final byte[] bytes = messagedigest.digest();
            long n = bytes.length;
            StringBuilder sb = new StringBuilder(Math.toIntExact(2 * n));
            for (final byte b : bytes) {
                char c0 = hexDigits[(b & 0xf0) >> 4];
                char c1 = hexDigits[b & 0xf];
                sb.append(c0).append(c1);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFileLastNotBlankLine(String uft8FilePath) {
        try {
            FileInputStream fis = new FileInputStream(uft8FilePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            String lineText;
            String lastLine = "";
            while ((lineText = in.readLine()) != null) {
                if (lineText.isEmpty()) {
                    continue;
                }
                lastLine = lineText;
            }
            in.close();
            return lastLine;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * @param dirPath 目录路径
     * @param action  操作
     */
    public static void dirLoop(String dirPath, QRDirLoopSingleFileAction action) {
        dirLoop(new File(dirPath), action);
    }

    /**
     * @param dir    目录
     * @param action 操作
     */
    public static void dirLoop(File dir, QRDirLoopSingleFileAction action) {
        if (dir.exists()) {
            if (dir.isFile()) {
                action.action(dir);
            } else {
                File[] list = dir.listFiles();
                if (list != null) {
                    for (File value : list) {
                        dirLoop(value, action);
                    }
                }
            }
        }
    }

    /**
     * 重命名文件
     *
     * @param dir     目录路径
     * @param oldName 旧文件名
     * @param newName 新文件名
     * @return 是否成功
     */
    public static boolean renameFile(String dir, String oldName, String newName, boolean oldFileDelete) {
        if (!dir.endsWith(File.separator)) {
            dir = dir.concat(File.separator);
        }
        File newFile = new File(dir + newName);
        if (oldFileDelete) {
            QRFileUtils.fileDelete(newFile);
        }
        return new File(dir + oldName).renameTo(newFile);
    }

    /**
     * 重命名文件
     *
     * @param dir       目录路径
     * @param oldName   旧文件名，不包含扩展名
     * @param newName   新文件名，不包含扩展名
     * @param extension 文件的扩展名，可以不加小数点
     * @return 是否成功
     */
    public static boolean renameFile(String dir, String oldName, String newName, String extension,
                                     boolean oldFileDelete) {
        if (!dir.endsWith(File.separator)) {
            dir = dir.concat(File.separator);
        }
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        File oldFile = new File(dir + oldName + extension);
        File newFile = new File(dir + newName + extension);
        if (oldFileDelete) {
            QRFileUtils.fileDelete(newFile);
        }
        return oldFile.renameTo(newFile);
    }

    /**
     * 为文件重命名其扩展名，如果没有，则默认加上
     *
     * @param filePath      文件路径
     * @param newExtension  新扩展名
     * @param oldFileDelete 是不是删除源文件
     * @return 是否成功
     */
    public static boolean renameExtension(String filePath, String newExtension, boolean oldFileDelete) {
        File file = new File(filePath);
        if (!newExtension.startsWith(".")) {
            newExtension = "." + newExtension;
        }
        String newFilePath;

        int i = filePath.lastIndexOf(".");
        if (file.getName().contains(".")) {
            newFilePath = filePath.substring(0, i) + newExtension;
        } else {
            newFilePath = filePath + newExtension;
        }
        return renameFile(filePath, newFilePath, oldFileDelete);
    }

    /**
     * 重命名文件
     *
     * @param oldFilePath 旧文件路径及名字
     * @param newFilePath 新文件路径及名字
     * @return 是否成功
     */
    public static boolean renameFile(String oldFilePath, String newFilePath) {
        File des = new File(newFilePath);
        if (QRFileUtils.dirCreate(des.getParentFile())) {
            return new File(oldFilePath).renameTo(des);
        }
        return false;
    }

    public static boolean renameFile(String oldFilePath, String newFilePath, boolean oldFileDelete) {
        File des = new File(newFilePath);
        boolean success = false;
        if (des.getParentFile().mkdirs()) {
            success = new File(oldFilePath).renameTo(des);
            if (success && oldFileDelete) {
                QRFileUtils.fileDelete(oldFilePath);
            }
        }
        return success;
    }

    public static void fileDownload(String url, String saveDir, String fileName) throws IOException {
        byte[] buff = new byte[8192];
        InputStream is = new URL(url).openStream();
        File file = new File(saveDir, fileName);
        dirCreate(file.getParentFile());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        int count;
        while ((count = is.read(buff)) != -1) {
            bos.write(buff, 0, count);
        }
        is.close();
        bos.close();
    }

    /**
     * 从网上下载文件，摘自 FileUtils
     *
     * @param urlString 网址
     * @param saveDir   目录
     * @param fileName  文件名
     */
    public static long fileDownloadWithCommons(String urlString, String saveDir, String fileName) throws IOException {
        File file = new File(saveDir, fileName);
        URL url = new URL(urlString);

        final InputStream stream = url.openConnection().getInputStream();
        fileCreate(file);
        OutputStream out = new FileOutputStream(file, false);
        long count = 0;
        int n;
        byte[] buffer = new byte[8192];
        while (-1 != (n = stream.read(buffer))) {
            out.write(buffer, 0, n);
            count += n;
        }
        out.close();
        stream.close();
        return count;
    }

    /**
     * 从网上下载文件
     *
     * @param urlString 网址
     * @param filePath  文件路径
     */
    public static long fileDownloadWithCommons(String urlString, String filePath) throws IOException {
        File file = new File(filePath);
        URL url = new URL(urlString);

        final InputStream stream = url.openConnection().getInputStream();
        fileCreate(file);
        OutputStream out = new FileOutputStream(file, false);
        long count = 0;
        int n;
        byte[] buffer = new byte[8192];
        while (-1 != (n = stream.read(buffer))) {
            out.write(buffer, 0, n);
            count += n;
        }
        out.close();
        stream.close();
        return count;
    }

    public static File fileSelect(Window parent, String fileType, String extension) {
        return fileSelect(parent, fileType, new String[]{extension});
    }

    public static File fileSelect(Window parent, String fileType, String... extension) {
        String currentDirectoryPath = System.getProperty("user.home");
        JFileChooser jfc = new JFileChooser(currentDirectoryPath);
        try {
            jfc.setDragEnabled(true);
        } catch (Exception ignore) {
        }
        StringBuilder extent = new StringBuilder(extension[0]);
        if (extension.length > 1) {
            extent.append(", .");
            for (int i = 1, extensionLength = extension.length; i < extensionLength; i++) {
                extent.append(extension[i]).append(i != extensionLength - 1 ? ", ." : "");
            }
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileType + "(." + extent + ")", extension);
        jfc.setFileFilter(filter);
        int returnVal = jfc.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        }
        return null;
    }

    public static void fileSelect(Window parent, String fileType, QRFileLoadData fileSelectedAction, String extension) {
        fileSelect(parent, fileType, fileSelectedAction, new String[]{extension});
    }

    public static void fileSelect(Window parent, String fileType, QRFileLoadData fileSelectedAction, String... extension) {
        File file = fileSelect(parent, fileType, extension);
        if (file != null) {
            fileSelectedAction.action(file);
        }
    }

    /**
     * 文本行内容乱序算法
     *
     * @param filePath 文件路径
     */
    public static void fileLineRandomWithUTF8(String filePath) {
        fileLineRandom(new File(filePath), StandardCharsets.UTF_8);
    }

    /**
     * 文本行内容乱序算法
     *
     * @param file 文件
     */
    public static void fileLineRandomWithUTF8(File file) {
        fileLineRandom(file, StandardCharsets.UTF_8);
    }

    /**
     * 文本行内容乱序算法
     *
     * @param filePath 文件路径
     * @param charset  编码
     */
    public static void fileLineRandom(String filePath, Charset charset) {
        fileLineRandom(new File(filePath), charset);
    }

    /**
     * 文本行内容乱序算法
     *
     * @param file    文件
     * @param charset 编码
     */
    public static void fileLineRandom(File file, Charset charset) {
        LinkedList<String> list = fileReader(file, charset);
        String[] phrase = QRArrayUtils.getRandomPhrase(list);
        fileWriter(file, phrase, charset);
    }
}