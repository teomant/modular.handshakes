package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.user.domain.model.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class StartCommand implements Command {

    @Override
    public SendMessage processMessage(Message message) {

        String text = "Добро пожаловать! Отправьте команду /register со своими ФИО через пробел"
            + " ('/register Фамилия Имя Отчество') для регистрации в боте";

        return new SendMessage(message.getChatId().toString(), text);
    }
}
