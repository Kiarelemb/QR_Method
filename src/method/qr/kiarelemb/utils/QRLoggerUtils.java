package method.qr.kiarelemb.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * @author Kiarelemb
 * @projectName QR_Method
 * @className QRLoggerUtils
 * @description TODO
 * @create 2024/6/17 下午8:03
 */
public class QRLoggerUtils {
    public static String logFilePath;
    public static Level outputLevel;
    public static ConsoleHandler consoleHandler;
    public static FileHandler fileHandler;

    /**
     * 使用默认的功能初始化 logger 配置
     */
    public static void initLogger() {
        LocalDate now = LocalDate.now();
        String separator = File.separator;
        String dir = "logs" + separator + now.format(DateTimeFormatter.ofPattern("yyyy.MM")) + separator;
        QRFileUtils.dirCreate(dir);
        logFilePath = dir + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
        QRFileUtils.fileCreate(logFilePath);

        outputLevel = Level.INFO;
        consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(outputLevel);

        try {
            fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    StringBuilder sb = new StringBuilder();
                    String dataFormat = QRTimeUtils.dateAndTimeMMFormat.format(Long.valueOf(record.getMillis()));
                    sb.append(dataFormat).append("\t");
                    sb.append("level:").append(record.getLevel()).append("\t");
                    sb.append(record.getMessage()).append("\n");
                    return sb.toString();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initLogger(String logFilePath, Level outputLevel, FileHandler fileHandler) {
        QRLoggerUtils.logFilePath = logFilePath;
        QRLoggerUtils.outputLevel = outputLevel;
        QRLoggerUtils.consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(outputLevel);
        QRLoggerUtils.fileHandler = fileHandler;
    }

    public static void initLogger(String logFilePath, ConsoleHandler consoleHandler, FileHandler fileHandler) {
        QRLoggerUtils.logFilePath = logFilePath;
        QRLoggerUtils.consoleHandler = consoleHandler;
        QRLoggerUtils.fileHandler = fileHandler;
    }

    public static Logger getLogger(Class c) {
        Logger logger = Logger.getLogger(c.getPackageName());
        if (consoleHandler != null) {
            logger.addHandler(consoleHandler);
        } else if (outputLevel != null) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(outputLevel);
            logger.addHandler(consoleHandler);
        }
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }
        return logger;
    }
}