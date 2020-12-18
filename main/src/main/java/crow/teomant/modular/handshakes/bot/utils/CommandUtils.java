package crow.teomant.modular.handshakes.bot.utils;

import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.model.UserCriteria;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandUtils {


    public static HashMap<Integer, String> getPartsMap(String name) {
        String[] parts = name.split(" ");

        HashMap<Integer, String> partsMap = new HashMap<>();

        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.stream(parts).forEach(part ->
            partsMap.put(atomicInteger.getAndIncrement(), part)
        );
        return partsMap;
    }

    public static UserCriteria getCriteriaFromParts(HashMap<Integer, String> partsMap) {
        return UserCriteria.of(
            partsMap.getOrDefault(1, ""),
            partsMap.getOrDefault(2, ""),
            partsMap.getOrDefault(0, ""));
    }

    public static User getUserFromParts(HashMap<Integer, String> partsMap) {
        User user = new User();
        user.setLastname(partsMap.getOrDefault(0, ""));
        user.setFirstname(partsMap.getOrDefault(1, ""));
        user.setPatronymic(partsMap.getOrDefault(2, ""));

        return user;
    }

    public static HashMap<Integer, User> getOptions(List<User> existing, User user) {
        AtomicInteger number = new AtomicInteger(1);
        HashMap<Integer, User> options = new HashMap<>();
        existing.forEach(existingUser -> options.put(number.getAndIncrement(), existingUser));
        options.put(0, user);
        return options;
    }

    public static HashMap<Integer, User> getOptions(List<User> existing) {
        AtomicInteger number = new AtomicInteger(1);
        HashMap<Integer, User> options = new HashMap<>();
        existing.forEach(existingUser -> options.put(number.getAndIncrement(), existingUser));
        return options;
    }

    public static String getOptionsMessage(Map<Integer, User> options) {
        StringBuilder builder = new StringBuilder("В базе найдены пользователи, подходящие под "
            + "указанные вами ФИО, выберите один из вариантов:\n");

        options.entrySet().stream()
            .filter(entry -> entry.getKey() != 0)
            .forEach(entry -> builder.append(entry.getKey() + ") " + entry.getValue().getFio() + "\n"));

        if (options.containsKey(0)) {
            builder.append("0) Добавить нового пользователя\n");
        }

        builder.append("Или отправьте команду /cancel для отмены");

        return builder.toString();
    }
}
