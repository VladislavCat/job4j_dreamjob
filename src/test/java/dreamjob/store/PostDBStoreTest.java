package dreamjob.store;

import dreamjob.Main;
import dreamjob.model.City;
import dreamjob.model.Post;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostDBStoreTest {
    @BeforeAll
    static void clearBase() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        try (Connection cn = store.getPool().getConnection();
             PreparedStatement ps = cn.prepareStatement("TRUNCATE TABLE posts RESTART IDENTITY")) {
                ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", "Java Job vacancy",
                new Date().toString(), true,  new City(1, "Москва"));
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName()).isEqualTo(post.getName());
    }

    @Test
    public void whenFindAll() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post1 = new Post(0, "Java Job", "Java Job vacancy",
                new Date().toString(), true,  new City(1, "Москва"));
        store.add(post1);
        Post post2 = new Post(1, "Python Job", "Python Job vacancy",
                new Date().toString(), true,  new City(3, "ЕКБ"));
        store.add(post2);
        assertThat(List.of(post1, post2)).isEqualTo(List.of(store.findById(post1.getId()),
                store.findById(post2.getId())));
    }

    @Test
    public void whenUpdate() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post1 = new Post(0, "Java Job", "Java Job vacancy",
                new Date().toString(), true,  new City(1, "Москва"));
        store.add(post1);
        post1.setName("Python Job");
        post1.setDescription("Python Job vacancy");
        store.update(post1);
        Post post2 = store.findById(post1.getId());
        assertThat(post1).isEqualTo(store.findById(post1.getId()));
    }
}
