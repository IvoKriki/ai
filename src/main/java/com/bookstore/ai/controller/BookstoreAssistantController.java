package com.bookstore.ai.controller;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/bookstore")
class BookstoreAssistantController {
    private final OpenAiChatClient chatClient;

    public BookstoreAssistantController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/information")
    public String bookstoreChat(@RequestParam(value = "message", defaultValue = "What are the 3 top java books?") String message) {
        return chatClient.call(message);
    }

    @GetMapping("/information-prompt")
    public ChatResponse bookstoreChatPrompt(@RequestParam(value = "message", defaultValue = "What are the 3 top java books?") String message) {
        return chatClient.call(new Prompt(message));
    }

    @GetMapping("/review")
    public String bookstoreReview(@RequestParam(value = "book", defaultValue = "bible") String book) {
        PromptTemplate promptTemplate = new PromptTemplate(""" 
                  Please, provide a feedback from {book} and also a short biography from the author.
                  """);
        promptTemplate.add("book", book);
        return this.chatClient.call(promptTemplate.create()).getResult().getOutput().getContent();
    }

    @GetMapping("/stream/information")
    public Flux<String> bookstoreChatStream(@RequestParam(value = "message",
            defaultValue = "What are the java best sellers book?") String message) {
        return chatClient.stream(message);
    }

}
