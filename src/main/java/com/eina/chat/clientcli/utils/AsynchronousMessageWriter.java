package com.eina.chat.clientcli.utils;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsynchronousMessageWriter {
    @Autowired
    Terminal terminal;

    @Autowired
    LineReader lineReader;

    public void println(String message) {
        // TODO: If some command is in the buffer it is cleared
        //String writeCommand = lineReader.getBuffer().toString();
        //String line = lineReader.readLine();
        lineReader.callWidget(LineReader.CLEAR);
        lineReader.getTerminal().writer().println(message);
        lineReader.callWidget(LineReader.REDRAW_LINE);
        lineReader.callWidget(LineReader.REDISPLAY);
        lineReader.getTerminal().writer().flush();
        //lineReader.getTerminal().writer().print(writeCommand);
    }
}
