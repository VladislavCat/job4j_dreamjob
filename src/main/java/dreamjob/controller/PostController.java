package dreamjob.controller;

import dreamjob.model.Post;
import dreamjob.service.CityService;
import dreamjob.service.PostService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
@ThreadSafe
@Controller
public class PostController {
    private final PostService service;
    private final CityService cityService;

    public PostController(PostService postService, CityService cityService) {
        this.service = postService;
        this.cityService = cityService;
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", service.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("post", new Post(0, "Заполните поле", "Desc", new Date(), true));
        return "addPost";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post) {
        service.add(post);
        return "redirect:/posts";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", service.findById(id));
        return "updatePost";
    }

    @PostMapping("/updatePost")
    public String fromUpdatePost(@ModelAttribute Post post) {
        service.update(post);
        return "redirect:/posts";
    }
}