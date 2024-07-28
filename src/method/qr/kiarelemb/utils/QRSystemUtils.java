package method.qr.kiarelemb.utils;

import com.sun.jna.Platform;
import com.sun.jna.platform.WindowUtils;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Kiarelemb QR
 * @date 2021/11/10 21:48
 * @apiNote
 */
public class QRSystemUtils {

    public static int[] screenSize;
    public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
    public static final boolean IS_OSX = System.getProperty("os.name").toLowerCase().contains("mac");
    public static final boolean IS_LINUX = System.getProperty("os.name").toLowerCase().contains("linux");

    public static void setSystemLookAndFeel() {
        try {
            //设置外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSystemName() {
        Properties props = System.getProperties();
        String name = props.getProperty("os.name");
        if (name.toLowerCase().contains("linux")) {
            LinkedList<String> osRelease = QRFileUtils.fileReader("/etc/os-release", StandardCharsets.UTF_8);
            if (osRelease.isEmpty()) {
                return name;
            }
            String osName = "";
            String osVersion = "";
            for (String items : osRelease) {
                String i = items.toLowerCase();
                //获取Linux系统名称
                if (i.contains("name") && osName.isEmpty()) {
                    osName = items.split("=")[1].replaceAll("\"", "").trim();
                } else if (i.contains("version") || i.contains("version_id")) {
                    if (osVersion.isEmpty()) {
                        if (i.contains("(")) {
                            //去除括号
                            items = items.substring(0, items.indexOf("("));
                        }
                        osVersion = items.split("=")[1].replaceAll("\"", "").trim();
                    } else if(!osName.contains(osVersion)){
                        name = osName + QRStringUtils.A_WHITE_SPACE + osVersion;
                        break;
                    }
                } else if (!osName.isEmpty() && !osVersion.isEmpty()) {
                    name = osName + QRStringUtils.A_WHITE_SPACE + osVersion;
                    break;
                }
            }
            if (osVersion.isEmpty() && !osName.isEmpty()) {
                return osName;
            }
        }
        return name;
    }

    public static String getSysClipboardText() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = clipboard.getContents(null);
        if (trans != null) {
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    return (String) trans.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 这个在Windows上可用，获取Windows电脑的Mac地址
     */
    public static String getLocalMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            //TODO

//            return "00-00-00-00-00-00";
            return null;
        }
    }

    public static List<ProcessInfo> getSystemProcessInfo() {
        return JProcesses.get().fastMode().listProcesses();
    }

