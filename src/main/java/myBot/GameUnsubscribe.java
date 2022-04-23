package myBot;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameUnsubscribe extends BotCommand {
    StatementClass statementClass=new StatementClass();
    ParseClass parseClass = new ParseClass();
    public GameUnsubscribe(String commandIdentifier, String description) throws SQLException {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String gameName=parseClass.gameNameParse(strings);
        String gameString;
        try {
             gameString=statementClass.getGamesByChatId(String.valueOf(chat.getId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String[] games=parseClass.gameParse(gameString);
        List<String> gameList=new ArrayList<>();
        for (int i=0;i<games.length;i++){
            gameList.add(games[i]);
        }
        if(gameList.contains(gameName)){
            gameList.remove(gameName);
            if(gameList.size()!=0) {
                String newGameString = gameList.get(0);
                for (int i = 1; i < gameList.size(); i++) {
                    newGameString += gameList.get(i) + "/";
                }
                statementClass.updateGameSub(String.valueOf(chat.getId()), newGameString);
            }
            if(gameList.size()==0){
                statementClass.updateGameSub(String.valueOf(chat.getId()),"");
            }

        }
        else {
            try {
                absSender.execute(new SendMessage(String.valueOf(chat.getId()),"No such game in subscriptions"));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
