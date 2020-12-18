package crow.teomant.modular.handshakes.bot;

import crow.teomant.modular.handshakes.bot.commands.HandshakeBotCommandFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Slf4j
public class HandShakeBot extends TelegramLongPollingBot {

    private final HandshakeBotCommandFactory factory;


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage()) {
            try {
                sendMsg(factory.getCommand(update.getMessage()).processMessage((update.getMessage())));
            } catch (Exception e) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText(e.getMessage());
                sendMsg(sendMessage);
            }
        }
    }

    private synchronized void sendMsg(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Exception: " + e.toString());
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    @Override
    public String getBotUsername() {
        return "TeomantTestBot";
    }

    //токен оставлен намерено, чтобы можно было быстро потестить
    @Override
    public String getBotToken() {
        return "623916553:AAFEEQdA0vkfj6NYxjObj_XUYksCQAgV3Mg";
    }
}
