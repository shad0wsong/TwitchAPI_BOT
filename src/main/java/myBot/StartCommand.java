package myBot;

import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommand extends BotCommand {
    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            absSender.execute(new SendMessage(String.valueOf(chat.getId()),"Hi,"+user.getUserName()+".I am bot for twitch. I can get clips or tell you when your favorite streamer is online.\n You can see my commands using /help"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
