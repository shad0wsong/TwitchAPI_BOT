package myBot;

import com.github.twitch4j.helix.domain.ClipList;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class GetTopOverWatchClips extends BotCommand {
    public GetTopOverWatchClips(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        List<SendMessage> messageList =new ArrayList<>();
        ClipList clipList=testController.sharedCliplist;
        clipList.getData().forEach(clip -> messageList.add(new SendMessage(String.valueOf(chat.getId()),"Found Clip:"+clip.getCreatorName()+"\n"+"Views:"+clip.getViewCount() +"\n"+"URL: "+clip.getUrl()))
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
