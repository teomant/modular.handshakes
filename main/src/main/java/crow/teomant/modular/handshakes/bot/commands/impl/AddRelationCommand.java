package crow.teomant.modular.handshakes.bot.commands.impl;

import crow.teomant.modular.handshakes.bot.commands.BotState;
import crow.teomant.modular.handshakes.bot.commands.Command;
import crow.teomant.modular.handshakes.bot.utils.CommandUtils;
import crow.teomant.modular.handshakes.user.domain.model.RelationType;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class AddRelationCommand implements Command {

    private final UserService userService;
    private final BotState.RelationCommandState relationCommandState;

    @Override
    public SendMessage processMessage(Message message) {

        String relation = StringUtils.substringAfterLast(message.getText(), "/addrelation").trim();

        SendMessage errorMessage = new SendMessage(message.getChatId().toString(),
            "Для добавления отношения необходимо прислать ФИО первого пользователя, тип отношения "
                + "и ФИО второго пользователя через пробелы. Типы отношений: "
                + Arrays.stream(RelationType.values())
                .map(RelationType::getValue)
                .collect(Collectors.joining(", "))
        );

        if (relation.isEmpty()) {
            return errorMessage;
        }

        Optional<RelationType> relationTypeOptional = Arrays.stream(RelationType.values())
            .filter(type -> relation.contains(" " + type.getValue() + " "))
            .max(Comparator.comparing(RelationType::getPriority));

        if (!relationTypeOptional.isPresent()) {
            return errorMessage;
        }

        RelationType relationType = relationTypeOptional.get();

        String[] split = relation.split(relationType.getValue());

        if (split.length != 2 || split[0].trim().isEmpty() || split[1].trim().isEmpty()) {
            return errorMessage;
        }

        HashMap<Integer, String> fromPartsMap = CommandUtils.getPartsMap(split[0].trim());

        List<User> existingFrom = userService.findByCriteria(CommandUtils.getCriteriaFromParts(fromPartsMap));

        User userFrom = CommandUtils.getUserFromParts(fromPartsMap);

        HashMap<Integer, String> toPartsMap = CommandUtils.getPartsMap(split[1].trim());

        List<User> existingTo = userService.findByCriteria(CommandUtils.getCriteriaFromParts(toPartsMap));

        User userTo = CommandUtils.getUserFromParts(toPartsMap);

        if (existingFrom.isEmpty() && existingTo.isEmpty()) {
            userService.addRelation(userService.save(userFrom).getId(), userService.save(userTo).getId(), relationType);
            return new SendMessage(message.getChatId().toString(),
                "Отношение добавлено!"
            );
        }

        if (!existingFrom.isEmpty() && !existingTo.isEmpty()) {
            relationCommandState.addRelation(message, relationType);

            HashMap<Integer, User> toOptions = CommandUtils.getOptions(existingTo, userTo);
            HashMap<Integer, User> fromOptions = CommandUtils.getOptions(existingFrom, userFrom);

            relationCommandState.addToOptions(message, toOptions);
            relationCommandState.addFromOptions(message, fromOptions);

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(fromOptions));
        }

        if (existingFrom.isEmpty()) {

            relationCommandState.addFrom(message, userFrom);
            relationCommandState.addRelation(message, relationType);

            HashMap<Integer, User> options = CommandUtils.getOptions(existingTo, userTo);

            relationCommandState.addToOptions(message, options);

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        } else {

            relationCommandState.addTo(message, userTo);
            relationCommandState.addRelation(message, relationType);

            HashMap<Integer, User> options = CommandUtils.getOptions(existingFrom, userFrom);

            relationCommandState.addFromOptions(message, options);

            return new SendMessage(message.getChatId().toString(), CommandUtils.getOptionsMessage(options));
        }
    }
}
