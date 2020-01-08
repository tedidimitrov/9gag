package finalproject.ninegag.model.dao;

import finalproject.ninegag.model.pojo.Category;
import finalproject.ninegag.model.pojo.Post;
import finalproject.ninegag.model.pojo.User;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostDAO {

    private static final String GET_MY_POSTS = "SELECT id,title,image_url,date_uploaded,points,category_id FROM posts WHERE post_owner_id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Post> getPostsByUser(User user) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(GET_MY_POSTS)){
            statement.setLong(1,user.getId());
            ResultSet rows = statement.executeQuery();
            List<Post> posts = new ArrayList<>();
            while (rows.next()){
                Post post = new Post(
                        rows.getLong("id"),
                        rows.getString("title"),
                        rows.getTimestamp("date_uploaded").toLocalDateTime(),
                        rows.getString("image_url"),
                        rows.getInt("points"),
                        user,
                        Category.values()[rows.getInt("category_id")]);
                posts.add(post);
            }
            return posts;
        }
    }
}
