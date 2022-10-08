package dreamjob.service;

import dreamjob.model.Candidate;
import dreamjob.store.CandidateDBStore;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class CandidateService {
    private final CandidateDBStore store;

    public CandidateService(CandidateDBStore store) {
        this.store = store;
    }

    public Collection<Candidate> findAll() {
        return store.findAll();
    }

    public void add(Candidate candidate) {
        store.add(candidate);
    }

    public Candidate findById(int id) {
        return store.findById(id);
    }

    public void update(Candidate candidate) {
        store.update(candidate);
    }
}
