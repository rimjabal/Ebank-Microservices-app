package ecommsapp.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class EbankAiAgent {
    private ChatClient chatClient;

    public EbankAiAgent(ChatClient.Builder builder, ChatMemory chatMemory, ToolCallbackProvider tools) {
        this.chatClient = builder
                .defaultSystem("Vous êtes un assistant qui se charge de répondre aux questions de l'utilisateur en fonction du contexte fourni à propos des clients et des comptes bancaires. Si aucun contexte n'est fourni tu réponds avec je ne sais pas.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(tools) // Updated for 2.0.1
                .build();
    }

    public String chat(String query, String conversationId) {
        return chatClient.prompt(query)
                // Replaced the broken import with the correct Spring AI 2.0 constant
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}