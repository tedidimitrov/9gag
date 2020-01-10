package finalproject.ninegag.model.dao;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.model.pojo.Category;
import finalproject.ninegag.model.pojo.Post;
import finalproject.ninegag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO {

    private static final String REGISTER_USER_SQL = "INSERT INTO users (" +
            "user_name," +
            "first_name," +
            "last_name," +
            "email," +
            "password," +
            "date_registered)" +
            " VALUES (?,?,?,?,?,?);";
    private static final String SELECT_USER_BY_USERNAME = "SELECT " +
            "id," +
            "user_name," +
            "first_name," +
            "last_name," +
            "email," +
            "password," +
            "date_registered" +
            " FROM users" +
            " WHERE user_name = ?;";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUser_name());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setTimestamp(6, Timestamp.valueOf(user.getDateRegistered()));
            statement.execute();
            ResultSet set = statement.getGeneratedKeys();
            set.next();
            user.setId(set.getLong(1));
        }
    }

    public User getByUserName(String username) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_USERNAME, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1,username);
            statement.executeQuery();
            ResultSet rows = statement.executeQuery();
            if(rows.next()) {
//                return new User(
//                        rows.getLong("id"),
//                        rows.getString("user_name"),
//                        rows.getString("first_name"),
//                        rows.getString("last_name"),
//                        rows.getString("email"),
//                        rows.getString("password"),
//                        rows.getTimestamp("date_registered").toLocalDateTime());
                return null;
            }
            else{
                return null;
            }
        }
    }
}
