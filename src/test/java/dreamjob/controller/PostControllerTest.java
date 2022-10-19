package dreamjob.controller;

import dreamjob.model.City;
import dreamjob.model.Post;
import dreamjob.service.CityService;
import dreamjob.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post", "Desc Post", new Date().toString(), true),
                new Post(2, "New post", "Desc Post", new Date().toString(), true)
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.posts(model, new MockHttpSession());
        verify(model).addAttribute("posts", posts);
        assertThat(page).isEqualTo("posts");
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "New post",
                "Desc Post", new Date().toString(), true, new City(1, ""));
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).add(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenUpdatePost() {
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        Post updatePost = new Post(1, "New post",
                "Desc Post", new Date().toString(), true, new City(1, ""));
        String page = postController.fromUpdatePost(updatePost);
        verify(postService).update(updatePost);
        assertThat(page).isEqualTo("redirect:/posts");
    }
}
