package ecommsapp.ebankbot.controllers;

import ecommsapp.ebankbot.agents.EbankAiAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class EbankChatbotController {
    private final EbankAiAgent ebankAiAgent;

    public EbankChatbotController(ChatClient.Builder chatClient, ChatMemory chatMemory, EbankAiAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "default") String conversationId) {
        return ebankAiAgent.chat(query, conversationId);
    }
    @GetMapping("/chatStream")
    public Flux<String> chatStream(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "default") String conversationId) {
        return ebankAiAgent.chatStream(query, conversationId);
    }
}