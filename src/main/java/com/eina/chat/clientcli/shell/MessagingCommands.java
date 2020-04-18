package com.eina.chat.clientcli.shell;

import com.eina.chat.clientcli.services.StateKeeper;
import com.eina.chat.clientcli.utils.AsynchronousMessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ShellComponent
public class MessagingCommands {
    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    AsynchronousMessageWriter asynchronousMessageWriter;

    @ShellMethod("Send message to user")
    @SuppressWarnings("unused")
    public String sendMessageToUser(String username, String message) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethod("Send message to chat room")
    @SuppressWarnings("unused")
    public String sendMessageToRoom(String roomName, String message) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethod("Send file to user")
    @SuppressWarnings("unused")
    public String sendFileToUser(String username, String filePath) {
        // TODO: Implement
        return "Try to create room";
    }

    @ShellMethod("Send file to chat room")
    @SuppressWarnings("unused")
    public String sendFileToRoom(String roomName, String filePath) {
        // TODO: Implement
        return "Try to create room";
    }
}
