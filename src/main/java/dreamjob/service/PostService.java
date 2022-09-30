package dreamjob.service;

import dreamjob.model.Post;
import dreamjob.store.PostStore;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class PostService {
    private final PostStore store;

    public PostService(PostStore postStore) {
        store = postStore;
    }

    public Collection<Post> findAll() {
        return store.findAll();
    }

    public void add(Post post) {
        store.add(post);
    }

    public void update(Post post) {
        store.update(post);
    }

    public Post findById(int id) {
        return store.findById(id);
    }
}
