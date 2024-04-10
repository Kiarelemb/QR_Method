package method.qr.kiarelemb.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Kiarelemb QR
 */
public class QRTools {

    /**
     * 占位作用
     */
    public static void doNothing() {
    }

    /**
     * 占位作用
     */
    public static void doNothing(Exception e) {
        System.out.print("Do nothing");
        e.printStackTrace();
    }

    /**
     * 对用户输入的小R指令的检查
     */
    public static boolean xiaoRInputCheck(String answer, String models) {
        String model = String.valueOf(answer.charAt(0));
        if (!models.contains(model)) {
            return false;
        }
        String numStr = answer.substring(1);
        if (!QRStringUtils.isNumber(numStr)) {
            return false;
        }
        int num = Integer.parseInt(numStr);
        return num >= 10 && num <= 1000;
    }

}