package com.saidas.saiscaling.core.utility;

import com.saidas.saiscaling.SaiDifficultyMod;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SaiLogger {
    public static void info(String message) {
        SaiDifficultyMod.LOGGER.info(message);
    }

    public static void info(String format, Object ... params) { info(String.format(format, params)); }

    public static void warn(String message) {
        SaiDifficultyMod.LOGGER.warn(message);
    }

    public static void warn(String format, Object ... params) {
        warn(String.format(format, params));
    }

    public static void error(String message) {
        SaiDifficultyMod.LOGGER.error(message);
    }

    public static void error(String format, Object ... params) { error(String.format(format, params)); }

    public static void exception(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        SaiDifficultyMod.LOGGER.error(sw.toString());
        pw.close();
    }
}
