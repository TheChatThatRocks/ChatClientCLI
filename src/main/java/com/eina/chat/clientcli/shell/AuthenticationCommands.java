package com.eina.chat.clientcli.shell;

import com.eina.chat.backendapi.protocol.packages.message.request.GetAuthLevelCommand;
import com.eina.chat.clientcli.services.BackEndCommunicator;
import com.eina.chat.clientcli.services.StateKeeper;
import com.eina.chat.clientcli.utils.AsynchronousMessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static java.util.concurrent.TimeUnit.SECONDS;

@ShellComponent
public class AuthenticationCommands {
    /**
     * Port of the back end
     */
    @Value("${app.back-end-api-ws-port:}")
    private int port;

    /**
     * Uri of the back end
     */
    @Value("${app.back-end-api-ws-uri:}")
    private String backEndURI;

    @Autowired
    StateKeeper stateKeeper;

    @Autowired
    AsynchronousMessageWriter asynchronousMessageWriter;

    @Autowired
    BackEndCommunicator backEndCommunicator;

    @ShellMethod("Login in the chat service")
    @SuppressWarnings("unused")
    public void login(String username, String password) {
        WebSocketHttpHeaders headersUser = new WebSocketHttpHeaders();
        StandardWebSocketClient standardWebSocketClientUser = new StandardWebSocketClient();
        WebSocketStompClient stompClientUser = new WebSocketStompClient(standardWebSocketClientUser);
        stompClientUser.setMessageConverter(new MappingJackson2MessageConverter());

        StompHeaders connectHeadersUser1 = new StompHeaders();
        connectHeadersUser1.add("username", username);
        connectHeadersUser1.add("password", password);

        // Connect
        StompSession sessionUser = null;
        try {
            sessionUser = stompClientUser.connect("ws://" + backEndURI + ":{port}/ws", headersUser,
                    connectHeadersUser1, new StompSessionHandlerAdapter() {
                    }, this.port).get(2, SECONDS);
        } catch (Exception ignore) {
        }

        // Check if connection have failed
        if (sessionUser == null || !sessionUser.isConnected()) {
            System.out.println("Fail when try to authenticate with: " + username + " " + password);
            return;
        }

        // Set authenticated
        stateKeeper.setAuthenticated(true);
        stateKeeper.setUsername(username);

        // Start message receiver
        backEndCommunicator.start(sessionUser);

        // Get auth level
        backEndCommunicator.getSessionUser().send("/app/auth-level", new GetAuthLevelCommand(1));
    }

    @ShellMethod("Get auth level in the chat service")
    @SuppressWarnings("unused")
    public void getAuthLevel() {
        backEndCommunicator.getSessionUser().send("/app/auth-level", new GetAuthLevelCommand(1));
    }

    @ShellMethod("Logout from the chat service")
    @SuppressWarnings("unused")
    public String logout() {
        backEndCommunicator.stop();
        stateKeeper.setAuthenticated(false);
        stateKeeper.setUsername(null);
        return "Operation successful";
    }

    @SuppressWarnings("unused")
    public Availability loginAvailability() {
        return !stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are authenticated yet. Please run logout first");
    }

    @SuppressWarnings("unused")
    public Availability getAuthLevelAvailability() {
        return stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }

    @SuppressWarnings("unused")
    public Availability logoutAvailability() {
        return stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
