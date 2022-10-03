package dreamjob.store;

import dreamjob.model.Candidate;
import dreamjob.model.Post;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateStore {
    private static final CandidateStore INST = new CandidateStore();
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public CandidateStore() {
        candidates.put(1, new Candidate(atomicInteger.getAndIncrement(),
                "Java Senior Dev", "Spring, Java Core, SQL, Kubernetes", new Date()));
        candidates.put(2, new Candidate(atomicInteger.getAndIncrement(), "Java Fullstack Java Job",
                "Spring, Java Core, SQL, Database, Kubernetes", new Date()));
        candidates.put(3, new Candidate(atomicInteger.getAndIncrement(), "Java Junior Job",
                "Java Core, SQL", new Date()));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        candidate.setId(atomicInteger.get());
        candidates.putIfAbsent(atomicInteger.getAndIncrement(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidates.replace(candidate.getId(), candidate);
    }
}
