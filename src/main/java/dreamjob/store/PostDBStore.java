package dreamjob.store;

import dreamjob.Main;
import dreamjob.model.City;
import dreamjob.model.Post;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;



@Repository
public class PostDBStore {
    private final BasicDataSource pool;
    private final String findAll = "SELECT * FROM posts";
    private final String add = "INSERT INTO posts(name, description, created, visible, city_id)"
            + " VALUES (?, ?, ?, ?, ?)";
    private final String update = "UPDATE posts SET name = ?, description = ?, "
            + "created = ?, visible = ?, city_id = ? WHERE id = ?";
    private final String findById = "SELECT * FROM posts WHERE id = ?";
    private final Logger logger = LoggerFactory.getLogger(PostDBStore.class);

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
                    posts.add(new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"),
                            it.getDate("created").toString(), it.getBoolean("visible"),
                            new City(it.getInt("city_id"), "")));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
            logger.error(e.getMessage(), e);
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(update)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ps.setDate(3, new Date(formatter.parse(findById(post.getId()).getCreated()).getTime()));
            ps.setBoolean(4, post.isVisible());
            ps.setInt(5, post.getCity().getId());
            ps.setInt(6, post.getId());
            ps.execute();
        } catch (SQLException | ParseException e) {
            logger.error(e.getMessage(), e);
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
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public BasicDataSource getPool() {
        return pool;
    }
}

