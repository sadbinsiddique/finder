package com.market.finder.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class SystemLogConverter extends ClassicConverter {

    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_RESET = "\u001B[0m";

    @Override
    public String convert(ILoggingEvent event) {
        String msg = event.getFormattedMessage();
        String loggerName = event.getLoggerName();

        if (msg == null) {
            return "";
        }

        if ("org.hibernate.orm.jdbc.bind".equals(loggerName)) {
            if (msg.contains("binding parameter")) {
                if (msg.toLowerCase().contains("role") || msg.toLowerCase().contains("permission")) {
                    return ANSI_PURPLE + msg.replace("binding parameter", "System Role Change") + ANSI_RESET;
                } else if (msg.toLowerCase().contains("update") || msg.toLowerCase().contains("insert") || msg.toLowerCase().contains("delete")) {
                    return ANSI_YELLOW + msg.replace("binding parameter", "System data change") + ANSI_RESET;
                } else {
                    return ANSI_CYAN + msg.replace("binding parameter", "System Entry") + ANSI_RESET;
                }
            }
        }

        return msg;
    }
}
