package myBot;

;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.Clip;
import com.github.twitch4j.helix.domain.ClipList;
import com.github.twitch4j.helix.domain.GameList;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


@Component
public class MyBot extends TelegramLongPollingCommandBot {

    @Autowired
    RequestForToken requestForToken;

    StatementClass statementClass = new StatementClass();
    ParseClass parseClass = new ParseClass();

    public static String accessAppToken;
    private String username = "@c00lboy_bot";
    private String token = System.getenv("COOL_BOT_TOKEN");

    public MyBot() throws SQLException {
        register(new StartCommand("start", ""));
        //register(new GetToken("getusertoken","get twitch token to use spicific bot operations.Use your twitch nickname as an argument.Example:/gettoken forsen"));
        register(new GetTopGameClips("topgameclips", "get top view clips by  game name.Use name of the game as an argument.Example:/gettopgameclips Apex Legends"));
        register(new HelpCommand());
        register(new GetTopUserClips("topuserclips", "get top view clips by user nickname.Use nickname of user as an argument.Example:/topuserclips forsen "));
        register(new GameSubscribe("gamesub", "subscribe for getting top clips of game every day."));
        register(new GameUnsubscribe("gameunsub","unsubscribe from getting daily clips"));
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        try {
            execute(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Sorry," + update.getMessage().getFrom().getUserName() + ".I don't understand simple words.Use /help to see my commands."));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Scheduled(fixedRate = 4000000)
    public void getAppToken() throws IOException, ParseException {
        String getContent = requestForToken.getAccessAppToken();
        System.out.println("Gained App Token");
        System.out.println(getContent);
        JSONParser jsonParser = new JSONParser(getContent);
        LinkedHashMap<String, Object> values = jsonParser.parseObject();
        String accessAppToken = (String) values.get("access_token");
        this.accessAppToken = accessAppToken;
    }

    @Scheduled(fixedRate = 40000)
    public void getСlipsByTime() throws TelegramApiException, SQLException {
        List<String> chatIdlist = statementClass.getAllChatId();
        for (String chatId : chatIdlist) {
            String gamesString = statementClass.getGamesByChatId(chatId);
            String[] games = parseClass.gameParse(gamesString);
            for (int i = 0; i < games.length; i++) {

                final String[] gameId = {""};
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Instant now = Instant.now();
                Instant oldDate = now.minusMillis(86400000);
                Date date = new Date(System.currentTimeMillis());
                execute(new SendMessage(chatId, "Today:" + date));

                TwitchClient twitchClient = TwitchClientBuilder.builder()
                        .withEnableHelix(true)
                        .build();

                GameList resultList = twitchClient.getHelix().getGames(MyBot.accessAppToken, null, Arrays.asList(games[i])).execute();
                resultList.getGames().forEach(game -> {
                    gameId[0] = game.getId();
                });

                ClipList clipList = twitchClient.getHelix().getClips(MyBot.accessAppToken, null, gameId[0], null, null, null, 10, oldDate, now).execute();
                Comparator<Clip> viewComparator = (o1, o2) -> o1.getViewCount().compareTo(o2.getViewCount());
                clipList.getData().sort(viewComparator);

                List<SendMessage> messageList = new ArrayList<>();
                clipList.getData().forEach(clip -> messageList.add(new SendMessage(chatId, "Clip owner:" + clip.getCreatorName() + "\n" + "Views:" + clip.getViewCount() + "\n" + "URL: " + clip.getUrl()))
                );
                for (SendMessage msg : messageList) {
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}


