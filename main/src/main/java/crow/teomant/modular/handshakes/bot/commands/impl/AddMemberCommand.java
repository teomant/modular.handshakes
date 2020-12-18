package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.bot.utils.CommandUtils;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class AddMemberCommand implements Command {

    private final UserService userService;
    private final BotState botState;

    @Override
    public SendMessage processMessage(Message message) {

        String name = StringUtils.substringAfterLast(message.getText(), "/addmember").trim();

        if (name.isEmpty()) {
            return new SendMessage(message.getChatId().toString(),
                "Для добавления пользователя необходимо прислать фамилию имя и отчество через пробел");
        }

        HashMap<Integer, String> partsMap = CommandUtils.getPartsMap(name);

        List<User> existing = userService.findByCriteria(CommandUtils.getCriteriaFromParts(partsMap));

        User user = CommandUtils.getUserFromParts(partsMap);

        if (existing.isEmpty()) {
            userService.save(user);
            return new SendMessage(message.getChatId().toString(), user.getFio() + " добавлен!");

        } else {

            HashMap<Integer, User> options = CommandUtils.getOptions(existing, user);

            botState.addAddOptions(message, options);

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        }
    }
}
