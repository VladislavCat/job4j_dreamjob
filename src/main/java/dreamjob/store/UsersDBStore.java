package dreamjob.store;

import dreamjob.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UsersDBStore {
    private final BasicDataSource pool;
    private final Logger logger = LoggerFactory.getLogger(UsersDBStore.class);
    private final String add = "INSERT INTO users(email, password) VALUES(?, ?)";

    public UsersDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(add, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.of(user);
    }

    public Optional<User> findUserByEmailAndPwd(String mail, String password) {
        User rsl = null;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("select * from users where email = ? and password = ?")) {
                ps.setString(1, mail);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery();) {
                    if (rs.next()) {
                        rsl = new User(rs.getString(1), rs.getString(2));
                    }
                }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return rsl == null ? Optional.empty() : Optional.of(rsl);
    }


}
