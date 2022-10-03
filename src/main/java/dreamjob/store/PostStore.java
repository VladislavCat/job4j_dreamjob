package dreamjob.store;

import dreamjob.model.City;
import dreamjob.model.Post;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Repository
public class PostStore {
    private static final PostStore INST = new PostStore();
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(atomicInteger.getAndIncrement(), "Junior Java Job",
                "Desc1", new Date(), true, new City(0, "Москва")));
        posts.put(2, new Post(atomicInteger.getAndIncrement(), "Middle Java Job",
                "Desc2", new Date(), true, new City(0, "Москва")));
        posts.put(3, new Post(atomicInteger.getAndIncrement(), "Senior Java Job",
                "Desc3", new Date(), false, new City(0, "Москва")));
    }

    public void add(Post post) {
        post.setId(atomicInteger.get());
        posts.putIfAbsent(atomicInteger.getAndIncrement(), post);
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
