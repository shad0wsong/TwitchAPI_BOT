package myBot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatementClass {
    MyDBConnection myDBConnection = new MyDBConnection();
    Connection connection = myDBConnection.getConnection();

    public StatementClass() throws SQLException {
    }

    public  String getGamesByChatId(String chatid) throws SQLException {
        String games="";
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT games FROM subscriber WHERE chat_id=?");
            preparedStatement.setString(1, chatid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                games=resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return games;
    }
    public void updateGameSub(String chatid,String games){
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE subscriber SET games=? WHERE chat_id=?");
            preparedStatement.setString(1, games);
            preparedStatement.setString(2, chatid);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllChatId() throws SQLException {
        List<String> chatIdList=new ArrayList<>();
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT chat_id FROM subscriber");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            chatIdList.add(resultSet.getString(1));
        }
        return chatIdList;
    }
}
