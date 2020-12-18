package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.bot.utils.CommandUtils;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class ConfirmAddMemberCommand implements Command {

    private final UserService userService;
    private final BotState botState;

    @Override
    public SendMessage processMessage(Message message) {

        Map<Integer, User> options = botState.getAddOptions(message);

        if (StringUtils.isNumeric(message.getText()) && options.containsKey(Integer.parseInt(message.getText()))) {

            int selected = Integer.parseInt(message.getText());
            User user;

            if (selected == 0) {
                user = userService.save(options.get(selected));
            } else {
                user = options.get(selected);

                User insertedUser = options.get(0);

                user.setLastname(insertedUser.getLastname());
                user.setFirstname(insertedUser.getFirstname());
                user.setPatronymic(insertedUser.getPatronymic());

                userService.update(user);
            }

            botState.removeAddOptions(message);

            return new SendMessage(message.getChatId().toString(), user.getFio() + " добавлен!");
        } else {

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        }
    }
}
