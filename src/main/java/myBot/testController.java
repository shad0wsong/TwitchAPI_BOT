package myBot;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.Clip;
import com.github.twitch4j.helix.domain.ClipList;
import com.github.twitch4j.helix.domain.GameList;
import com.github.twitch4j.helix.domain.VideoList;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class testController {
    @Autowired
    RequestForToken requestForToken;

    static ClipList sharedCliplist;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String takeUserToken( HttpServletRequest request) throws IOException, ParseException {
        String getContent;
        String code = request.getParameter("code");
        getContent= requestForToken.getAccessToken(code);
        JSONParser jsonParser = new JSONParser(getContent);
        LinkedHashMap<String, Object> values =jsonParser.parseObject() ;
        String accessToken= (String) values.get("access_token");
        TwitchClient twitchClient = TwitchClientBuilder.builder()
               .withEnableHelix(true)
                .build();
        GameList resultList = twitchClient.getHelix().getGames(accessToken,null, Arrays.asList("overwatch")).execute();
        resultList.getGames().forEach(game -> {
            System.out.println("Game ID: " + game.getId() + " is " + game.getName());
        });
        ClipList clipList = twitchClient.getHelix().getClips(accessToken, null, "488552", null, null, null, 10, null,null).execute();
        Comparator<Clip> viewComparator = (o1, o2) -> o1.getViewCount().compareTo(o2.getViewCount());

        clipList.getData().sort(viewComparator);
        this.sharedCliplist=clipList;
        clipList.getData().forEach(clip -> System.out.println("Found Clip:"+clip.getCreatorName()+"\n"+"Views:"+clip.getViewCount() +"\n"+"URL: "+clip.getUrl()));
        return "Token granted,you can return to bot";
    }
    @GetMapping("/test")
    public void test(){
        System.out.println("test");
    }

}
