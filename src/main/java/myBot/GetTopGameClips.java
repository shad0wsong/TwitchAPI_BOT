package myBot;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.Clip;
import com.github.twitch4j.helix.domain.ClipList;
import com.github.twitch4j.helix.domain.GameList;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GetTopGameClips extends BotCommand {
    ParseClass parseClass = new ParseClass();
    public GetTopGameClips(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        final String[] gameId = {""};
        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .build();
        String gameName=parseClass.gameNameParse(strings);
        GameList resultList = twitchClient.getHelix().getGames(MyBot.accessAppToken,null, Arrays.asList(gameName)).execute();
        resultList.getGames().forEach(game -> {
             gameId[0] =game.getId();
        });

        ClipList clipList = twitchClient.getHelix().getClips(MyBot.accessAppToken, null, gameId[0], null, null, null, 10, null,null).execute();
        Comparator<Clip> viewComparator = (o1, o2) -> o1.getViewCount().compareTo(o2.getViewCount());
        clipList.getData().sort(viewComparator);

        List<SendMessage> messageList =new ArrayList<>();
        clipList.getData().forEach(clip -> messageList.add(new SendMessage(String.valueOf(chat.getId()),"Clip Owner:"+clip.getCreatorName()+"\n"+"Views:"+clip.getViewCount() +"\n"+"URL: "+clip.getUrl()))
        );
        for (SendMessage msg :messageList){
            try {
                absSender.execute(msg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
