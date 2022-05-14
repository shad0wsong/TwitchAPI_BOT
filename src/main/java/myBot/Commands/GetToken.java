package myBot.Commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

;import java.util.HashMap;
import java.util.Map;

public class GetToken extends BotCommand {
     static Map<String,String> usermap = new HashMap<>();
    public GetToken(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            usermap.put(strings[0],null);
            absSender.execute(new SendMessage(String.valueOf(chat.getId()),"https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=vmb6s9rnuweobyejtlojxywl0d714w&redirect_uri=http://localhost:5000&scope=channel%3Amanage%3Apolls+channel%3Aread%3Apolls&state=c3ab8aa609ea11e793ae92361f002671"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
