package crow.teomant.modular.handshakes.bot.commands;

import crow.teomant.modular.handshakes.bot.commands.impl.AddMemberCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.AddRelationCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.CancelCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.ChooseFromCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.ChooseToCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.ConfirmAddMemberCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.ConfirmFindCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.ConfirmRegistrationCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.FindCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.ForgetMeCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.InfoCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.RegisterCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.StartCommand;
import crow.teomant.modular.handshakes.bot.commands.impl.UnknownCommand;
import crow.teomant.modular.handshakes.user.domain.exchange.service.MqService;
import crow.teomant.modular.handshakes.user.domain.exchange.service.RestService;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class HandshakeBotCommandFactory {

    private final UserService userService;
    private final RestService restService;
    private final MqService mqService;

    private final BotState botState = new BotState();

    public Command getCommand(Message message) {
        if (message.getText().startsWith("/cancel")) {
            return new CancelCommand(botState);
        }
        if (botState.isRegistrationInProcess(message)) {
            return new ConfirmRegistrationCommand(userService, botState);
        }
        if (botState.isAddInProcess(message)) {
            return new ConfirmAddMemberCommand(userService, botState);
        }
        if (botState.isFindInProcess(message)) {
            return new ConfirmFindCommand(userService, mqService, botState);
        }
        if (botState.getRelationCommandState().isChoosingFrom(message)) {
            return new ChooseFromCommand(userService, botState.getRelationCommandState());
        }
        if (botState.getRelationCommandState().isChoosingTo(message)) {
            return new ChooseToCommand(userService, botState.getRelationCommandState());
        }
        if (message.getText().startsWith("/register")) {
            return new RegisterCommand(userService, botState);
        }
        if (message.getText().startsWith("/forgetme")) {
            return new ForgetMeCommand(botState, userService);
        }
        if (message.getText().startsWith("/addmember")) {
            return new AddMemberCommand(userService, botState);
        }
        if (message.getText().startsWith("/addrelation")) {
            return new AddRelationCommand(userService, botState.getRelationCommandState());
        }
        if (message.getText().startsWith("/info")) {
            return new InfoCommand(userService);
        }
        if (message.getText().startsWith("/find")) {
            return new FindCommand(userService, mqService, botState);
        }
        if (message.getText().startsWith("/start")
            || !userService.findByTelegramId(message.getFrom().getId()).isPresent()) {
            return new StartCommand();
        }

        return new UnknownCommand();
    }
}
