package org.yankov.mso.application.command;

public interface Command {

    void execute(Object... commandArgs) throws InvalidCommandArgumentsException;

}
