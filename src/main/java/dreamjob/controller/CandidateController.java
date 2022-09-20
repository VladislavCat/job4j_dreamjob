package dreamjob.controller;

import dreamjob.store.CandidateStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class CandidateController {
    private final CandidateStore store = CandidateStore.instOf();

    @GetMapping("/candidates")
    public String posts(Model model) {
        model.addAttribute("candidates", store.findAll());
        return "candidates";
    }
}
