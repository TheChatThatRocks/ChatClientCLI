package com.eina.chat.clientcli.shell;

import com.eina.chat.backendapi.protocol.packages.admin.request.SendMessageToAllCommand;
import com.eina.chat.clientcli.services.BackEndCommunicator;
import com.eina.chat.clientcli.services.StateKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AdminMessagingCommands {
    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    BackEndCommunicator backEndCommunicator;

    @ShellMethod("Send message to all users")
    @SuppressWarnings("unused")
    public String sendMessageToAllUser(String message) {
        if (message.length() > 500)
            return "Operation fail: Message is too big";

        backEndCommunicator.getSessionUser().send("/app/admin", new SendMessageToAllCommand(1, message));
        return "";
    }

    @SuppressWarnings("unused")
    public Availability sendMessageToAllUserAvailability() {
        return stateKeeper.isAuthenticated() && stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
