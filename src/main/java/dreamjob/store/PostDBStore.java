package dreamjob.store;

import dreamjob.model.City;
import dreamjob.model.Post;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;

import static dreamjob.Main.LOGGER;

@Repository
public class PostDBStore {
    private final BasicDataSource pool;
    private final String findAll = "SELECT * FROM posts";
    private final String add = "INSERT INTO posts(name, description, created, visible, city_id)"
            + " VALUES (?, ?, ?, ?, ?)";
    private final String update = "UPDATE posts SET name = ?, description = ?, "
            + "created = ?, visible = ?, city_id = ? WHERE id = ?";
    private final String findById = "SELECT * FROM posts WHERE id = ?";

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(findAll)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"), it.getString("description"),
                            it.getDate("created").toString(), it.getBoolean("visible"),
                            new City(it.getInt("city_id"), "")));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return posts;
    }


    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(add, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            ps.setBoolean(4, post.isVisible());
            ps.setInt(5, post.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(update)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            DateFormat date = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
            ps.setDate(3, new Date(date.parse(findById(post.getId()).getCreated()).getTime()));
            ps.setBoolean(4, post.isVisible());
            ps.setInt(5, post.getCity().getId());
        } catch (SQLException | ParseException e) {
            LOGGER.error(e.toString());
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(findById)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"), it.getString("name"), it.getString("description"),
                            it.getDate("created").toString(),
                            it.getBoolean("visible"), new City(it.getInt("city_id"), ""));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return null;
    }
}

