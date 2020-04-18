package com.eina.chat.clientcli.shell;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AuthenticationCommands {
    @Autowired
    Terminal terminal;

    @Autowired
    LineReader lineReader;

    private Thread thread = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // lineReader.printAbove();

            lineReader.callWidget(LineReader.CLEAR);
            lineReader.getTerminal().writer().println("World!");
            lineReader.callWidget(LineReader.REDRAW_LINE);
            lineReader.callWidget(LineReader.REDISPLAY);
            lineReader.getTerminal().writer().flush();

            // terminal.writer().println("Prueba desde el thread");
            // terminal.writer().flush();
        }
    };

    @ShellMethod("Login in the chat service")
    public String login(String username, String password
    ) {
        // System.out.println("Prueba");
        thread.start();
        // invoke service
        return "Try to authenticate with: " + username + " " + password;
    }
}
