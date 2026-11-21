package ecommsapp.ebankbot.discord;

import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import ecommsapp.ebankbot.agents.EbankAiAgent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@DiscordController
public class DiscordBot {
    private final EbankAiAgent ebankAiAgent;

    public DiscordBot(EbankAiAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }

    @DiscordMapping
    public void perform(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String query = event.getMessage().getContentRaw();
        String userId = event.getAuthor().getId(); // Use the Discord user ID as the memory ID

        System.out.println("🤖 Message from " + userId + ": " + query);

        String response = ebankAiAgent.chat(query, userId);
        event.getChannel().sendMessage(response).queue();
    }
}