package com.eina.chat.clientcli.config;

import com.eina.chat.clientcli.services.StateKeeper;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {
    @Autowired
    StateKeeper stateKeeper;

    @Override
    public AttributedString getPrompt() {
        if (!stateKeeper.isAuthenticated())
            return new AttributedString("not-authenticated:>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE));
        else
            return new AttributedString(stateKeeper.getUsername() + ":>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
    }
}
