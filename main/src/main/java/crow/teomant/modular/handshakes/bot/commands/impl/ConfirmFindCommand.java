package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.bot.utils.CommandUtils;
import crow.teomant.modular.handshakes.user.domain.model.Path;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.rest.service.RestService;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class ConfirmFindCommand implements Command {

    private final UserService userService;
    private final RestService restService;
    private final BotState botState;

    @Override
    public SendMessage processMessage(Message message) {

        Map<Integer, User> options = botState.getFindOptions(message);

        if (StringUtils.isNumeric(message.getText()) && options.containsKey(Integer.parseInt(message.getText()))) {

            botState.removeFindOptions(message);

            int selected = Integer.parseInt(message.getText());

            User currentuser = userService.findByTelegramId(message.getFrom().getId())
                .orElseThrow(IllegalStateException::new);

            List<Path> path = restService.getPath(currentuser.getId(), options.get(selected).getId());

            StringBuilder builder = new StringBuilder("Путь до пользователя " + options.get(selected).getFio()
                + ":\n");

            if (path.isEmpty()) {
                return new SendMessage(message.getChatId().toString(), builder.append("не найден").toString());
            }

            path.forEach(part ->
                builder.append(part.getFrom().getFio()).append("\n").append(part.getRelation().getValue()).append("\n")
            );
            builder.append(path.get(path.size() - 1).getTo().getFio());

            return new SendMessage(message.getChatId().toString(), builder.toString());
        } else {

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        }
    }
}
