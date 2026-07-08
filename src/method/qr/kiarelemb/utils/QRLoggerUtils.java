package method.qr.kiarelemb.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.function.Consumer;
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
     * 使用默认等级初始化日志系统。
     * <p>
     * 默认控制台输出 {@link Level#INFO} 及以上日志，日志文件写入 {@link Level#ALL}
     * 及以上日志。日志文件会创建在 {@code logs/yyyy.MM/yyyy-MM-dd.log}。
     * <p>
     * 典型用法：
     * <pre>{@code
     * QRLoggerUtils.prefix = "grade";
     * QRLoggerUtils.initLogger();
     * Logger logger = QRLoggerUtils.getLogger(Enter.class);
     * logger.info("application started");
     * }</pre>
     */
    public static void initLogger() {
        initLogger(Level.INFO, Level.ALL);
    }

    /**
     * 初始化本工具包统一使用的控制台和文件日志处理器。
     * <p>
     * 调用后再通过 {@link #getLogger(Class)} 获取 logger。日志格式中的类名和行号
     * 不是直接来自 {@link LogRecord#getSourceClassName()}，而是从当前调用栈里寻找第一个
     * 以 {@link #prefix} 开头的类，因此接入新项目时通常要先设置 {@code prefix}。
     * {@link #classMsgMaxLength} 用于控制“类名:方法 文件:行号”列的制表符对齐宽度。
     *
     * @param outputLevel 控制台输出等级，例如 {@link Level#INFO}
     * @param writeLevel  文件写入等级，例如 {@link Level#CONFIG} 或 {@link Level#ALL}
     */
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
                String dataFormat = QRTimeUtils.dateAndTimeMMFormat.format(record.getMillis());

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
                String total = msg + throwable;
                if (!CONSUMER_LINKED_LIST.isEmpty()) CONSUMER_LINKED_LIST.forEach(c -> c.accept(total));
                return total;
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

    /**
     * 获取指定类所属包名对应的 logger，并挂载已初始化的处理器。
     * <p>
     * 该方法会使用 {@code c.getPackageName()} 作为 logger 名称，所以同一包下的类会共享
     * 一个 logger。请先调用 {@link #initLogger()} 或 {@link #initLogger(Level, Level)}；
     * 未初始化文件处理器时只会尝试使用控制台处理器。
     * <p>
     * 使用例：
     * <pre>{@code
     * private static final Logger LOGGER = QRLoggerUtils.getLogger(ProjectLoader.class);
     * LOGGER.config("project loaded");
     * }</pre>
     *
     * @param c 用于确定 logger 包名的类
     * @return 已配置处理器和等级的 logger
     */
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


    private static final LinkedList<Consumer<String>> CONSUMER_LINKED_LIST = new LinkedList<>();

    /**
     * 添加一份额外的日志文本输出回调。
     * <p>
     * 每条日志在 formatter 中完成格式化后，会同步调用这里注册的所有回调。
     * 如果把日志输出到 Swing 组件，回调内部需要自行切回 EDT，例如使用
     * {@code SwingUtilities.invokeLater(...)}。
     *
     * @param c 接收完整日志文本的回调，文本中可能包含异常堆栈和末尾换行
     */
    public static void addMessageOutputAction(Consumer<String> c) {
        CONSUMER_LINKED_LIST.add(c);
    }

    /**
     * 按 {@link String#format(String, Object...)} 规则格式化日志消息后输出。
     *
     * @param logger 目标 logger
     * @param level  日志等级
     * @param format 格式化模板
     * @param args   模板参数
     */
    public static void log(Logger logger, Level level, String format, Object... args) {
        logger.log(level, String.format(format, args));
    }
}
