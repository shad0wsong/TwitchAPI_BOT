package myBot;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RequestForToken {

    public String getAccessUserToken(String code) throws IOException {
        String getContent="";
        final URL url = new URL("https://id.twitch.tv/oauth2/token");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("client_id", System.getenv("CLIENT_ID"));
        parameters.put("client_secret", System.getenv("CLIENT_SECRET"));
        parameters.put("code", code);
        parameters.put("grant_type", "authorization_code");

        con.setDoOutput(true);

        final DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(getParamsString(parameters));
        out.flush();
        out.close();

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            getContent=content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return getContent;
    }

    public String getParamsString(final Map<String, String> params) {
        final StringBuilder result = new StringBuilder();

        params.forEach((name, value) -> {
            try {
                result.append(URLEncoder.encode(name, "UTF-8"));
                result.append('=');
                result.append(URLEncoder.encode(value, "UTF-8"));
                result.append('&');
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        final String resultString = result.toString();
        return (!resultString.isEmpty()
                ? resultString.substring(0, resultString.length() - 1)
                : resultString)+"&redirect_uri=http://localhost:5000";
    }

    public static String getParamsAppString(final Map<String, String> params) {
        final StringBuilder result = new StringBuilder();

        params.forEach((name, value) -> {
            try {
                result.append(URLEncoder.encode(name, "UTF-8"));
                result.append('=');
                result.append(URLEncoder.encode(value, "UTF-8"));
                result.append('&');
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        final String resultString = result.toString();
        return (!resultString.isEmpty()
                ? resultString.substring(0, resultString.length() - 1)
                : resultString);
    }

    public String getAccessAppToken() throws IOException {
        String getContent="";
        final URL url = new URL("https://id.twitch.tv/oauth2/token");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("client_id", System.getenv("CLIENT_ID"));
        parameters.put("client_secret", System.getenv("CLIENT_SECRET"));
        parameters.put("grant_type", "client_credentials");

        con.setDoOutput(true);

        final DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(getParamsAppString(parameters));
        out.flush();
        out.close();

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            getContent=content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return getContent;
    }
    public String takeUserToken( HttpServletRequest request) throws IOException, ParseException {
        String getContent;
        String code = request.getParameter("code");
        getContent=getAccessUserToken(code);
        JSONParser jsonParser = new JSONParser(getContent);
        LinkedHashMap<String, Object> values =jsonParser.parseObject() ;
        String accessToken= (String) values.get("access_token");

        return "Token granted,you can return to bot";
    }
}
