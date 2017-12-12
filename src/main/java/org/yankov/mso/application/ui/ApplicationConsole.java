package org.yankov.mso.application.ui;

import javafx.scene.Node;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;

public class ApplicationConsole implements UserInterfaceControls<Node>, ConsoleService {

    private static ApplicationConsole instance;

    private static final int PREF_ROW_COUNT = 5;
    private static final String TIME_STAMP_FORMAT = "[%02d.%02d.%04d, %02d:%02d:%02d] ";

    private TextArea textArea;

    private ApplicationConsole() {
        this.textArea = new TextArea();
    }

    public static ApplicationConsole getInstance() {
        if (instance == null) {
            instance = new ApplicationConsole();
        }
        return instance;
    }

    @Override
    public void layout() {
        textArea.setPrefRowCount(PREF_ROW_COUNT);
    }

    @Override
    public Node getContent() {
        return textArea;
    }

    @Override
    public void clear() {
        textArea.clear();
    }

    @Override
    public void writeMessage(String message) {
        textArea.appendText(message);
        textArea.appendText(System.lineSeparator());
    }

    @Override
    public void writeMessageWithTimeStamp(String message) {
        StringBuilder messageWithTimeStamp = new StringBuilder();
        messageWithTimeStamp.append(produceTimeStamp());
        messageWithTimeStamp.append(message);

        textArea.appendText(messageWithTimeStamp.toString());
        textArea.appendText(System.lineSeparator());
    }

    private String produceTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        return String.format(TIME_STAMP_FORMAT, now.getDayOfMonth(), now.getMonthValue(), now.getYear(), now.getHour(),
                             now.getMinute(), now.getSecond());
    }

}
