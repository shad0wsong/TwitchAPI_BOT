package myBot;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.sql.SQLException;


public class GameSubscribe extends BotCommand {

    ParseClass parseClass=new ParseClass();
    StatementClass statementClass = new StatementClass();
    public GameSubscribe(String commandIdentifier, String description) throws SQLException {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String gameName=parseClass.gameNameParse(strings);
        String games= null;
        try {
            games = statementClass.getGamesByChatId(String.valueOf(chat.getId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        games+=gameName+"/";
        statementClass.updateGameSub(String.valueOf(chat.getId()),games);
       // String[] gameNames;
       // gameNames=ParseClass.gameParse(games);
        //for (int i =0;i<gameNames.length;i++){
       //     System.out.println(gameNames[i]);
       // }
    }
}
