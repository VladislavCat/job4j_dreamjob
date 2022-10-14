package dreamjob.service;

import dreamjob.model.User;
import dreamjob.store.UsersDBStore;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UsersDBStore store;

    public UserService(UsersDBStore store) {
        this.store = store;
    }

    public Optional<User> add(User user) {
        return Optional.of(store.add(user));
    }
}
