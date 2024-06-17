package method.qr.kiarelemb.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    public static Level writeLevel;
    public static ConsoleHandler consoleHandler;
    public static FileHandler fileHandler;

    /**
     * 使用默认的功能初始化 logger 配置
     */
    public static void initLogger() {
        initLogger(Level.INFO, Level.ALL);
    }

    public static void initLogger(Level outputLevel, Level writeLevel) {
        LocalDate now = LocalDate.now();
        String separator = File.separator;
        String dir = "logs" + separator + now.format(DateTimeFormatter.ofPattern("yyyy.MM")) + separator;
        QRFileUtils.dirCreate(dir);
        logFilePath = dir + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
        QRFileUtils.fileCreate(logFilePath);
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                String dataFormat = QRTimeUtils.dateAndTimeMMFormat.format(Long.valueOf(record.getMillis()));

                Thread currentThread = Thread.currentThread();
                StackTraceElement stackTrace = currentThread.getStackTrace()[8];
                String msg = String.format("%s\tlevel:%s\t[%s:%s] %s:%d\t%s\n", dataFormat, record.getLevel(),
                        stackTrace.getClassName(), stackTrace.getMethodName(), stackTrace.getFileName(),
                        stackTrace.getLineNumber(), record.getMessage());
                String throwable = "";
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    throwable = sw.toString();
                }
                return msg + throwable;
            }
        };

        QRLoggerUtils.outputLevel = outputLevel;
        consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(outputLevel);
        consoleHandler.setFormatter(formatter);
        try {
            fileHandler = new FileHandler(logFilePath, true);
            QRLoggerUtils.writeLevel = writeLevel;
            fileHandler.setLevel(writeLevel);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(formatter);
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
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        }
        if (consoleHandler != null) {
            logger.addHandler(consoleHandler);
        } else if (outputLevel != null) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(outputLevel);
            logger.addHandler(consoleHandler);
        }

        return logger;
    }
}