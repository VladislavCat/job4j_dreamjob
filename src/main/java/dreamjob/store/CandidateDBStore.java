package dreamjob.store;

import dreamjob.model.Candidate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static dreamjob.store.PostStore.LOGGER;
import static dreamjob.store.CandidateStore.STOCK_PICTURE;

@Repository
public class CandidateDBStore {
    private final BasicDataSource pool;
    private final String findAll = "SELECT * FROM candidates";
    private final String add = "INSERT INTO candidates(name, description, created, photo)"
            + " VALUES (?, ?, ?, ?)";
    private final String update = "UPDATE candidates SET name = ?, description = ?, "
            + "created = ?, photo = ? WHERE id = ?";
    private final String findById = "SELECT * FROM candidates WHERE id = ?";

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(findAll)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createCandidate(it));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return posts;
    }


    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(add, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            byte[] tmpArr = candidate.getPhoto().length == 0 ? STOCK_PICTURE : candidate.getPhoto();
            ps.setArray(4, getArraySql(tmpArr, cn));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                     candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(update)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            DateFormat date = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
            ps.setDate(3, new Date(date.parse(findById(candidate.getId()).getCreated()).getTime()));
            byte[] tmpArr = candidate.getPhoto().length == 0 ? STOCK_PICTURE : candidate.getPhoto();
            ps.setArray(4, getArraySql(tmpArr, cn));
        } catch (SQLException | ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(findById)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createCandidate(it);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private Array getArraySql(byte[] tmpArr, Connection cn) throws SQLException {
        Byte[] arrayInt = new Byte[tmpArr.length];
        for (int i = 0; i < tmpArr.length; i++) {
            arrayInt[i] = tmpArr[i];
        }
        return cn.createArrayOf("Integer", arrayInt);
    }

    private Candidate createCandidate(ResultSet it) throws SQLException {
        return new Candidate(it.getInt("id"), it.getString("name"),
                it.getString("description"), it.getDate("created").toString(),
                (it.getBytes("photo") == null ? STOCK_PICTURE : it.getBytes("photo")));
    }
}
