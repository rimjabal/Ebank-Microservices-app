package ecommsapp.ebankbot.controllers;

import ecommsapp.ebankbot.agents.EbankAiAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbankChatbotController {
private EbankAiAgent ebankAiAgent;

    public EbankChatbotController(EbankAiAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }


    @GetMapping("/chat, produces=MediaType.TEXT_PLAIN_VALUE")
    public String chat(@RequestParam(name= "query", defaultValue = "bonjour") String query){
        return ebankAiAgent.chat(query);
    }
}
