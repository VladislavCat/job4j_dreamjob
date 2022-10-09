package dreamjob.store;

import dreamjob.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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



}
