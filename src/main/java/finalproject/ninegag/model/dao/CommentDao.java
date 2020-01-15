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

    public static final String GET_POINTS_BY_COMMENT_ID =
            "SELECT COUNT(uuc.comment_id) AS upvoted, COUNT(udv.comment_id) as downvoted\n" +
            "FROM users_downvoted_comments AS udv\n" +
            "RIGHT JOIN final_project.comments AS c ON udv.comment_id = id\n" +
            "LEFT JOIN users_upvoted_comments AS uuc ON uuc.comment_id = id\n" +
            "WHERE c.id = ?;";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void upvoteComment(User user, Comment comment) throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            //if the comment is already upvoted
            //then remove the upvote
            if (isCommentUpvoted(user, comment)) {
                try (PreparedStatement statement = connection.prepareStatement(DELETE_UPVOTED_COMMENT_BY_USER_ID);) {
                    statement.setLong(1, user.getId());
                    statement.setLong(2, comment.getId());
                    statement.executeUpdate();
                }
            } else {
                try {
                    connection.setAutoCommit(false);
                    //if the comment is already downvoted
                    //then remove the downvote
                    if (isCommentDownvoted(user, comment)) {
                        try(PreparedStatement statement = connection.prepareStatement(DELETE_DOWNVOTED_COMMENT_BY_USER_ID);) {
                            statement.setLong(1, user.getId());
                            statement.setLong(2, comment.getId());
                            statement.executeUpdate();
                        }
                    }
                    //upvote the comment
                    try(PreparedStatement statement = connection.prepareStatement(INSERT_INTO_UPVOTED);) {
                        statement.setLong(1, user.getId());
                        statement.setLong(2, comment.getId());
                        statement.executeUpdate();

                        connection.commit();
                    }
                }catch (SQLException e){
                    connection.rollback();
                    throw new SQLException("This comments wasn't upvoted!" , e);
                }
                finally {
                    connection.setAutoCommit(true);
                }
            }
        }
    }

    public void downvoteComment(User user, Comment comment) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            //if the comment is already downvoted
            //then remove the downvote
            if (isCommentDownvoted(user, comment)) {
                try(PreparedStatement statement = connection.prepareStatement(DELETE_DOWNVOTED_COMMENT_BY_USER_ID);) {
                    statement.setLong(1, user.getId());
                    statement.setLong(2, comment.getId());
                    statement.executeUpdate();
                }
            } else {
                try {
                    connection.setAutoCommit(false);
                    //if the comment is already upvoted
                    //remove the upvote
                    if (isCommentUpvoted(user, comment)) {
                        try(PreparedStatement statement = connection.prepareStatement(DELETE_UPVOTED_COMMENT_BY_USER_ID);) {
                            statement.setLong(1, user.getId());
                            statement.setLong(2, comment.getId());
                            statement.executeUpdate();
                        }
                    }
                    //downvote the comment
                    try(PreparedStatement statement = connection.prepareStatement(INSERT_INTO_DOWNVOTED);) {
                        statement.setLong(1, user.getId());
                        statement.setLong(2, comment.getId());
                        statement.executeUpdate();

                        connection.commit();
                    }
                }catch (SQLException e){
                    connection.rollback();
                    throw new SQLException("This comments wasn't downvoted!" , e);
                }
                finally {
                    connection.setAutoCommit(true);
                }
            }
        }
    }

    public long getPoints(Comment comment) throws SQLException{
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_POINTS_BY_COMMENT_ID)){
            statement.setLong(1, comment.getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            long numberUpvotes = resultSet.getInt("upvoted");
            long numberDownvotes = resultSet.getInt("downvoted");
            return numberUpvotes - numberDownvotes;
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
