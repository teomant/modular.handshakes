package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class InfoCommand implements Command {

    private final UserService userService;

    @Override
    public SendMessage processMessage(Message message) {

        Optional<User> user = userService.findByTelegramId(message.getFrom().getId());

        return new SendMessage(message.getChatId().toString(), user.map(User::getFio).orElseGet(() -> "Неизвестно"));
    }
}
