package com.ahwers.marvin.framework.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandFormatterTest {

    CommandFormatter commandFormatter = new CommandFormatter();

    @Test
    public void formatTest() {
        String command = "hey, MARVIN; SUCCESSFUL marvin REQUEST test. please";
        String formattedCommand = commandFormatter.formatCommand(command);
        assertEquals("successful marvin request test", formattedCommand);
    }

}
