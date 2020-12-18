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
public class ChooseFromCommand implements Command {

    private final UserService userService;
    private final BotState.RelationCommandState relationCommandState;

    @Override
    public SendMessage processMessage(Message message) {

        Map<Integer, User> options = relationCommandState.getFromOptions(message);

        if (StringUtils.isNumeric(message.getText()) && options.containsKey(Integer.parseInt(message.getText()))) {

            int selected = Integer.parseInt(message.getText());
            User user = selected == 0 ? userService.save(options.get(selected)) : options.get(selected);

            if (relationCommandState.isChoosingTo(message)) {

                relationCommandState.addFrom(message, user);

                return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(
                    relationCommandState.getToOptions(message)
                ));
            } else {
                userService.addRelation(
                    user.getId(),
                    relationCommandState.getTo(message).getId(),
                    relationCommandState.getRelation(message)
                );
                relationCommandState.removeRelationsOptions(message);
                return new SendMessage(message.getChatId().toString(), "Отношение добавлено");
            }

        } else {

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        }
    }
}
