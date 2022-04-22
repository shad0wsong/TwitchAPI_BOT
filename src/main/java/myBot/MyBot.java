package myBot;

;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class MyBot extends TelegramLongPollingCommandBot {
    private String username="@c00lboy_bot";
    private String token=System.getenv("COOL_BOT_TOKEN");

    public MyBot() {
        register(new GetToken("gettoken","get twitch token to use bot.Use your twitch nickname as an argument.Example:/gettoken forsen"));
        register(new GetTopGameClips("gettopgameclips","get top view clips by  game name.Use name of the game as an argument.Example:/gettopgameclips VALORANT"));
        register(new HelpCommand());
        register(new GetTopUserClips("gettopuserclips","get top view clips by user nickname.Use nickname of user as an argument.Example:/gettopuserclips forsen "));
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    @Override
    public String getBotToken() {
        return token;
    }
}


