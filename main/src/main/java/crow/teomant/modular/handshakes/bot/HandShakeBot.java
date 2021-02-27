package crow.teomant.modular.handshakes.bot;

import crow.teomant.modular.handshakes.bot.commands.HandshakeBotCommandFactory;
import crow.teomant.modular.handshakes.common.exchange.PathResponseExchange;
import crow.teomant.modular.handshakes.common.exchange.RelationExchange;
import crow.teomant.modular.handshakes.user.domain.exchange.service.MqListener;
import crow.teomant.modular.handshakes.user.domain.exchange.service.MqService;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class HandShakeBot extends TelegramLongPollingBot implements MqListener {

    private final HandshakeBotCommandFactory factory;
    private final UserService userService;

    public HandShakeBot(HandshakeBotCommandFactory factory,
                        MqService mqService,
                        UserService userService) {
        this.factory = factory;
        this.userService = userService;
        mqService.registerListener(this);
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage()) {
            try {
                sendMsg(factory.getCommand(update.getMessage()).processMessage((update.getMessage())));
            } catch (Exception e) {
                log.error("Exception: " + e.toString());
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

    @Override
    public void processPath(PathResponseExchange response) {


        StringBuilder builder = new StringBuilder("Путь:\n");

        List<RelationExchange> path = response.getPath();
        if (path.isEmpty()) {
            sendMsg(new SendMessage(response.getResponseTo().toString(), builder.append("не найден").toString()));
            return;
        }

        path.forEach(part ->
            builder.append(userService.findById(part.getPersonFrom()).getFio()).append("\n")
                .append(part.getRelationType().getValue()).append("\n")
        );
        builder.append(userService.findById(path.get(path.size() - 1).getPersonTo()).getFio());

        sendMsg(new SendMessage(response.getResponseTo().toString(), builder.toString()));
    }
}
