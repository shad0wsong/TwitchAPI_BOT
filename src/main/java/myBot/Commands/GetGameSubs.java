package myBot.Commands;

import myBot.DBClasses.StatementClass;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class GetGameSubs extends BotCommand {
    StatementClass statementClass = new StatementClass();

    public GetGameSubs(String commandIdentifier, String description) throws SQLException {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String gameString="";
        try {
           gameString = statementClass.getGamesByChatId(String.valueOf(chat.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            absSender.execute(new SendMessage(String.valueOf(chat.getId()),gameString));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
