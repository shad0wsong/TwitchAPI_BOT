package myBot;

public class ParseClass {

    public String gameNameParse(String[] strings){
        String gameName=strings[0];
        for (int i =1;i<strings.length;i++){
            gameName+=" "+strings[i];
        }
        return gameName;
    }
    public String[] gameParse(String st){
        String[] subStr;
        String delimeter = "/";
        subStr = st.split(delimeter);
        return subStr;
    }
}
