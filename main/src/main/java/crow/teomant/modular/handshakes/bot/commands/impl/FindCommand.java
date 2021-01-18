package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.bot.utils.CommandUtils;
import crow.teomant.modular.handshakes.user.domain.exchange.service.MqService;
import crow.teomant.modular.handshakes.user.domain.model.Path;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.exchange.service.RestService;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class FindCommand implements Command {

    private final UserService userService;
    private final MqService mqService;
    private final BotState botState;

    @Override
    public SendMessage processMessage(Message message) {

        String name = StringUtils.substringAfterLast(message.getText(), "/find").trim();

        if (name.isEmpty()) {
            return new SendMessage(message.getChatId().toString(),
                "Для поиска пользователя необходимо прислать фамилию имя и отчество через пробел");
        }

        HashMap<Integer, String> partsMap = CommandUtils.getPartsMap(name);

        List<User> existing = userService.findByCriteria(CommandUtils.getCriteriaFromParts(partsMap));

        if (existing.isEmpty()) {
            return new SendMessage(message.getChatId().toString(), "Пользователь не найден");
        }

        if (existing.size() == 1) {

            User currentuser = userService.findByTelegramId(message.getFrom().getId())
                .orElseThrow(IllegalStateException::new);

//            List<Path> path = restService.getPath(currentuser.getId(), existing.get(0).getId());
//
//            StringBuilder builder = new StringBuilder("Путь до пользователя " + existing.get(0).getFio() + ":\n");
//
//            if (path.isEmpty()) {
//                return new SendMessage(message.getChatId().toString(), builder.append("не найден").toString());
//            }
//
//            path.forEach(part ->
//                builder.append(part.getFrom().getFio()).append("\n").append(part.getRelation().getValue()).append("\n")
//            );
//            builder.append(path.get(path.size() - 1).getTo().getFio());
//
//            return new SendMessage(message.getChatId().toString(), builder.toString());

            mqService.getPath(currentuser.getId(), existing.get(0).getId(), message.getChatId());

            return new SendMessage(message.getChatId().toString(), "Путь ищется, подождите...");
        } else {
            HashMap<Integer, User> options = CommandUtils.getOptions(existing);

            botState.addFindOptions(message, options);

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        }
    }
}
