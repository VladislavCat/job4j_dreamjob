package dreamjob.store;

import dreamjob.model.Post;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {
    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Post> candidates = new ConcurrentHashMap<>();

    public CandidateStore() {
        candidates.put(1, new Post(1, "Java Senior Dev", "Spring, Java Core, SQL, Kubernetes", new Date()));
        candidates.put(2, new Post(2, "Java Fullstack Java Job", "Spring, Java Core, SQL, Database, Kubernetes", new Date()));
        candidates.put(3, new Post(3, "Java Junior Job", "Java Core, SQL", new Date()));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return candidates.values();
    }
}
