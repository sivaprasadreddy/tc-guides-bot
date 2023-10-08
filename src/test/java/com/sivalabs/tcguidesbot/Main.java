package com.sivalabs.tcguidesbot;

import com.sivalabs.tcguidesbot.config.ApiKeys;
import com.sivalabs.tcguidesbot.config.AppConfig;
import com.sivalabs.tcguidesbot.config.ApplicationProperties;
import com.sivalabs.tcguidesbot.domain.ChatService;
import dev.langchain4j.chain.ConversationalRetrievalChain;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ApplicationProperties properties = new ApplicationProperties(ApiKeys.OPENAI_API_KEY);
        AppConfig appConfig = new AppConfig(properties);
        ConversationalRetrievalChain chain = appConfig.chain();
        ChatService chatService = new ChatService(chain);

        List<String> questions = List.of(
            "What is Testcontainers special JDBC URL for PostgreSQL?",
            "Initializing the container by copying files into it",
            "Initializing the container by executing commands inside container"
        );

        for (String question : questions) {
            String answer = chatService.ask(question);
            System.out.println("======================================================");
            System.out.println("Question: " + question);
            System.out.println("Answer: " + answer);
            System.out.println("======================================================");
        }

    }
}