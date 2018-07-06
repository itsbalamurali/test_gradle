package com.girmiti.mobilepos.logger;

import java.util.Calendar;

public abstract class Logger {

    private boolean enable = true;

    public static final int LEVEL_ERROR = 0;
    public static final int LEVEL_WARN = 1;
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_DIAGNOSE = 4;
    public static final int LEVEL_TRACE = 5;
    private static int _level = LEVEL_INFO;
    private final String BUILD_VERSION = "1.0";

    private static Logger _logger = null;
    private String[] _prefixes = {"SEVERE", "WARN", "DEBUG", "INFO", "DIAGNOSE", "TRACE"};
    private final static String MONTHS[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private String _name = "DEFAULT";

    protected Logger() {
    }

    protected Logger(String name) {
        _name = name;
    }

    public static void init(String logger) {
        if ("console".equalsIgnoreCase(logger)) {
            _logger = new ConsoleLogger();
        } else if ("file".equalsIgnoreCase(logger)) {
            FileLogger fileLogger = new FileLogger(logger);
            fileLogger.init();
            _logger = fileLogger;
        }
    }


    /**
     * Use this method to get the logger when it is required to preserve the
     * class and method names in the logs when the library is obfuscated.
     *
     * @param name Fully qualified name of the class
     * @return
     */
    public static Logger getNewLogger(String name) {
        if (_logger == null) {
            init("Console");
        }
        return _logger.newLogger(name);
    }

    /**
     * Use this method to get logger when the library would not be obfuscated.
     *
     * @param obj Object specific logger
     * @return
     */
    public static Logger getNewLogger(Object obj) {
        String name = obj.getClass().getName();
        return _logger.newLogger(name);
    }

    /**
     * <ul>
     * <li>When log level is set to ERROR, only error conditions that are errors
     * are logged</li>
     * <li>
     * When log level is set to WARN, the following are logged
     * <ul>
     * <li>ERROR</li>
     * <li>WARN</li>
     * </ul>
     * </li>
     * <li>
     * When log level is set to DEBUG, the following are logged
     * <ul>
     * <li>ERROR</li>
     * <li>WARN</li>
     * <li>DEBUG</li>
     * </ul>
     * </li>
     * <li>When log level is set to INFO, everything is logged</li>
     * </ul>
     *
     * @param level Set Level for the entire logging system
     */
    public static void setLevel(int level) {
        _level = level;
    }

    private void log(int level, String message) {
        boolean log = false;
        String logMesg;
        if (level == LEVEL_DIAGNOSE)
            log = true;
        if (level == LEVEL_TRACE)
            log = true;
        if (_level == LEVEL_ERROR) {
            if(level == LEVEL_ERROR)
                log = true;
        } else if (_level == LEVEL_WARN && (level == LEVEL_ERROR || level == LEVEL_WARN)) {
            log = true;
        } else if (_level == LEVEL_DEBUG && (level == LEVEL_ERROR || level == LEVEL_WARN || level == LEVEL_DEBUG)) {
            log = true;
        } else if (_level == LEVEL_INFO) {
            log = true;
        }
        logMesg = "[" + now() + "] " + "[" + Thread.currentThread().getName() + "] " + "[" + BUILD_VERSION + "] " + " [" + _prefixes[level] + "] " + _name + "."
                + message;
        if (log && enable) {
            writeLog(logMesg);
        }
    }

    public abstract void init();

    public static void closeLog() {
        _logger.close();
    }

    public abstract void close();

    protected abstract void writeLog(String mesg);

    protected abstract Logger newLogger(String name);

    public void severe(String message) {
        log(LEVEL_ERROR, message);
    }

    public void info(String message) {
        log(LEVEL_INFO, message);
    }

    public void error(String message) {
        log(LEVEL_ERROR, message);
    }

    public void debug(String message) {
        log(LEVEL_DEBUG, message);
    }

    public void diagnose(String message) {
        log(LEVEL_DIAGNOSE, message);
    }

    public void trace(String message) {
        log(LEVEL_TRACE, message);
    }

    protected String now() {

        StringBuilder f = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        String mon = MONTHS[cal.get(Calendar.MONTH)];
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        f.append(mon);
        f.append("-");
        f.append(day);
        f.append("-");
        f.append(year);
        f.append(" ");
        f.append(hr);
        f.append(":");
        f.append(min);
        f.append(":");
        f.append(sec);
        return f.toString();
    }
}
