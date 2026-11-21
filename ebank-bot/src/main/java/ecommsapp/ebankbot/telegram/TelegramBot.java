package ecommsapp.ebankbot.telegram;

import ecommsapp.ebankbot.agents.EbankAiAgent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.token}")
    private String telegramBotToken;

    private final EbankAiAgent aiAgent;

    public TelegramBot(EbankAiAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @PostConstruct
    public void registerTelegramBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to register Telegram bot", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        String caption = update.getMessage().getCaption();
        List<PhotoSize> photos = update.getMessage().getPhoto();

        String query = messageText != null ? messageText : caption;
        if ((query == null || query.isBlank()) && (photos == null || photos.isEmpty())) {
            return;
        }
        if (query == null || query.isBlank()) {
            query = "What do you see in this image?";
        }

        try {
            sendTypingAction(chatId);
            String answer = aiAgent.chat(String.valueOf(chatId), query);
            sendTextMessage(chatId, answer);
        } catch (Exception e) {
            throw new RuntimeException("Error handling Telegram update", e);
        }
    }

    @Override
    public String getBotUsername() {
        return "ebankBot";
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTypingAction(Long chatId) throws TelegramApiException {
        SendChatAction action = new SendChatAction();
        action.setChatId(String.valueOf(chatId));
        action.setAction(ActionType.TYPING);
        execute(action);
    }

    private void sendTextMessage(Long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text != null && !text.isBlank() ? text : "No response generated.");
        execute(message);
    }
}