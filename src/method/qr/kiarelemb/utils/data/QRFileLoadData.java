package method.qr.kiarelemb.utils.data;

import java.io.File;
import java.io.Serializable;

/**
 * @author Kiarelemb
 * @projectName QR_Method
 * @className QRFileLoadData
 * @description 反序列化时，如果源文件被改动，将重新读取
 * @create 2024/4/16 6:49
 */
public interface QRFileLoadData extends Serializable {
	Object action(File file);
}