package ecommsapp.ebankbot.agents;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Service
public class EbankAiAgent {
    private final ChatClient chatClient;

    @SuppressWarnings("removal") // defaultToolCallbacks is deprecated in Spring AI 2.0 but still works
    public EbankAiAgent(ChatClient.Builder chatClient, ChatMemory chatMemory, ToolCallbackProvider tools) {
        // Debug: shows in the console which MCP tools the bot received
        Arrays.stream(tools.getToolCallbacks())
                .forEach(t -> System.out.println("MCP tool: " + t.getToolDefinition().name()));

        this.chatClient = chatClient
                .defaultSystem("""
                        Vous êtes un assistant bancaire qui répond aux questions de l'utilisateur
                        à propos des clients et des comptes bancaires.
                        Pour obtenir les données, utilisez toujours les outils (tools) disponibles :
                        par exemple, pour lister les comptes, appelez l'outil qui retourne tous les comptes.
                        Répondez uniquement à partir des résultats des outils.
                        Si les outils ne permettent pas de répondre, répondez : JE NE SAIS PAS.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(tools)
                .build();
    }

    public String chat(String query, String conversationId) {
        return chatClient.prompt(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public Flux<String> chatStream(String query, String conversationId) {
        return chatClient.prompt(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }
}