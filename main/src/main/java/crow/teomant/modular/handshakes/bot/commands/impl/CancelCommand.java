package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.user.domain.model.User;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class CancelCommand implements Command {

    private final BotState botState;

    @Override
    public SendMessage processMessage(Message message) {
        botState.cancel(message);
        return new SendMessage(message.getChatId().toString(), "Ок");
    }
}
