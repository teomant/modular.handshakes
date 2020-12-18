package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.bot.utils.CommandUtils;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.model.UserCriteria;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class RegisterCommand implements Command {

    private final UserService userService;
    private final BotState botState;

    @Override
    public SendMessage processMessage(Message message) {

        Optional<User> thisUser = userService.findByTelegramId(message.getFrom().getId());
        if (thisUser.isPresent()) {
            return new SendMessage(message.getChatId().toString(),
                "Приветствую, " + thisUser.get().getFio() + "! Вы уже зарегистрированы!");
        }


        String name = StringUtils.substringAfterLast(message.getText(), "/register").trim();

        if (name.isEmpty()) {
            return new SendMessage(message.getChatId().toString(),
                "Для регистрации необходимо прислать фамилию имя и отчество через пробел");
        }

        HashMap<Integer, String> partsMap = CommandUtils.getPartsMap(name);

        List<User> existing = userService.findByCriteria(CommandUtils.getCriteriaFromParts(partsMap));

        User user = CommandUtils.getUserFromParts(partsMap);
        user.setTelegramId(message.getFrom().getId());

        if (existing.isEmpty()) {
            userService.save(user);
            return new SendMessage(message.getChatId().toString(), "Приветствую, " + user.getFio() + "!");

        } else {

            HashMap<Integer, User> options = CommandUtils.getOptions(existing, user);

            botState.addRegistrationOptions(message, options);

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        }
    }
}
