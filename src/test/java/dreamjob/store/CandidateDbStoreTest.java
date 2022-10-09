package dreamjob.store;

import dreamjob.Main;
import dreamjob.model.Candidate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import static dreamjob.store.CandidateStore.STOCK_PICTURE;
import static org.assertj.core.api.Assertions.assertThat;

public class CandidateDBStoreTest {

    @BeforeAll
    static void clearBase() {
        PostDBStore postDBStore = new PostDBStore(new Main().loadPool());
        try (Connection cn = postDBStore.getPool().getConnection();
             PreparedStatement ps = cn.prepareStatement("TRUNCATE TABLE posts RESTART IDENTITY")) {
                ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenCreatePost() {
        var store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Java Junior", "Junior Java Developer",
                new Date().toString(), STOCK_PICTURE);
        store.add(candidate);
        Candidate candidateInDB = store.findById(candidate.getId());
        assertThat(candidateInDB.getName()).isEqualTo(candidate.getName());
    }

    @Test
    public void whenFindAll() {
        var store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(0, "Java Junior", "Junior Java Developer",
                new Date().toString(), STOCK_PICTURE);
        store.add(candidate1);
        Candidate candidate2 = new Candidate(1, "Java Senior", "Senior Java Developer",
                new Date().toString(), STOCK_PICTURE);
        store.add(candidate2);
        assertThat(List.of(candidate1, candidate2)).isEqualTo(List.of(store.findById(candidate1.getId()),
                store.findById(candidate2.getId())));
    }

    @Test
    public void whenUpdate() {
        var store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(0, "Java Junior", "Junior Java Developer",
                new Date().toString(), STOCK_PICTURE);
        store.add(candidate1);
        candidate1.setName("Middle Java Developer");
        candidate1.setDesc("Middle Java Developer");
        store.update(candidate1);
        assertThat(candidate1).isEqualTo(store.findById(candidate1.getId()));
    }



}
