package finalproject.ninegag.model.dao;

import finalproject.ninegag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;

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


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
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
}
