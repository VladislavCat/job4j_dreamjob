package dreamjob.store;

import dreamjob.model.Candidate;
import dreamjob.model.Post;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {
    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public CandidateStore() {
        candidates.put(1, new Candidate(1, "Java Senior Dev", "Spring, Java Core, SQL, Kubernetes", new Date()));
        candidates.put(2, new Candidate(2, "Java Fullstack Java Job", "Spring, Java Core, SQL, Database, Kubernetes", new Date()));
        candidates.put(3, new Candidate(3, "Java Junior Job", "Java Core, SQL", new Date()));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        candidates.putIfAbsent(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidates.replace(candidate.getId(), candidate);
    }
}
