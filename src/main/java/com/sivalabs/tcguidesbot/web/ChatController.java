package com.sivalabs.tcguidesbot.web;

import com.sivalabs.tcguidesbot.domain.Answer;
import com.sivalabs.tcguidesbot.domain.ChatService;
import com.sivalabs.tcguidesbot.domain.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
class ChatController {
    private static final List<String> samplesQuestions = List.of(
        "What is Testcontainers special JDBC URL for PostgreSQL?",
        "Initializing Testcontainers by copying files into it",
        "Initializing the container by executing commands inside container"
    );

    private final ChatService chatService;

    ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    String index(Model model) {
        model.addAttribute("samplesQuestions", samplesQuestions);
        return "index";
    }

    @PostMapping("/ask")
    @ResponseBody
    Answer ask(@RequestBody Question question) {
        try {
            String response = chatService.ask(question.question());
            return new Answer(question.question(), response, false);
        } catch (Exception e) {
            return new Answer(question.question(), "Sorry, I don't know.", true);
        }
    }
}
