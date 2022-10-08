package dreamjob.store;

import dreamjob.model.Candidate;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Repository
public class CandidateStore {
    public static final byte[] STOCK_PICTURE = initStore();
    private final AtomicInteger atomicInteger = new AtomicInteger(1);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public CandidateStore() {
        add(new Candidate(atomicInteger.get(),
                "Java Senior Dev", "Spring, Java Core, SQL, Kubernetes",
                new Date().toString(), STOCK_PICTURE));
        add(new Candidate(atomicInteger.get(), "Java Fullstack Java Job",
                "Spring, Java Core, SQL, Database, Kubernetes", new Date().toString(), STOCK_PICTURE));
        add(new Candidate(atomicInteger.get(), "Java Junior Job",
                "Java Core, SQL", new Date().toString(), STOCK_PICTURE));
    }

    public static byte[] initStore() {
        byte[] rsl = {};
        try (FileInputStream fis = new FileInputStream("stock_picture.png")) {
            rsl = fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        candidate.setId(atomicInteger.get());
        candidate.setCreated(new Date().toString());
        candidate.setPhoto(candidate.getPhoto().length == 0 ? STOCK_PICTURE : candidate.getPhoto());
        candidates.putIfAbsent(atomicInteger.getAndIncrement(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidate.setCreated(findById(candidate.getId()).getCreated());
        candidate.setPhoto(candidate.getPhoto().length == 0 ? STOCK_PICTURE : candidate.getPhoto());
        candidates.replace(candidate.getId(), candidate);
    }
}
