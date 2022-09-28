package dreamjob.store;

import dreamjob.model.Post;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostStore {
    private static final PostStore INST = new PostStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Desc1", new Date()));
        posts.put(2, new Post(2, "Middle Java Job", "Desc2", new Date()));
        posts.put(3, new Post(3, "Senior Java Job", "Desc3", new Date()));
    }

    public void add(Post post) {
        posts.putIfAbsent(post.getId(), post);
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        posts.replace(post.getId(), post);
    }
}
