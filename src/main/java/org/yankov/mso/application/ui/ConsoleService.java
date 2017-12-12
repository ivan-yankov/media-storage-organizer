package org.yankov.mso.application.ui;

public interface ConsoleService {

    void clear();

    void writeMessage(String message);

    void writeMessageWithTimeStamp(String message);

}
