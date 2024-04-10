package method.qr.kiarelemb.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiarelemb QR
 * @date 2021/11/14 11:04
 * @apiNote 一个文件去重的工具类
 */
public class QRFileRepeatUtil {
    private final Map<String, Long> files;

    public QRFileRepeatUtil() {
        files = new HashMap<>();
    }

    public void putFileBeforeCheckRepeat(String filePath) {
        if (!isRepeatFile(filePath)) {
            putFile(filePath);
        }
    }

    public void putFile(String filePath) {
        files.put(filePath, QRFileUtils.getFileSize(filePath));
    }

    public boolean isRepeatFile(String filePath) {
        final Long fileBytes = files.get(filePath);
        if (fileBytes == null) {
            return false;
        }
        final long fileSize = QRFileUtils.getFileSize(filePath);
        return fileBytes == fileSize;
    }

    public Map<String, Long> files() {
        return files;
    }
}