package mk.ukim.finki.ib.filesharing.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.service.UploadedFileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping({"", "/", "/home", "/files"})
@AllArgsConstructor
public class TempController {
    private final UploadedFileService fileService;

    @GetMapping
    public String index() {
        return "home";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access")
    public String showAllAccessibleFilesByUsername(@AuthenticationPrincipal User user, Model model) {
        List<UploadedFile> files = fileService.findByAccess(user.getUsername());
        model.addAttribute("files", files);
        return "files-access"; // This is the name of the Thymeleaf template for accessible files
    }

    @GetMapping("/created")
    public String showCreatedByUser(@AuthenticationPrincipal User user, Model model) {
        List<UploadedFile> files = fileService.findByOwner(user.getUsername());
        model.addAttribute("files", files);
        return "files-created"; // This is the name of the Thymeleaf template for files created by the user
    }
}
