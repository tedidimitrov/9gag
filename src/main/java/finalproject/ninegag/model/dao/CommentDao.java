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
import java.util.Optional;

@Component
public class CommentDao{


    public static final String SELECT_UPVOTED_COMMENT_BY_USER_ID =
            "SELECT FROM users_upvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String SELECT_DOWNVOTED_COMMENT_BY_USER_ID =
            "SELECT FROM users_downvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String DELETE_UPVOTED_COMMENT_BY_USER_ID =
            "DELETE FROM users_upvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String DELETE_DOWNVOTED_COMMENT_BY_USER_ID =
            "DELETE FROM users_downvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String INSERT_INTO_UPVOTED =
            "INSERT INTO users_upvoted_comments (user_id, comment_id) VALUES (?, ?)";

    public static final String INSERT_INTO_DOWNVOTED =
            "INSER INTO users_downvoted_comments (user_id, comment_id) VALUES (?, ?)";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void upvoteComment(User u, Comment c) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            if (isCommentUpvoted(u, c)) {
                String removeFromUpvotes = DELETE_UPVOTED_COMMENT_BY_USER_ID;
                try(PreparedStatement statement = connection.prepareStatement(removeFromUpvotes);) {
                    statement.setLong(1, u.getId());
                    statement.setLong(2, c.getId());
                    statement.executeUpdate();
                }
            } else {
                try {
                    connection.setAutoCommit(false);
                    if (isCommentDownvoted(u, c)) {
                        String removeFromDownvotes = DELETE_DOWNVOTED_COMMENT_BY_USER_ID;
                        try (PreparedStatement statement = connection.prepareStatement(removeFromDownvotes);) {
                            statement.setLong(1, u.getId());
                            statement.setLong(2, c.getId());
                            statement.executeUpdate();
                        }
                    }
                    String upvote = INSERT_INTO_UPVOTED;
                    try (PreparedStatement statement = connection.prepareStatement(upvote);) {
                        statement.setLong(1, u.getId());
                        statement.setLong(2, c.getId());
                        statement.executeUpdate();
                    }
                    connection.commit();
                    connection.setAutoCommit(true);
                }
                catch (SQLException e){
                    connection.rollback();
                    throw new SQLException("The comment wasn't upvoted!", e);
                }
            }
        }
    }

    public void downvoteComment(User u, Comment c) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            if (isCommentDownvoted(u, c)) {
                String removeFromDownvotes = DELETE_DOWNVOTED_COMMENT_BY_USER_ID ;
                try(PreparedStatement statement = connection.prepareStatement(removeFromDownvotes);) {
                    statement.setLong(1, u.getId());
                    statement.setLong(2, c.getId());
                    statement.executeUpdate();
                }
            } else {
                try {
                    connection.setAutoCommit(false);
                    if (isCommentUpvoted(u, c)) {
                        String removeFromUpvotes = DELETE_UPVOTED_COMMENT_BY_USER_ID;
                        try(PreparedStatement statement = connection.prepareStatement(removeFromUpvotes);) {
                            statement.setLong(1, u.getId());
                            statement.setLong(2, c.getId());
                            statement.executeUpdate();
                        }
                    }
                    String downvote = INSERT_INTO_DOWNVOTED;
                    try(PreparedStatement statement = connection.prepareStatement(downvote);) {
                        statement.setLong(1, u.getId());
                        statement.setLong(2, c.getId());
                        statement.executeUpdate();

                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                }catch (SQLException e){
                    connection.rollback();
                    throw new SQLException("This comments wasn't downvoted!" , e);
                }
            }
        }
    }


    private boolean isCommentUpvoted(User u, Comment c) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
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
