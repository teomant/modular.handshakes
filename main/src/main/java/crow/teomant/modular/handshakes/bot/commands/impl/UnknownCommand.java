package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.Command;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class UnknownCommand implements Command {

    @Override
    public SendMessage processMessage(Message message) {
        return new SendMessage(message.getChatId().toString(), "Неизвестная команда. Доступные команды: "
            + "/info, /addmember, /addrelation, /forgetme, /cancel");
    }
}
