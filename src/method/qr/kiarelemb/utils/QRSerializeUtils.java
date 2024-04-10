package method.qr.kiarelemb.utils;

import java.io.*;

/**
 * @author Kiarelemb
 * @projectName QR_Method
 * @className QRSerializeUtils
 * @description TODO
 * @create 2024/4/10 22:12
 */
public class QRSerializeUtils {
    /**
     * 序列化
     *
     * @param binPath 文件路径
     * @param obj     要序列化的对象
     * @throws IOException 可能抛出的异常
     */
    public static void writeObject(String binPath, Object obj) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(binPath);
        ObjectOutputStream ops = new ObjectOutputStream(outputStream);
        ops.writeObject(obj);
        ops.flush();
        ops.close();
        outputStream.close();
    }

    /**
     * 反序列化
     *
     * @param binPath 文件路径
     * @return 反序列化的对象
     * @throws Exception 可能抛出的异常
     */
    public static Object readObject(String binPath) throws Exception {
        FileInputStream inputStream = new FileInputStream(binPath);
        ObjectInputStream in = new ObjectInputStream(inputStream);
        Object obj = in.readObject();
        in.close();
        inputStream.close();
        return obj;
    }
}