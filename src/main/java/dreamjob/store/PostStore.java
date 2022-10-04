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
    private final AtomicInteger atomicInteger = new AtomicInteger(1);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
            add(new Post(atomicInteger.get(), "Junior Java Job",
                "Desc1", new Date().toString(), true, new City(0, "Москва")));
            add(new Post(atomicInteger.get(), "Middle Java Job",
                "Desc2", new Date().toString(), true, new City(0, "Москва")));
            add(new Post(atomicInteger.get(), "Senior Java Job",
                "Desc3", new Date().toString(), false, new City(0, "Москва")));
    }

    public void add(Post post) {
        post.setId(atomicInteger.get());
        post.setCreated(new Date().toString());
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
