package com.eina.chat.clientcli.shell;

import com.eina.chat.backendapi.protocol.packages.message.request.SendFileToRoomCommand;
import com.eina.chat.backendapi.protocol.packages.message.request.SendFileToUserCommand;
import com.eina.chat.backendapi.protocol.packages.message.request.SendMessageToRoomCommand;
import com.eina.chat.backendapi.protocol.packages.message.request.SendMessageToUserCommand;
import com.eina.chat.clientcli.services.BackEndCommunicator;
import com.eina.chat.clientcli.services.StateKeeper;
import com.eina.chat.clientcli.utils.FileManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.nio.file.Paths;

@ShellComponent
public class MessagingCommands {

    @Autowired
    BackEndCommunicator backEndCommunicator;

    @Autowired
    StateKeeper stateKeeper;

    @ShellMethod("Send message to user")
    @SuppressWarnings("unused")
    public void sendMessageToUser(String username, String message) {
        backEndCommunicator.getSessionUser().send("/app/message",
                new SendMessageToUserCommand(1, username, message));
    }

    @ShellMethod("Send message to chat room")
    @SuppressWarnings("unused")
    public void sendMessageToRoom(String roomName, String message) {
        backEndCommunicator.getSessionUser().send("/app/message",
                new SendMessageToRoomCommand(1, roomName, message));
    }

    @ShellMethod("Send file to user")
    @SuppressWarnings("unused")
    public void sendFileToUser(String username, String filePath) {
        String filename = Paths.get(filePath).getFileName().toString();
        byte[] fileInBytes = FileManagement.readFile(filePath);
        byte[] fileInBytesWithName = FileManagement.concatenateFileAndName(filename, fileInBytes);
        backEndCommunicator.getSessionUser().send("/app/message",
                new SendFileToUserCommand(1, username, fileInBytesWithName));
    }

    @ShellMethod("Send file to chat room")
    @SuppressWarnings("unused")
    public void sendFileToRoom(String roomName, String filePath) {
        String filename = Paths.get(filePath).getFileName().toString();
        byte[] fileInBytes = FileManagement.readFile(filePath);
        byte[] fileInBytesWithName = FileManagement.concatenateFileAndName(filename, fileInBytes);
        backEndCommunicator.getSessionUser().send("/app/message",
                new SendFileToRoomCommand(1, roomName, fileInBytesWithName));
    }

    @ShellMethodAvailability
    @SuppressWarnings("unused")
    public Availability messagingMethodsAvailability() {
        return stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
