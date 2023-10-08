package com.sivalabs.tcguidesbot.domain;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ConversationalRetrievalChain chain;

    public ChatService(ConversationalRetrievalChain chain) {
        this.chain = chain;
    }

    public String ask(String question) {
        log.debug("======================================================");
        log.debug("Question: " + question);
        String answer = chain.execute(question);
        log.debug("Answer: " + answer);
        log.debug("======================================================");
        return answer;
    }
}
