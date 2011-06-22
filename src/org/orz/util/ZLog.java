package org.orz.util;

public class ZLog {

    public static void info(String s) {
        if (System.getProperty("zlog.enable_color") != null) {
            System.out.println("\033[22;37m [ II ] " + s + "\033[m");
        } else {
            System.out.println("[ II ] " + s);
        }
    }

    public static void warn(String s) {
        if (System.getProperty("zlog.enable_color") != null) {
            System.out.println("\033[22;33m [ WW ] " + s + "\033[m");
        } else {
            System.out.println("[ WW ] " + s);
        }
    }

    public static void err(String s) {
        if (System.getProperty("zlog.enable_color") != null) {
            System.out.println("\033[22;31m [ EE ] " + s + "\033[m");
        } else {
            System.out.println("[ EE ] " + s);
        }
    }
}
