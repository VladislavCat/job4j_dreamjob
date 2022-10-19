package dreamjob.controller;

import dreamjob.model.Post;
import dreamjob.service.CityService;
import dreamjob.service.PostService;
import dreamjob.util.UserName;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    public String posts(Model model,  HttpSession httpSession) {
        UserName.userSessionSetName(model, httpSession);
        model.addAttribute("posts", service.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("post", new Post(0, "Заполните поле",
                "Desc", new Date().toString(), true));
        return "addPost";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post) {
        post.setCity(cityService.findById(post.getCity().getId()));
        service.add(post);
        return "redirect:/posts";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("post", service.findById(id));
        return "updatePost";
    }

    @PostMapping("/updatePost")
    public String fromUpdatePost(@ModelAttribute Post post) {
        post.setCity(cityService.findById(post.getCity().getId()));
        service.update(post);
        return "redirect:/posts";
    }
}