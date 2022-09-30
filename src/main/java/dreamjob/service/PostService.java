package dreamjob.service;

import dreamjob.model.Post;
import dreamjob.store.PostStore;
import java.util.Collection;

public class PostService {
    private static final PostService INST = new PostService();
    private final PostStore store = PostStore.instOf();

    private PostService() {
    }

    public static PostService instOf() {
        return INST;
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
