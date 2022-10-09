package dreamjob.store;

import dreamjob.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CandidateDbStoreTest {

    @BeforeAll
    static void clearTable() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        try (Connection cn = store.getPool().getConnection();
             PreparedStatement ps = cn.prepareStatement("TRUNCATE TABLE posts RESTART IDENTITY")) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenA() {
        System.out.println("ooo");
    }
}
