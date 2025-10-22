package com.onesnzeroes.clashnzeroes.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class Trace {

    private static final String LOG_FILE = "app.log"; // change path if needed

    private Trace() {}

    public static void info(String message) {
        log("[INFO]", message);
    }

    public static void debug(String message) {
        log("[DEBUG]", message);
    }

    public static void warn(String message) {
        log("[WARN]", message);
    }

    public static void error(String message, Throwable t) {
        log("[ERROR]", message);
        if (t != null) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                t.printStackTrace(pw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static synchronized void log(String level, String message) {
        String logMessage = level + " " + timestamp() + " - " + message;
        System.out.println(logMessage); // optional: still print to console
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String timestamp() {
        return Instant.now().toString();
    }
}