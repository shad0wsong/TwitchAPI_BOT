package myBot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.ClipList;
import com.github.twitch4j.helix.domain.VideoList;
;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;


@Component
public class MyBot extends TelegramLongPollingCommandBot {
    private String username="@c00lboy_bot";
    private String token=System.getenv("COOL_BOT_TOKEN");

    public MyBot() {
        register(new GetToken("gettoken","get twitch token to use bot"));
        register(new GetTopOverWatchClips("gettopovewatchclips","get top overwatch clips"));
        register(new HelpCommand());
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


