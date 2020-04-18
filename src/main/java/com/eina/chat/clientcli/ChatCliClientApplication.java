package com.eina.chat.clientcli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatCliClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatCliClientApplication.class, args);
    }

//    @Bean
//    public PromptProvider myPromptProvider() {
//        return () -> new AttributedString("my-shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
//    }

}
