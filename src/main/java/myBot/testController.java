package myBot;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.*;
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

    static String token="";
    @Autowired
    RequestForToken requestForToken;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String takeUserToken( HttpServletRequest request) throws IOException, ParseException {
        String getContent;
        String code = request.getParameter("code");
        getContent= requestForToken.getAccessToken(code);
        JSONParser jsonParser = new JSONParser(getContent);
        LinkedHashMap<String, Object> values =jsonParser.parseObject() ;
        String accessToken= (String) values.get("access_token");
        this.token=accessToken;

        /*
        resultList.getUsers().forEach(user -> {
            if(GetToken.usermap.containsKey(user.getLogin())){
                GetToken.usermap.put(user.getLogin(),accessToken);
            }
            System.out.println(GetToken.usermap);
        });
        */


        return "Token granted,you can return to bot";
    }
    @GetMapping("/test")
    public void test(){
        System.out.println("test");
    }

}
