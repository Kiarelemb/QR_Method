package method.qr.kiarelemb.utils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.*;

/**
 * @author Kiarelemb
 * @projectName QR_Method
 * @className QRLoggerUtils
 * @description 当前 log 方法还存在诸多问题，有待完善
 * @create 2024/6/17 下午8:03
 */
public class QRLoggerUtils {
    public static String logFilePath;
    public static Level outputLevel;
    public static Level writeLevel;
    public static ConsoleHandler consoleHandler;
    public static FileHandler fileHandler;
    public static int classMsgMaxLength = 152;
    public static String prefix = "method";

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

                StackTraceElement[] trace = Thread.currentThread().getStackTrace();
                StackTraceElement stackTrace = trace[0];
                for (StackTraceElement element : trace) {
                    if (element.getClassName().startsWith(prefix)) {
                        stackTrace = element;
                        break;
                    }
                }
                String levelTmp = record.getLevel().toString();
                String level = levelTmp + "\t".repeat((8 - levelTmp.length()) / 4 + (levelTmp.length() % 4 == 0 ? 0 : 1));

                String classTmp = String.format("[%s:%s] %s:%d", stackTrace.getClassName(), stackTrace.getMethodName(),
                        stackTrace.getFileName(), stackTrace.getLineNumber());
                int restLen = classMsgMaxLength - classTmp.length();
                int times = restLen / 4 + (classTmp.length() % 4 == 0 ? 0 : 1);
                String classMsg = classTmp + "\t".repeat(Math.max(times, 0));
                String msg = String.format("%s\t%s\t%s\t%s\n", dataFormat, level, classMsg, record.getMessage());
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
        consoleHandler = new ConsoleHandler() {

            private String preRecord = "";

            @Override
            public synchronized void publish(LogRecord record) {
                String format = getFormatter().format(record);
                if (format.equals(preRecord)) {
                    return;
                }
                preRecord = format;
                super.publish(record);
            }
        };
        consoleHandler.setLevel(outputLevel);
        consoleHandler.setFormatter(formatter);
        try {
            fileHandler = new FileHandler(logFilePath, true) {

                private String preRecord = "";

                @Override
                public synchronized void publish(LogRecord record) {
                    String format = getFormatter().format(record);
                    if (format.equals(preRecord)) {
                        return;
                    }
                    preRecord = format;
                    super.publish(record);
                }
            };
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
        logger.setLevel(writeLevel);
        return logger;
    }

    public static void log(Logger logger, Level level, String format, Object... args) {
        logger.log(level, String.format(format, args));
    }
}