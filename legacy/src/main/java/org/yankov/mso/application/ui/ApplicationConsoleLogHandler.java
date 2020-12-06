package org.yankov.mso.application.ui;

import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ConsoleService;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ApplicationConsoleLogHandler extends Handler {

    private static final String CLASS_NAME = ApplicationConsoleLogHandler.class.getName();

    public static final String ERROR = CLASS_NAME + "-error";
    public static final String WARNING = CLASS_NAME + "-warning";
    public static final String INFO = CLASS_NAME + "-info";

    private final ConsoleService consoleService;

    public ApplicationConsoleLogHandler(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @Override
    public void publish(LogRecord record) {
        ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

        StringBuilder prefix = new StringBuilder();
        prefix.append("[");
        if (record.getLevel().intValue() == Level.SEVERE.intValue()) {
            prefix.append(resourceBundle.getString(ERROR));
        } else if (record.getLevel().intValue() == Level.WARNING.intValue()) {
            prefix.append(resourceBundle.getString(WARNING));
        } else {
            prefix.append(resourceBundle.getString(INFO));
        }
        prefix.append("] ");

        StringBuilder consoleMessage = new StringBuilder();
        consoleMessage.append(prefix);
        consoleMessage.append(record.getMessage());
        if (record.getThrown() != null) {
            consoleMessage.append(System.lineSeparator());
            Arrays.stream(record.getThrown().getStackTrace()).forEach(ste -> {
                consoleMessage.append(ste.toString());
                consoleMessage.append(System.lineSeparator());
            });
        }

        consoleService.writeMessageWithTimeStamp(consoleMessage.toString());
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

}
