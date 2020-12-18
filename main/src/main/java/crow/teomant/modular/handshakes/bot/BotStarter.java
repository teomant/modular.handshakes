package crow.teomant.modular.handshakes.bot;

import crow.teomant.modular.handshakes.bot.commands.HandshakeBotCommandFactory;
import crow.teomant.modular.handshakes.user.domain.rest.service.RestService;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class BotStarter {

    private final UserService userService;
    private final RestService restService;

    @PostConstruct
    public void init() throws TelegramApiException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new HandShakeBot(new HandshakeBotCommandFactory(userService, restService)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
