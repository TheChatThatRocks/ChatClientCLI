package com.eina.chat.clientcli.shell;

import com.eina.chat.backendapi.protocol.packages.message.request.*;
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
    public String sendMessageToUser(String username, String message) {
        if (message.length() > 500)
            return "Operation fail: Message is too big";

        backEndCommunicator.getSessionUser().send("/app/message",
                new SendMessageToUserCommand(1, username, message));
        return "";
    }

    @ShellMethod("Send message to chat room")
    @SuppressWarnings("unused")
    public String sendMessageToRoom(String roomName, String message) {
        if (message.length() > 500)
            return "Operation fail: Message is too big";

        backEndCommunicator.getSessionUser().send("/app/message",
                new SendMessageToRoomCommand(1, roomName, message));

        return "";
    }

    @ShellMethod("Send file to user")
    @SuppressWarnings("unused")
    public String sendFileToUser(String username, String filePath) {
        String filename = Paths.get(filePath).getFileName().toString();
        byte[] fileInBytes = FileManagement.readFile(filePath);
        if (fileInBytes == null)
            return "Operation fail: File path not exist";
        byte[] fileInBytesWithName = FileManagement.concatenateFileAndName(filename, fileInBytes);
        if (fileInBytesWithName.length > 1024 * 7)
            return "Operation fail: File is too big";

        backEndCommunicator.getSessionUser().send("/app/message",
                new SendFileToUserCommand(1, username, fileInBytesWithName));

        return "";
    }

    @ShellMethod("Send file to chat room")
    @SuppressWarnings("unused")
    public String sendFileToRoom(String roomName, String filePath) {
        String filename = Paths.get(filePath).getFileName().toString();
        byte[] fileInBytes = FileManagement.readFile(filePath);
        if (fileInBytes == null)
            return "Operation fail: File path not exist";

        byte[] fileInBytesWithName = FileManagement.concatenateFileAndName(filename, fileInBytes);
        if (fileInBytesWithName.length > 1024 * 7)
            return "Operation fail: File is too big";

        backEndCommunicator.getSessionUser().send("/app/message",
                new SendFileToRoomCommand(1, roomName, fileInBytesWithName));
        return "";
    }

    @ShellMethod("Get messages from room")
    @SuppressWarnings("unused")
    public void getMessagesFromRoom(String roomName) {
        backEndCommunicator.getSessionUser().send("/app/message", new GetMessageHistoryFromRoomCommand(1, roomName));
    }

    @ShellMethod("Get files from room")
    @SuppressWarnings("unused")
    public void getFilesFromRoom(String roomName) {
        backEndCommunicator.getSessionUser().send("/app/message", new GetFileHistoryFromRoomCommand(1, roomName));
    }

    @ShellMethodAvailability
    @SuppressWarnings("unused")
    public Availability messagingMethodsAvailability() {
        return stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
