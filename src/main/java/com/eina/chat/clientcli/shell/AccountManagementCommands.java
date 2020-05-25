package com.eina.chat.clientcli.shell;

import com.eina.chat.backendapi.protocol.packages.BasicPackage;
import com.eina.chat.backendapi.protocol.packages.common.response.OperationSucceedResponse;
import com.eina.chat.backendapi.protocol.packages.message.request.DeleteAccountCommand;
import com.eina.chat.backendapi.protocol.packages.signup.request.AddAccountCommand;
import com.eina.chat.clientcli.services.BackEndCommunicator;
import com.eina.chat.clientcli.services.StateKeeper;
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

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.SECONDS;

@ShellComponent
public class AccountManagementCommands {
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
    BackEndCommunicator backEndCommunicator;

    @ShellMethod("Sign-up in the chat service")
    @SuppressWarnings("unused")
    public String signUp(String username, String password) {
        // Connection variables
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(standardWebSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Failure variable
        final AtomicReference<Throwable> failure = new AtomicReference<>();

        // Message id
        final int messageId = 1;

        // New user
        AddAccountCommand sendCommandPackage = new AddAccountCommand(messageId, username, password);

        StompSession session = null;
        try {
            session = stompClient.connect(backEndURI, new WebSocketHttpHeaders(), new StompSessionHandlerAdapter() {
            }, this.port).get(2, SECONDS);
        } catch (Exception ignore) {
        }

        // Check if connection have failed
        if (session == null || !session.isConnected())
            return "Fail when try to SignUp with: " + username + " " + password + ": Websocket session not opened";

        final CountDownLatch messagesToReceive = new CountDownLatch(1);

        // Subscribe
        session.subscribe("/user/queue/error/sign-up", new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return BasicPackage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                BasicPackage errorResponse = (BasicPackage) payload;
                if (errorResponse.getMessageId() == messageId && errorResponse instanceof OperationSucceedResponse)
                    messagesToReceive.countDown();
                else {
                    failure.set(new Exception("Unexpected message received or sign-up fail"));
                }
            }
        });

        session.send("/app/sign-up", sendCommandPackage);

        boolean hasReceivedMessage = false;
        try {
            hasReceivedMessage = messagesToReceive.await(5, TimeUnit.SECONDS);
        } catch (Exception ignored) {
        }

        session.disconnect();

        if (failure.get() != null) {
            return "Fail when try to sign-up with: " + username + " " + password + ": " + failure.get().getMessage();
        } else if (!hasReceivedMessage) {
            return "Fail when try to sign-up with: " + username + " " + password + ": " + "Timeout";
        }

        return "Successful SignUp with: " + username + " " + password;
    }

    @ShellMethod("Delete account in the chat service")
    @SuppressWarnings("unused")
    public void deleteAccount() {
        backEndCommunicator.getSessionUser().send("/app/message", new DeleteAccountCommand(1));
        stateKeeper.setAuthenticated(false);
        stateKeeper.setUsername(null);
    }

    @SuppressWarnings("unused")
    public Availability signUpAvailability() {
        return !stateKeeper.isAuthenticated()
                ? Availability.available()
                : Availability.unavailable("you are authenticated yet. Please run logout first");
    }

    @SuppressWarnings("unused")
    public Availability deleteAccountAvailability() {
        return stateKeeper.isAuthenticated() && !stateKeeper.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you are not authenticated");
    }
}
