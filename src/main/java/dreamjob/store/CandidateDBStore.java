package dreamjob.store;

import dreamjob.Main;
import dreamjob.model.Candidate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    private final Logger logger = LoggerFactory.getLogger(Main.class);

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
            logger.error(e.getMessage(), e);
        }
        return posts;
    }


    public Candidate add(Candidate candidate) {
        validateImageCandidate(candidate);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(add, PreparedStatement.RETURN_GENERATED_KEYS);
             FileOutputStream fos = new FileOutputStream("candidate_photo.png");
             FileInputStream fis = new FileInputStream("candidate_photo.png")
        ) {
            fos.write(candidate.getPhoto());
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            File file = new File("candidate_photo.png");
            ps.setBinaryStream(4, fis, file.length());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                     candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        validateImageCandidate(candidate);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(update);
             FileOutputStream fos = new FileOutputStream("candidate_photo.png");
             FileInputStream fis = new FileInputStream("candidate_photo.png")) {
            fos.write(candidate.getPhoto());
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            ps.setDate(3, new Date(date.parse(findById(candidate.getId()).getCreated()).getTime()));
            File file = new File("candidate_photo.png");
            ps.setBinaryStream(4, fis, file.length());
            ps.setInt(5, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(findById)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createCandidate(it);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private Candidate createCandidate(ResultSet it) throws SQLException {
        return new Candidate(it.getInt("id"), it.getString("name"),
                it.getString("description"), it.getDate("created").toString(),
                it.getBytes("photo"));
    }

    private Array getArraySql(byte[] tmpArr, Connection cn) throws SQLException {
        tmpArr = org.apache.commons.codec.binary.Base64.encodeBase64(tmpArr);
        String[] arrayInt = new String[tmpArr.length];
        for (int i = 0; i < tmpArr.length; i++) {
            arrayInt[i] = Integer.toHexString(tmpArr[i]);
        }
        return cn.createArrayOf("bytea", arrayInt);
    }

    private void validateImageCandidate(Candidate candidate) {
        if (candidate.getPhoto().length == 0) {
            candidate.setPhoto(STOCK_PICTURE);
        }
    }
}
