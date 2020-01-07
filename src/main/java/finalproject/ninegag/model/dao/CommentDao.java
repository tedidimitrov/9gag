package finalproject.ninegag.model.dao;

import finalproject.ninegag.model.pojo.Comment;
import finalproject.ninegag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CommentDao{

    public static final String SELECT_UPVOTED_COMMENT_BY_USER_ID = "SELECT FROM users_upvoted_comments WHERE user_id = ? AND comment_id = ?";
    public static final String SELECT_DOWNVOTED_COMMENT_BY_USER_ID = "SELECT FROM users_downvoted_comments WHERE user_id = ? AND comment_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;



    private boolean isCommentUpvoted(User u, Comment c) throws SQLException {
        Connection connection =jdbcTemplate.getDataSource().getConnection();
        String sql = SELECT_UPVOTED_COMMENT_BY_USER_ID;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, u.getId());
            statement.setLong(2, c.getId());

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }

    private boolean isCommentDownvoted(User u, Comment c) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        String sql = SELECT_DOWNVOTED_COMMENT_BY_USER_ID;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, u.getId());
            statement.setLong(2, c.getId());

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }



}