    /**
     * 将传入的内容放入剪贴板
     */
    public static void putTextToClipboard(String message) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable text = new StringSelection(message);
        clip.setContents(text, null);
    }

    /**
     * 判断网络是否连接
     */
    public static boolean onlineConnected() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Boolean> submit = service.submit(() -> {
            URL url;
            try {
                url = new URL("http://www.baidu.com/");
                InputStream in = url.openStream();
                in.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        });
        for (int i = 0; i < 200; i++) {
            QRSleepUtils.sleep(20);
            try {
                if (submit.isDone()) {
                    return submit.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        submit.cancel(true);
        return false;
    }

    public static String getCpuSerial() {
        StringBuilder result = new StringBuilder();
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                         + "Set colItems = objWMIService.ExecQuery _ \n"
                         + "   (\"Select * from Win32_Processor\") \n"
                         + "For Each objItem in colItems \n"
                         + "    Wscript.Echo objItem.ProcessorId \n"
                         + "    exit for  ' do the first cpu only! \n" + "Next \n";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result.append(line);
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.toString().trim().length() < 1) {
            result = new StringBuilder("无CPU_ID被读取");
        }
        return result.toString().trim();
    }

    public static int[] getScreenSize() {
        if (screenSize == null) {
            screenSize = new int[2];
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            QRSystemUtils.screenSize[0] = screenSize.width;
            QRSystemUtils.screenSize[1] = screenSize.height;
        }
        return screenSize;
    }

    public static Dimension screenSize() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        return screenSize;
    }

    public static String aiSeisann() {
        //00-0C-29-22-1A-C2
        int length = 12;
        StringBuilder sb = new StringBuilder();
        char[] hexes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final int maxNum = hexes.length;
        for (int i = 0; i < length; i++) {
            sb.append(hexes[QRRandomUtils.getRandomInt(maxNum)]);
            if (i % 2 == 1) {
                sb.append('-');
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 默认将窗体透明度从0到1
     *
     * @param window 要渐变显示的窗体
     */
    public static void setWindowShowSlowly(Window window) {
        setWindowShowSlowly(window, 1);
    }

    /**
     * 将窗体透明度从0到{@code endAlpha}
     *
     * @param window   要渐变显示的窗体
     * @param endAlpha 最终窗体的透明度
     */
    public static void setWindowShowSlowly(Window window, float endAlpha) {
        if (Platform.isWindows()) {
            new Thread(() -> {
                float alphaFloat = 0.01f;
                try {
                    WindowUtils.setWindowAlpha(window, 0.01f);
                    window.setVisible(true);
                    WindowUtils.setWindowAlpha(window, 0.01f);
                    final float times = (endAlpha - alphaFloat) * 100;
                    for (int i = 1; i <= times; i++) {
                        QRSleepUtils.sleep(8);
                        alphaFloat += 0.01f;
                        WindowUtils.setWindowAlpha(window, alphaFloat);
                    }
                } catch (Exception ignore) {
                } finally {
                    window.setVisible(true);
                }
            }).start();
        } else {
            window.setVisible(true);
        }
    }

    /**
     * 将窗体透明度从1降到0
     *
     * @param window 要关闭的窗体
     */
    public static void setWindowCloseSlowly(Window window) {
        setWindowCloseSlowly(window, 1f);
    }

    /**
     * 将窗体透明度从指定透明度降到0
     *
     * @param window     要关闭的窗体
     * @param startAlpha 开始的透明度
     */
    public static void setWindowCloseSlowly(Window window, float startAlpha) {
        setWindowCloseSlowly(window, startAlpha, false);
    }

    /**
     * 将窗体透明度从指定透明度降到0
     *
     * @param window     要关闭的窗体
     * @param startAlpha 开始的透明度
     * @param systemExit 是否退出程序
     */
    public static void setWindowCloseSlowly(Window window, float startAlpha, boolean systemExit) {
        if (Platform.isWindows()) {
            new Thread(() -> {
                try {
                    float alphaFloat = startAlpha;
                    float times = 1 / startAlpha;
                    int timeExtent = (int) (8 * (systemExit ? times : 1));
                    while (alphaFloat > 0.01) {
                        QRSleepUtils.sleep(timeExtent);
                        alphaFloat -= 0.015f;
                        WindowUtils.setWindowAlpha(window, alphaFloat);
                    }
                } catch (Exception ignore) {
                } finally {
                    window.setVisible(false);
                    if (systemExit) {
                        System.exit(0);
                    }
                }
            }).start();
        } else {
            window.setVisible(false);
            if (systemExit) {
                System.exit(0);
            }
        }
    }

    public static void windowCheckAndDisposing(Window... windows) {
        for (Window w : windows) {
            if (w != null && w.isVisible()) {
                w.dispose();
            }
        }
    }

    public static void setWindowNotRound(Window w) {
        if (IS_WINDOWS) {
            double SCALE = (double) Toolkit.getDefaultToolkit().getScreenResolution() / 96;
            final RoundRectangle2D.Double mask = new RoundRectangle2D.Double(0.0D, 0.0D, w.getWidth() * SCALE, w.getHeight() * SCALE, 0D, 0D);
            WindowUtils.setWindowMask(w, mask);
        }
    }

    public static void setWindowRound(Window w) {
        if (IS_WINDOWS) {
            final RoundRectangle2D.Double mask;
            double SCALE = (double) Toolkit.getDefaultToolkit().getScreenResolution() / 96;
            mask = new RoundRectangle2D.Double(0.0D, 0.0D, w.getWidth() * SCALE, w.getHeight() * SCALE, 10.0D, 10.0D);
            WindowUtils.setWindowMask(w, mask);
        }
    }

    public static void setWindowRound(Window w, float f) {
        if (IS_WINDOWS) {
            if (f < 0.99) {
                WindowUtils.setWindowAlpha(w, f);
            } else {
                WindowUtils.setWindowAlpha(w, 0.9999f);
            }
            final RoundRectangle2D.Double mask;
            double SCALE = (double) Toolkit.getDefaultToolkit().getScreenResolution() / 96;
            mask = new RoundRectangle2D.Double(0.0D, 0.0D, w.getWidth() * SCALE, w.getHeight() * SCALE, 10.0D, 10.0D);
            WindowUtils.setWindowMask(w, mask);
        }
    }

    public static void setWindowTrans(Window w, float alphaFloat) {
        if (IS_WINDOWS) {
            if (alphaFloat < 0.99) {
                WindowUtils.setWindowAlpha(w, alphaFloat);
            } else {
                WindowUtils.setWindowAlpha(w, 0.999f);
            }
        }
    }

    public static void setWindowNotTrans(Window w) {
        if (IS_WINDOWS) {
            WindowUtils.setWindowAlpha(w, 0.999f);
        }
    }
}