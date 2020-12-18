package crow.teomant.modular.handshakes.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {

    SendMessage processMessage(Message message);
}
