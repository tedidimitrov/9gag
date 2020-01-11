package finalproject.ninegag.model.dao;

import finalproject.ninegag.model.entity.Comment;
import finalproject.ninegag.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CommentDao{


    public static final String SELECT_UPVOTED_COMMENT_BY_USER_ID =
            "SELECT user_id, comment_id FROM users_upvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String SELECT_DOWNVOTED_COMMENT_BY_USER_ID =
            "SELECT user_id, comment_id FROM users_downvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String DELETE_UPVOTED_COMMENT_BY_USER_ID =
            "DELETE FROM users_upvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String DELETE_DOWNVOTED_COMMENT_BY_USER_ID =
            "DELETE FROM users_downvoted_comments WHERE user_id = ? AND comment_id = ?";

    public static final String INSERT_INTO_UPVOTED =
            "INSERT INTO users_upvoted_comments (user_id, comment_id) VALUES (?, ?)";

    public static final String INSERT_INTO_DOWNVOTED =
            "INSERT INTO users_downvoted_comments (user_id, comment_id) VALUES (?, ?)";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void upvoteComment(User user, Comment comment) throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            if (isCommentUpvoted(user, comment)) {
                try (PreparedStatement statement = connection.prepareStatement(DELETE_UPVOTED_COMMENT_BY_USER_ID);) {
                    statement.setLong(1, user.getId());
                    statement.setLong(2, comment.getId());
                    statement.executeUpdate();
                }
            } else {
                    connection.setAutoCommit(false);
                    if (isCommentDownvoted(user, comment)) {
                        try (PreparedStatement statement = connection.prepareStatement(DELETE_DOWNVOTED_COMMENT_BY_USER_ID);) {
                            {
                                statement.setLong(1, user.getId());
                                statement.setLong(2, comment.getId());
                                statement.executeUpdate();
                            }
                        }
                        try (PreparedStatement statement = connection.prepareStatement(INSERT_INTO_UPVOTED)) {
                                statement.setLong(1, user.getId());
                                statement.setLong(2, comment.getId());
                                statement.executeUpdate();
                            connection.commit();
                            connection.setAutoCommit(true);
                        } catch (SQLException e) {
                            connection.rollback();
                            throw new SQLException("The comment wasn't upvoted!", e);
                        }
                    }
                }
            }
        }

    public void downvoteComment(User user, Comment comment) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            if (isCommentDownvoted(user, comment)) {
                try(PreparedStatement statement = connection.prepareStatement(DELETE_DOWNVOTED_COMMENT_BY_USER_ID);) {
                    statement.setLong(1, user.getId());
                    statement.setLong(2, comment.getId());
                    statement.executeUpdate();
                }
            } else {
                try {
                    connection.setAutoCommit(false);
                    if (isCommentUpvoted(user, comment)) {
                        try(PreparedStatement statement = connection.prepareStatement(DELETE_UPVOTED_COMMENT_BY_USER_ID);) {
                            statement.setLong(1, user.getId());
                            statement.setLong(2, comment.getId());
                            statement.executeUpdate();
                        }
                    }
                    try(PreparedStatement statement = connection.prepareStatement(INSERT_INTO_DOWNVOTED);) {
                        statement.setLong(1, user.getId());
                        statement.setLong(2, comment.getId());
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

    public long getPoints(Comment comment) throws SQLException{
        String sql = "SELECT COUNT(uuc.comment_id) AS upvoted, COUNT(udv.comment_id) as downvoted " +
                "FROM users_downvoted_comments AS udv " +
                "RIGHT JOIN final_project.comments AS c ON udv.comment_id = id " +
                "LEFT JOIN users_upvoted_comments AS uuc ON uuc.comment_id = id;";
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            long x = resultSet.getInt("upvoted");
            long y = resultSet.getInt("downvoted");
            return x - y;
        }
    }

    private boolean isCommentUpvoted(User user, Comment comment) throws SQLException {
                try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_UPVOTED_COMMENT_BY_USER_ID)) {
                        statement.setLong(1, user.getId());
                        statement.setLong(2, comment.getId());

                        ResultSet resultSet = statement.executeQuery();
                        return resultSet.next();
                    }
    }

    private boolean isCommentDownvoted(User user, Comment comment) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_DOWNVOTED_COMMENT_BY_USER_ID)) {
            statement.setLong(1, user.getId());
            statement.setLong(2, comment.getId());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }



}
