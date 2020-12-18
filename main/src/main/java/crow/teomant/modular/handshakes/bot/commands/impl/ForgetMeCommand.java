package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class ForgetMeCommand implements Command {

    private final BotState botState;
    private final UserService userService;

    @Override
    public SendMessage processMessage(Message message) {

        userService.findByTelegramId(message.getFrom().getId())
            .ifPresent(user -> {
                    user.setTelegramId(null);
                    userService.update(user);
                }
            );

        botState.cancel(message);
        return new SendMessage(message.getChatId().toString(), "Ок");
    }
}
