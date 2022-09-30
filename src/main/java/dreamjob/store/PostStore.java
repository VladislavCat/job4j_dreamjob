package dreamjob.store;

import dreamjob.model.Post;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {
    private static final PostStore INST = new PostStore();
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(atomicInteger.getAndIncrement(), "Junior Java Job", "Desc1", new Date()));
        posts.put(2, new Post(atomicInteger.getAndIncrement(), "Middle Java Job", "Desc2", new Date()));
        posts.put(3, new Post(atomicInteger.getAndIncrement(), "Senior Java Job", "Desc3", new Date()));
    }

    public void add(Post post) {
        post.setId(atomicInteger.get());
        posts.putIfAbsent(atomicInteger.getAndIncrement(), post);
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
